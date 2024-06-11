package com.broadtech.arthur.admin.auth.config;

import com.broadtech.arthur.admin.auth.service.impl.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.*;

/**
 * 1.client访问配置
 * 2.token生成配置
 * 3.令牌颁发端点及其安全配置
 *
 * @author Machenike
 * @version 1.0.0
 * @date 2022/8/28
 */
@Configuration
@EnableAuthorizationServer
@RequiredArgsConstructor
public class Oauth2AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final AuthenticationManager authenticationManager;
    private final ClientDetailsService clientDetailsService;
    private final AuthorizationCodeServices codeServices;
    private final AuthorizationServerTokenServices tokenServices;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetailsService);
    }


    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

        endpoints.authorizationCodeServices(codeServices)
                .tokenServices(tokenServices)
                .authenticationManager(authenticationManager)
                //配置加载用户信息的服务
                .userDetailsService(userDetailsServiceImpl)
                .allowedTokenEndpointRequestMethods(HttpMethod.POST);
    }


    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.allowFormAuthenticationForClients();
    }
}
