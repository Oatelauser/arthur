package com.broadtech.arthur.admin.auth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;

import java.security.KeyPair;
import java.util.Arrays;
import java.util.List;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/8/28
 */
@Configuration
@RequiredArgsConstructor
public class TokenConfig {

    private static final String PASSWORD = "123456";

    private final JwtTokenEnhancer jwtTokenEnhancer;

    /**
     * 默认有效时间(单位秒)
     */
    private static final int DEFAULT_VALID_TIME = 3 * 60 * 60;
    private static final int DEFAULT_VALID_REFRESH_TIME = 6 * 60 * 60;

    @Bean
    public TokenStore jwtTokenStore(@Qualifier("accessTokenConverter") JwtAccessTokenConverter jwtAccessTokenConverter) {
        return new JwtTokenStore(jwtAccessTokenConverter);
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter(KeyPair keyPair) {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setKeyPair(keyPair);
        return jwtAccessTokenConverter;
    }

    @Bean
    public KeyPair keyPair() {
        //从classpath下的证书中获取秘钥对
        String path = "jwt.jks";
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource(path), PASSWORD.toCharArray());
        return keyStoreKeyFactory.getKeyPair("jwt", PASSWORD.toCharArray());
    }

    @Bean
    public AuthorizationServerTokenServices tokenServices(ClientDetailsService clientService, TokenStore tokenStore, @Qualifier("accessTokenConverter") JwtAccessTokenConverter jwtAccessTokenConverter) {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setClientDetailsService(clientService);
        defaultTokenServices.setSupportRefreshToken(true);
        defaultTokenServices.setTokenStore(tokenStore);

        //令牌增强
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();

        //TODO 一定要注意顺序,先执行 自定义 enhancer 添加信息 然后尽心加密生成token
        tokenEnhancerChain.setTokenEnhancers(List.of(jwtTokenEnhancer,jwtAccessTokenConverter));
        defaultTokenServices.setTokenEnhancer(tokenEnhancerChain);

        //默认时间
        defaultTokenServices.setAccessTokenValiditySeconds(DEFAULT_VALID_TIME);
        //刷新时间
        defaultTokenServices.setRefreshTokenValiditySeconds(DEFAULT_VALID_REFRESH_TIME);
        return defaultTokenServices;
    }


}
