package com.arthur.plugin.util;

import org.springframework.cloud.gateway.config.conditional.OnEnabledFilter;
import org.springframework.cloud.gateway.config.conditional.OnEnabledGlobalFilter;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;

/**
 * 网关过滤器条件匹配的key
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-03-22
 * @since 1.0
 */
public abstract class GatewayFilterFormatter {

	private static final String PREFIX = "spring.cloud.gateway.";
	private static final String SUFFIX = ".enabled";


	/**
	 * 获取网关全局过滤器开关的key
	 *
	 * @param filterClass 网关全局过滤器
	 * @return key
	 */
	public static String obtainsGlobalFilterEnabledKey(Class<? extends GlobalFilter> filterClass) {
		return PREFIX + new GlobalFilterFormatter().normalizeComponentName(filterClass) + SUFFIX;
	}

	/**
	 * 获取网关过滤器开关的key
	 *
	 * @param filterClass 网关过滤器
	 * @return key
	 */
	public static String obtainsGatewayFilterFactoryEnabledKey(Class<? extends GatewayFilterFactory<?>> filterClass) {
		return PREFIX + new GatewayFilterFactoryFormatter().normalizeComponentName(filterClass) + SUFFIX;
	}

	private static class GlobalFilterFormatter extends OnEnabledGlobalFilter {

		@Override
		protected String normalizeComponentName(Class<? extends GlobalFilter> filterClass) {
			return super.normalizeComponentName(filterClass);
		}

	}

	private static class GatewayFilterFactoryFormatter extends OnEnabledFilter {

		@Override
		protected String normalizeComponentName(Class<? extends GatewayFilterFactory<?>> filterClass) {
			return super.normalizeComponentName(filterClass);
		}

	}

}
