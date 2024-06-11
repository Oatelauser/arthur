package com.arthur.plugin.loadbalance.support;

import org.springframework.lang.Nullable;

/**
 * 负债均衡注册器
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-01-03
 * @since 1.0
 */
public interface LoadBalanceLocator<T> {

	/**
	 * 通过载均衡的名称查找负载均衡对象
	 *
	 * @param lbName 均衡的名称
	 * @return 负载均衡对象
	 */
	@Nullable
	T getLoadBalancer(String lbName);

}
