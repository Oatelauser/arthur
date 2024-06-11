package com.arthur.boot.mybatis.datasource;

import com.arthur.boot.context.ApplicationContextHolder;

/**
 * 动态数据源管理器
 *
 * @author DearYang
 * @date 2022-09-27
 * @since 1.0
 */
public class DynamicDataSourceManager {

	/**
	 * 获取当前数据源的名称
	 *
	 * @return 数据源的名称
	 */
	public static String get() {
		return Holder.INSTANCE.get();
	}

	/**
	 * 切换数据源
	 *
	 * @param databaseName 数据源名称
	 */
	public static void choose(String databaseName) {
		Holder.INSTANCE.choose(databaseName);
	}

	private static final class Holder {
		private static final RouteLookup INSTANCE = ApplicationContextHolder.getBean(RouteLookup.class);
	}

}
