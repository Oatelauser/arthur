package com.arthur.oauth2.autoconfigure;

import com.arthur.oauth2.feign.OAuth2TokenRequestInterceptor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;

/**
 * OpenFeign自动配置类
 *
 * @author DearYang
 * @date 2022-08-14
 * @since 1.0
 */
@AutoConfiguration
@ConditionalOnClass(name = "feign.RequestInterceptor")
public class OpenFeignAutoConfiguration {

    @Bean
    public OAuth2TokenRequestInterceptor oAuth2RequestInterceptor(BearerTokenResolver tokenResolver) {
        return new OAuth2TokenRequestInterceptor(tokenResolver);
    }

}
