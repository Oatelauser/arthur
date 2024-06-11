package com.arthur.plugin.loadbalance.metrics;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.lang.Nullable;

import java.util.Map;
import java.util.function.Function;

/**
 * 服务实例指标
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2022-12-29
 * @since 1.0
 */
public interface ServiceInstanceMetrics<T> {

	/**
	 * 通过服务实例获取指标key
	 *
	 * @param serviceInstance 服务实例
	 * @return 指标key
	 */
	String getInstanceKey(ServiceInstance serviceInstance);

	/**
	 * 返回所有的指标
	 */
	Map<String, T> getAllMetrics();

	/**
	 * 通过指标key获取存储的指标信息对象
	 *
	 * @param instanceKey 服务key
	 * @return 指标信息对象
	 */
	T getMetrics(String instanceKey);

	/**
	 * 通过服务实例获取存储的指标信息对象
	 *
	 * @param serviceInstance 服务实例
	 * @return 指标信息对象
	 */
	@Nullable
	T getMetrics(ServiceInstance serviceInstance);

	/**
	 * 如果不存在指标的服务key，则设置指标，否则返回指标信息对象
	 *
	 * @param instanceKey 服务key
	 * @param metrics     获取指标信息对象的lambda方法
	 * @return 指标信息对象
	 */
	T computeIfAbsent(String instanceKey, Function<String, T> metrics);

}
