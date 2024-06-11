package com.broadtech.arthur.admin.auth.config;

import cn.hutool.core.map.MapUtil;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.client.InMemoryClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/9/15
 */
@Configuration
public class ClientDetailConfig {


    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    @Primary
    public ClientDetailsService clientService(DataSource dataSource) throws Exception {
        return jdbcClientDetailService(dataSource);
    }

    private ClientDetailsService memClientDetailService() throws Exception {
        final String client = "client-app";
        final String clientPass = "123456";
        InMemoryClientDetailsService memoryClientDetailsService = new InMemoryClientDetailsService();
        BaseClientDetails clientDetails = new BaseClientDetails();
        clientDetails.setClientSecret(passwordEncoder.encode(clientPass));
        clientDetails.setAuthorizedGrantTypes(List.of("password", "refresh_token"));
        clientDetails.setScope(List.of("all"));
        clientDetails.setClientId(client);
        clientDetails.setAccessTokenValiditySeconds(300000);
        clientDetails.setRefreshTokenValiditySeconds(3000000);
        Map<String, ClientDetails> map = MapUtil.newHashMap(1);
        map.put(client, clientDetails);
        memoryClientDetailsService.setClientDetailsStore(map);
        return memoryClientDetailsService;

    }

    private ClientDetailsService jdbcClientDetailService(DataSource dataSource) throws Exception {
        JdbcClientDetailsService clientDetailsService = new JdbcClientDetailsService(dataSource);
        clientDetailsService.setPasswordEncoder(passwordEncoder);
        return clientDetailsService;
    }
}
