package com.arthur.plugin.loadbalance.core;

import com.arthur.boot.utils.NameUtils;
import com.arthur.plugin.loadbalance.support.LoadBalanceLocator;

/**
 * 命名负载均衡算法
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-01-03
 * @see AbstractServiceInstanceLoadBalancer
 * @see LoadBalanceLocator
 * @since 1.0
 */
public interface NamedLoadBalancer {

	/**
	 * 通过传入的类名解析负载均衡算法名称
	 *
	 * @param className 类名
	 * @return 负载均衡算法名称
	 */
	static String resolveName(String className) {
		return NameUtils.normalizeClassName("LoadBalancer", className);
	}

	/**
	 * @see #resolveName(String)
	 */
	static String resolveName(Class<?> clazz) {
		return NamedLoadBalancer.resolveName(clazz.getSimpleName());
	}

	/**
	 * 负载均衡算法名称
	 */
	default String name() {
		return NamedLoadBalancer.resolveName(this.getClass().getSimpleName());
	}

	/**
	 * 是否注册到{@link LoadBalanceLocator}
	 *
	 * @return true-注册
	 */
	default boolean supportsRegistry() {
		return true;
	}

}
