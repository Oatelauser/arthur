package com.arthur.boot.mybatis.datasource;

import org.springframework.lang.Nullable;

/**
 * 动态多数据源路由键
 *
 * @author DearYang
 * @date 2022-09-23
 * @since 1.0
 */
public interface RouteLookup {

	/**
	 * 获取当前路由键
	 *
	 * @return 路由键
	 */
	String get();

	/**
	 * 切换当前路由键
	 *
	 * @param routeKey 切换后的路由键
	 */
	void choose(String routeKey);

	/**
	 * 删除的路由键
	 *
	 * @param route 路由键
	 */
	void remove(@Nullable String route);

	/**
	 * 清除所有路由键
	 */
	void clear();

	/**
	 * 获取默认的路由键
	 *
	 * @return 默认的路由键
	 */
	default String getDefaultRouteKey() {
		return this.get();
	}

}
