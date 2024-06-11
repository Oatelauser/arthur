package com.arthur.plugin.route.autoconfigure;

import com.arthur.plugin.route.core.GatewayRouteDefinition;
import com.arthur.plugin.route.core.GatewayRouteDefinitions;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

/**
 * 拓展网关路由配置信息
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2022-12-30
 * @see GatewayProperties
 * @since 1.0
 */
@Validated
@ConfigurationProperties(prefix = GatewayProperties.PREFIX)
public class GatewayRouteProperties {

	private static final Logger LOG = LoggerFactory.getLogger(GatewayRouteConfiguration.class);

	private final GatewayRouteDefinitions gatewayRouteDefinitions;

	public GatewayRouteProperties(GatewayRouteDefinitions gatewayRouteDefinitions) {
		this.gatewayRouteDefinitions = gatewayRouteDefinitions;
	}

	/**
	 * List of Routes.
	 */
	@Valid
	@NotNull
	private List<GatewayRouteDefinition> routes = new ArrayList<>();

	public List<GatewayRouteDefinition> getRoutes() {
		return routes;
	}

	/**
	 * @see GatewayProperties#setRoutes(List)
	 */
	public void setRoutes(List<GatewayRouteDefinition> routes) {
		this.routes = routes;
		if (CollectionUtils.isEmpty(routes)) {
			return;
		}
		routes.forEach(gatewayRouteDefinitions::asyncRouteMetadata);
		if (LOG.isDebugEnabled()) {
			LOG.debug("Routes supplied from Gateway Properties: " + routes);
		}
	}

}
