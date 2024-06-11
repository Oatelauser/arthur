package com.arthur.plugin.route.core;

import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.cloud.gateway.handler.predicate.RoutePredicateFactory;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteDefinitionRouteLocator;
import org.springframework.cloud.gateway.support.ConfigurationService;
import org.springframework.core.Ordered;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * 拓展网关路由{@link Route}新增属性字段
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2022-12-30
 * @since 1.0
 */
public class GatewayRouteLocator extends RouteDefinitionRouteLocator implements Ordered {

	public GatewayRouteLocator(RouteDefinitionLocator routeDefinitionLocator,
		   List<RoutePredicateFactory> predicates, List<GatewayFilterFactory> gatewayFilterFactories,
		   GatewayProperties gatewayProperties, ConfigurationService configurationService) {
		super(routeDefinitionLocator, predicates, gatewayFilterFactories, gatewayProperties, configurationService);
	}

	@Override
	public Flux<Route> getRoutes() {
		return super.getRoutes();
	}

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}

}
