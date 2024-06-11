package com.arthur.plugin.loadbalance.failover.function;

/**
 * 权重控制的回调函数，定制权重增减算法，或者定制健康检查的时机，就需要使用该接口
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2022-12-29
 * @since 1.0
 */
public interface WeightFunction<T> {

	/**
	 * 一个资源访问成功后，会调用该方法，重新计算权重
	 *
	 * @param minWeight     资源的最小权重
	 * @param maxWeight     资源的最大权重
	 * @param priority      资源的优先级
	 * @param currentWeight 资源的当前权重（计算前）
	 * @param resource      资源
	 * @return 重新计算后的权重
	 */
	double success(double minWeight, double maxWeight, int priority, double currentWeight, T resource);

	/**
	 * 一个资源访问失败后，会调用该方法，重新计算权重
	 *
	 * @param minWeight     资源的最小权重
	 * @param maxWeight     资源的最大权重
	 * @param priority      资源的优先级
	 * @param currentWeight 资源的当前权重（计算前）
	 * @param resource      资源
	 * @return 重新计算后的权重
	 */
	double fail(double minWeight, double maxWeight, int priority, double currentWeight, T resource);

	/**
	 * 控制一个资源是否需要健康检查，健康检查程序运行时，针对每个资源，逐个调用该方法
	 *
	 * @param minWeight     资源的最小权重
	 * @param maxWeight     资源的最大权重
	 * @param priority      资源的优先级
	 * @param currentWeight 资源的当前权重（计算前）
	 * @param resource      资源
	 * @return 是否需要健康检查，true-对相关资源进行健康检查
	 */
	boolean healthCheck(double minWeight, double maxWeight, int priority, double currentWeight, T resource);

}
