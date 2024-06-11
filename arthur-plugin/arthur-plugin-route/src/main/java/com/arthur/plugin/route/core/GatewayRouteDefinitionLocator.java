package com.arthur.plugin.route.core;

import com.arthur.plugin.route.autoconfigure.GatewayRouteProperties;
import org.springframework.cloud.gateway.config.PropertiesRouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteDefinition;
import reactor.core.publisher.Flux;

/**
 * 覆盖网关默认的路由查找器
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2022-12-30
 * @see PropertiesRouteDefinitionLocator
 * @since 1.0
 */
public class GatewayRouteDefinitionLocator extends PropertiesRouteDefinitionLocator {

	private final GatewayRouteProperties properties;

	public GatewayRouteDefinitionLocator(GatewayRouteProperties properties) {
		super(null);
		this.properties = properties;
	}

	@Override
	public Flux<RouteDefinition> getRouteDefinitions() {
		return Flux.fromIterable(this.properties.getRoutes());
	}

}
