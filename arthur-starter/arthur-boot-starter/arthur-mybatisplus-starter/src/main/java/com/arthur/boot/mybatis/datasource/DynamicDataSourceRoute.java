package com.arthur.boot.mybatis.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.lang.NonNull;

/**
 * 基于Spring提供的{@link AbstractRoutingDataSource}实现多数据源
 *
 * @author DearYang
 * @date 2022-09-22
 * @since 1.0
 */
public class DynamicDataSourceRoute extends AbstractRoutingDataSource {

	private final RouteLookup lookupKey;

	public DynamicDataSourceRoute(RouteLookup lookupKey) {
		this.lookupKey = lookupKey;
	}

	@NonNull
	@Override
	protected Object resolveSpecifiedLookupKey(@NonNull Object lookupKey) {
		if (lookupKey instanceof String) {
			return lookupKey;
		}

		throw new IllegalArgumentException("Invalid datasource key [" + lookupKey + "],must be string");
	}

	@Override
	protected Object determineCurrentLookupKey() {
		String key = lookupKey.get();
		if (key == null) {
			key = lookupKey.getDefaultRouteKey();
		}
		return key;
	}

}
