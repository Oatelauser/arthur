package com.arthur.plugin.circuitbreaker.autoconfigure;

import com.arthur.plugin.circuitbreaker.key.KeyResolverFactory;
import com.arthur.plugin.circuitbreaker.key.RemoteIpKeyResolver;
import com.arthur.plugin.circuitbreaker.key.RouteIdKeyResolver;
import com.arthur.plugin.circuitbreaker.key.UrlPathKeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.PrincipalNameKeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;

import static com.arthur.plugin.circuitbreaker.constant.CircuitBreakerConstant.*;

/**
 * Redis限流的key的生成策略
 *
 * @author DearYang
 * @date 2022-08-11
 * @see KeyResolver
 * @since 1.0
 */
@Configuration(proxyBeanMethods = false)
public class KeyResolverConfiguration {

	/**
	 * IP限流
	 */
    @Primary
    @Bean(name = KEY_RESOLVER_REMOTE_ADDR)
    public KeyResolver remoteAddrKeyResolver() {
        return new RemoteIpKeyResolver();
    }

	/**
	 * 请求路径限流
	 */
    @Bean(name = KEY_RESOLVER_URL_PATH)
    public KeyResolver urlPathKeyResolver() {
        return new UrlPathKeyResolver();
    }

	/**
	 * 路由ID限流
	 */
    @Bean(name = KEY_RESOLVER_ROUTE_ID)
    public KeyResolver routeIdKeyResolver() {
        return new RouteIdKeyResolver();
    }

	/**
	 * 用户限流
	 */
    @Bean(name = KEY_RESOLVER_PRINCIPAL)
    public KeyResolver principalKeyResolver() {
        return new PrincipalNameKeyResolver();
    }

	@Bean
	public KeyResolverFactory keyResolverFactory(List<KeyResolver> keyResolvers) {
		return new KeyResolverFactory(keyResolvers);
	}

}
