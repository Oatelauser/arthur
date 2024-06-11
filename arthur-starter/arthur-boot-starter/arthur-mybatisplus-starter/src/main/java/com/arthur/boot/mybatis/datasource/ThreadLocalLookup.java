package com.arthur.boot.mybatis.datasource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 基于{@link ThreadLocal}实现动态路由查找键
 *
 * @author DearYang
 * @date 2022-09-24
 * @see InheritableThreadLocal
 * @since 1.0
 */
public class ThreadLocalLookup implements RouteLookup {

	private static final Logger LOG = LoggerFactory.getLogger(ThreadLocalLookup.class);

	private static final ThreadLocal<String> DATA_SOURCE_ROUTE = new ThreadLocal<>();

	@Override
	public String get() {
		return DATA_SOURCE_ROUTE.get();
	}

	@Override
	public void choose(String routeKey) {
		DATA_SOURCE_ROUTE.set(routeKey);
		if (LOG.isTraceEnabled()) {
			LOG.trace("Switch database key [{}]", routeKey);
		}
	}

	@Override
	public void remove(String route) {
		DATA_SOURCE_ROUTE.remove();
	}

	@Override
	public void clear() {
		this.remove(null);
	}

}
