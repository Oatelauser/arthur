package com.arthur.plugin.route.autoconfigure;

import com.arthur.plugin.route.core.*;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.config.PropertiesRouteDefinitionLocator;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.cloud.gateway.handler.predicate.RoutePredicateFactory;
import org.springframework.cloud.gateway.route.*;
import org.springframework.cloud.gateway.support.ConfigurationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * 网关路由拓展配置
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2022-12-30
 * @since 1.0
 */
@Configuration(proxyBeanMethods = false)
public class GatewayRouteConfiguration {

	@Bean
	public GatewayRouteAccessor gatewayRouteArgsAccessor() {
		return new GatewayRouteArgsAccessor();
	}

	@Bean
	public GatewayRouteProperties gatewayRouteProperties(GatewayRouteDefinitions gatewayRouteDefinitions) {
		return new GatewayRouteProperties(gatewayRouteDefinitions);
	}

	/**
	 * 覆盖网关的{@link PropertiesRouteDefinitionLocator} Bean对象
	 *
	 * @param properties {@link GatewayRouteProperties}
	 */
	@Bean
	@ConditionalOnMissingBean
	public PropertiesRouteDefinitionLocator gatewayRouteDefinitionLocator(GatewayRouteProperties properties) {
		return new GatewayRouteDefinitionLocator(properties);
	}

	@Bean
	@ConditionalOnMissingBean
	public GatewayRouteDefinitions gatewayRouteDefinitions(ObjectProvider<List<GatewayRouteAccessor>> provider) {
		return new GatewayRouteDefinitions(provider);
	}

	//@Bean
	@SuppressWarnings("rawtypes")
	@ConditionalOnMissingBean(GatewayRouteLocator.class)
	public GatewayRouteLocator gatewayRouteLocator(GatewayProperties properties,
		   List<GatewayFilterFactory> gatewayFilters, List<RoutePredicateFactory> predicates,
		   RouteDefinitionLocator routeDefinitionLocator, ConfigurationService configurationService) {
		return new GatewayRouteLocator(routeDefinitionLocator, predicates, gatewayFilters, properties, configurationService);
	}

	/**
	 * 删除Gateway中定义的{@link RouteDefinitionRouteLocator}，使用自定义拓展的{@link GatewayRouteLocator}
	 *
	 * @see org.springframework.cloud.gateway.config.GatewayAutoConfiguration#cachedCompositeRouteLocator(List)
	 */
	//@Bean
	@Primary
	@ConditionalOnMissingBean(name = "cachingCompositeRouteLocator")
	public RouteLocator cachingCompositeRouteLocator(List<RouteLocator> routeLocators) {
		// TODO：Gateway无法取代RouteDefinitionRouteLocator Bean对象
		if (routeLocators.stream().anyMatch(routeLocator -> routeLocator instanceof GatewayRouteLocator)) {
			routeLocators.removeIf(routeLocator -> RouteDefinitionRouteLocator.class.equals(routeLocator.getClass()));
		}
		return new CachingRouteLocator(new CompositeRouteLocator(Flux.fromIterable(routeLocators)));
	}

}
