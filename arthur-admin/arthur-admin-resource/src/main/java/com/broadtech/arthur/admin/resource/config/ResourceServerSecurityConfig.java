package com.broadtech.arthur.admin.resource.config;

import cn.hutool.core.util.ArrayUtil;
import com.broadtech.arthur.admin.resource.filter.IgnoreUrlsRemoveJwtFilter;
import com.broadtech.arthur.admin.resource.security.JwtAuthorizationManager;
import com.broadtech.arthur.admin.resource.security.RequestAccessDeniedHandler;
import com.broadtech.arthur.admin.resource.security.RequestAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/9/9
 */
@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class ResourceServerSecurityConfig {

    private final JwtAuthorizationManager authorizationManager;
    private final RequestAccessDeniedHandler requestAccessDeniedHandler;
    private final RequestAuthenticationEntryPoint requestAuthenticationEntryPoint;
    private final ReactiveJwtAuthenticationConverterAdapter converter;
    private final IgnoreUrlsRemoveJwtFilter ignoreUrlsRemoveJwtFilter;
    private final IgnoreUrlsConfig ignoreUrlsConfig;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {

        http.oauth2ResourceServer().jwt()
                .jwtAuthenticationConverter(converter);

        http.oauth2ResourceServer().authenticationEntryPoint(requestAuthenticationEntryPoint);

        //move auth
        http.addFilterBefore(ignoreUrlsRemoveJwtFilter, SecurityWebFiltersOrder.AUTHENTICATION);

        http.authorizeExchange()
                //白名单
                .pathMatchers(ArrayUtil.toArray(ignoreUrlsConfig.getUrls(), String.class)).permitAll()
                //鉴权管理器配置
                .anyExchange().access(authorizationManager)
                .and().exceptionHandling()
                //处理未授权
                .accessDeniedHandler(requestAccessDeniedHandler)
                //处理未认证
                .authenticationEntryPoint(requestAuthenticationEntryPoint)
                .and()
                .csrf().disable();
        return http.build();
    }


}
