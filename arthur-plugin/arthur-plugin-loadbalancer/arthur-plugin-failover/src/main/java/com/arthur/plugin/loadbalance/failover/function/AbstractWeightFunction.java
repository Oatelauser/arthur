package com.arthur.plugin.loadbalance.failover.function;

import org.jctools.maps.NonBlockingHashMap;
import org.springframework.util.Assert;

import java.util.concurrent.ConcurrentMap;

/**
 * 提供{@link WeightFunction}抽象父类实现通用的接口方法
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2022-12-29
 * @since 1.0
 */
public abstract class AbstractWeightFunction<T> implements WeightFunction<T> {

	public static final int DEFAULT_RECOVER_THRESHOLD = 1;

	/**
	 * 探活的时候，几次探活成功开始增加权重
	 */
	private final int recoverThreshold;

	/**
	 * 探活成功次数的记录
	 */
	private final ConcurrentMap<T, Integer> recoverCountMap;

	public AbstractWeightFunction() {
		this(DEFAULT_RECOVER_THRESHOLD);
	}

	public AbstractWeightFunction(int recoverThreshold) {
		Assert.isTrue(recoverThreshold >= 1, "recoverThreshold[" + recoverThreshold + "] >= 1");
		this.recoverThreshold = recoverThreshold;
		this.recoverCountMap = recoverThreshold > 1 ? new NonBlockingHashMap<>() : null;
	}

	@Override
	public double success(double minWeight, double maxWeight, int priority, double currentWeight, T resource) {
		if (recoverCountMap != null && currentWeight <= minWeight) {
			int count = recoverCountMap.compute(resource, (k, v) -> v == null ? 1 : v + 1);
			if (count < this.recoverThreshold) {
				return currentWeight;
			}
		}

		double weight = this.computeIfSuccess(minWeight, maxWeight, priority, currentWeight, resource);
		if (recoverCountMap != null && currentWeight <= minWeight && weight > maxWeight) {
			recoverCountMap.remove(resource);
		}
		return Math.min(weight, maxWeight);
	}

	@Override
	public double fail(double minWeight, double maxWeight, int priority, double currentWeight, T resource) {
		double weight = this.computeIfFail(minWeight, maxWeight, priority, currentWeight, resource);
		if (recoverCountMap != null && weight <= minWeight) {
			recoverCountMap.put(resource, 0);
		}
		return Math.max(weight, minWeight);
	}

	@Override
	public boolean healthCheck(double minWeight, double maxWeight, int priority, double currentWeight, T resource) {
		return currentWeight <= minWeight && maxWeight > 0;
	}

	/**
	 * 计算成功之后的权重
	 *
	 * @see #success(double, double, int, double, Object)
	 */
	protected abstract double computeIfSuccess(double minWeight, double maxWeight, int priority, double currentWeight, T resource);

	/**
	 * 计算失败之后的权重
	 *
	 * @see #fail(double, double, int, double, Object)
	 */
	protected abstract double computeIfFail(double minWeight, double maxWeight, int priority, double currentWeight, T resource);

}
