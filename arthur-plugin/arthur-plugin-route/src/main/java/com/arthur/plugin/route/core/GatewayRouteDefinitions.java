package com.arthur.plugin.route.core;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 网关路由定义的工具类
 * <p>
 * 同步自定义实现的网关路由{@link GatewayRouteDefinition#getMetadata()}参数中
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2022-12-30
 * @since 1.0
 */
public class GatewayRouteDefinitions {

	private final List<GatewayRouteAccessor> attributes = new ArrayList<>(10);

	public GatewayRouteDefinitions(ObjectProvider<List<GatewayRouteAccessor>> provider) {
		Set<String> sourceClassFields = this.getClassFieldNames(RouteDefinition.class);
		Set<String> targetClassFields = this.getClassFieldNames(GatewayRouteDefinition.class);
		targetClassFields.removeAll(sourceClassFields);
		this.loadGatewayRouteAttributes(provider.getIfAvailable(), targetClassFields);
	}

	public void asyncRouteMetadata(GatewayRouteDefinition route) {
		Map<String, Object> metadata = route.getMetadata();
		BeanMap.Generator bean = new BeanMap.Generator();
		bean.setRequire(BeanMap.REQUIRE_GETTER + BeanMap.REQUIRE_SETTER);
		bean.setBean(route);
		BeanMap beanMap = bean.create();
		for (GatewayRouteAccessor attribute : this.attributes) {
			attribute.writeTo(beanMap.get(attribute.attributeName()), metadata);
		}
		route.setMetadata(metadata);
	}

	private void loadGatewayRouteAttributes(List<GatewayRouteAccessor> attributes, Set<String> classFields) {
		if (!CollectionUtils.isEmpty(attributes)) {
			for (GatewayRouteAccessor attribute : attributes) {
				if (classFields.remove(attribute.attributeName())) {
					this.attributes.add(attribute);
				}
			}
		}

		for (String field : classFields) {
			this.attributes.add(() -> field);
		}
	}

	/**
	 * 获取指定Class对象的成员变量，该成员变量必须有getter、setter方法
	 *
	 * @param clazz Class对象
	 * @return 有getter、setter方法的成员变量
	 */
	@SuppressWarnings("unchecked")
	private Set<String> getClassFieldNames(Class<?> clazz) {
		BeanMap.Generator bean = new BeanMap.Generator();
		bean.setRequire(BeanMap.REQUIRE_GETTER + BeanMap.REQUIRE_SETTER);
		bean.setBeanClass(clazz);
		BeanMap beanMap = bean.create();
		return new HashSet<String>(beanMap.keySet());
	}

}
