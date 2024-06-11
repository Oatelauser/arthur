package com.arthur.plugin.loadbalance.failover.function;

/**
 * 简单实现{@link AbstractWeightFunction}的权重加减
 * <p>
 * 1.成功权重 = 当前权重 + 最大权重 * 成功权重增加率
 * <br/>
 * 2.失败权重 = 当前权重 - 最大权重 * 失败权重减少率
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2022-12-29
 * @since 1.0
 */
public class SimpleWeightFunction<T> extends AbstractWeightFunction<T> {

	private static final double DEFAULT_FAIL_DECREASE_RATE = 0.05;
	private static final double DEFAULT_SUCCESS_INCREASE_RATE = 0.05;

	/**
	 * 失败权重下降率
	 */
	private final double failDecrRate;

	/**
	 * 成功权重上升率
	 */
	private final double successIncrRate;

	public SimpleWeightFunction() {
		this(DEFAULT_FAIL_DECREASE_RATE, DEFAULT_SUCCESS_INCREASE_RATE);
	}

	public SimpleWeightFunction(double failDecrRate, double successIncrRate) {
		this(DEFAULT_RECOVER_THRESHOLD, failDecrRate, successIncrRate);
	}

	public SimpleWeightFunction(int recoverThreshold, double failDecrRate, double successIncrRate) {
		super(recoverThreshold);
		if (failDecrRate < 0 || failDecrRate > 1) {
			throw new IllegalArgumentException("0 <= failDecrRate <= 1");
		}
		if (successIncrRate < 0 || successIncrRate > 1) {
			throw new IllegalArgumentException("0 <= successIncrRate <= 1");
		}
		this.failDecrRate = failDecrRate;
		this.successIncrRate = successIncrRate;
	}

	@Override
	protected double computeIfSuccess(double minWeight, double maxWeight, int priority, double currentWeight, T resource) {
		return currentWeight + maxWeight * this.successIncrRate;
	}

	@Override
	protected double computeIfFail(double minWeight, double maxWeight, int priority, double currentWeight, T resource) {
		return currentWeight - maxWeight * this.failDecrRate;
	}

}
