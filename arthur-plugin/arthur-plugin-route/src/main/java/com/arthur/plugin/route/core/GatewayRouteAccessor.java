package com.arthur.plugin.route.core;

import org.springframework.cloud.gateway.route.RouteDefinition;

import java.util.Map;

/**
 * 网关路由属性新增编辑器
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2022-12-30
 * @since 1.0
 */
public interface GatewayRouteAccessor {

	/**
	 * 获取{@link GatewayRouteDefinition}新增的属性名
	 */
	String attributeName();

	/**
	 * @see #getMetadataKey(String)
	 */
	default String getMetadataKey() {
		return GatewayRouteAccessor.getMetadataKey(this.attributeName());
	}

	/**
	 * 编辑路由的元数据信息
	 *
	 * @param value    设置元数据的值
	 * @param metadata 元数据Map
	 */
	default void writeTo(Object value, Map<String, Object> metadata) {
		String key = this.getMetadataKey();
		if (value != null && !metadata.containsKey(key)) {
			metadata.put(key, value);
		}
	}

	/**
	 * 获取存储在{@link RouteDefinition#getMetadata()}中的key
	 *
	 * @param attr {@link GatewayRouteDefinition}新增的属性名
	 * @return 路由元数据的key
	 */
	static String getMetadataKey(String attr) {
		return "__" + attr + "__";
	}

	/**
	 * 获取存储在{@link RouteDefinition#getMetadata()}中的key
	 *
	 * @param attr {@link GatewayRouteDefinition}新增的属性名
	 * @return 路由元数据的key
	 */
	static String getMetadataArgsKey(String attr) {
		return "__arg-" + attr + "__";
	}

}
