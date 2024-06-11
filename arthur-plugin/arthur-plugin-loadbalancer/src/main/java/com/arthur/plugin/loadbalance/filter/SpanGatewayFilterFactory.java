package com.arthur.plugin.loadbalance.filter;

import com.arthur.cloud.gateway.AbstractSmartGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;

/**
 * 概要描述
 * <p>
 * 详细描述
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-15
 * @since 1.0
 */
public class SpanGatewayFilterFactory extends AbstractSmartGatewayFilterFactory<SpanGatewayFilterFactory.Config> {

	@Override
	public GatewayFilter apply(Config config) {
		return null;
	}

	public static class Config {

	}

}
