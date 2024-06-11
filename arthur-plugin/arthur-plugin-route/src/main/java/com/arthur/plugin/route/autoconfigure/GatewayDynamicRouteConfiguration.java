package com.arthur.plugin.route.autoconfigure;

import com.arthur.plugin.route.ArthurDynamicRoute;
import com.arthur.plugin.route.ArthurDynamicRouteAdapter;
import com.arthur.plugin.route.GatewayDynamicRoute;
import com.arthur.plugin.route.core.GatewayRouteDefinitions;
import com.arthur.common.config.ConfigTemplate;
import com.arthur.plugin.route.subscribe.AbstractRouteSubscriber;
import com.arthur.plugin.route.subscribe.RouteListener;
import com.arthur.plugin.route.subscribe.RouteMultiConfigListener;
import com.arthur.plugin.route.subscribe.RouteSingleConfigListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 网关动态路由配置
 *
 * @author DearYang
 * @date 2022-07-25
 * @since 1.0
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(ArthurRouteProperties.class)
@ConditionalOnProperty(value = "arthur.route.enabled", havingValue = "true", matchIfMissing = true)
public class GatewayDynamicRouteConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public GatewayDynamicRoute dynamicRoute(RouteDefinitionWriter writer, RouteDefinitionLocator locator) {
		return new ArthurDynamicRoute(writer, locator);
	}

	@Bean
	@ConditionalOnMissingBean
	public AbstractRouteSubscriber routeSubscriber(GatewayDynamicRoute dynamicRoute, GatewayRouteDefinitions gatewayRouteDefinitions) {
		return new ArthurDynamicRouteAdapter(dynamicRoute, gatewayRouteDefinitions);
	}

	@Bean
	@ConditionalOnProperty(value = "arthur.route.multiRoute.enabled", havingValue = "true")
	public RouteListener multiConfigListener(ArthurRouteProperties routeProperties, ConfigTemplate configTemplate, AbstractRouteSubscriber routeSubscriber) {
		return new RouteMultiConfigListener(routeProperties.getMultiRoute(), configTemplate, routeSubscriber);
	}

	@Bean
	@ConditionalOnMissingBean(RouteListener.class)
	public RouteListener singleConfigListener(ArthurRouteProperties routeProperties, ConfigTemplate configTemplate, AbstractRouteSubscriber routeSubscriber) {
		return new RouteSingleConfigListener(routeProperties, configTemplate, routeSubscriber);
	}

}
