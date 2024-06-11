package com.arthur.plugin.loadbalance.failover.function;

import org.springframework.util.Assert;

/**
 * 等比递减加减权重，在失败的时候可以加速扣减权重
 * <p>
 * 假设初始权重为1，失败一次之后0.5，再一次0.25，连续失败7次down（当downThreshold=0.01的时候）
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2022-12-29
 * @since 1.0
 */
@SuppressWarnings("unused")
public class RatioWeightFunction<T> extends AbstractWeightFunction<T> {

	private static final double DEFAULT_RECOVER_RATE = 0.1;
	private static final double DEFAULT_DOWN_THRESHOLD_RATE = 0.01;
	private static final double DEFAULT_SUCCESS_INCREASE_THRESHOLD_RATE = 0.01;
	private static final double DEFAULT_FAIL_DECREASE_THRESHOLD_RATE = 0.5;

	/**
	 * 权重低于多少直接down（设置为最小权重），防止等比递减扣不到0
	 */
	private final double downThreshold;

	/**
	 * 失败后扣减权重的比例（相对于当前权重）
	 */
	private final double failDecrRateOfCurrentWeight;

	/**
	 * 成功后增加权重的比例（相对于最大权重）
	 */
	private final double successIncrRateOfMaxWeight;

	/**
	 * 以最大权重的比例作为权重低于多少直接down，防止等比递减扣不到0
	 *
	 * @see #downThreshold
	 */
	private double downThresholdRateOfMaxWeight = DEFAULT_DOWN_THRESHOLD_RATE;

	/**
	 * 以最大权重的比例作为恢复阈值
	 */
	private double recoverRateOfMaxWeight = DEFAULT_RECOVER_RATE;

	public RatioWeightFunction() {
		this(DEFAULT_FAIL_DECREASE_THRESHOLD_RATE, DEFAULT_SUCCESS_INCREASE_THRESHOLD_RATE);
	}

	public RatioWeightFunction(double failKeepRateOfCurrentWeight, double successIncrRateOfMaxWeight) {
		this(DEFAULT_RECOVER_THRESHOLD, failKeepRateOfCurrentWeight, successIncrRateOfMaxWeight);
	}

	public RatioWeightFunction(int recoverThreshold, double failKeepRateOfCurrentWeight, double successIncrRateOfMaxWeight) {
		this(recoverThreshold, 0, failKeepRateOfCurrentWeight, successIncrRateOfMaxWeight);
	}

	public RatioWeightFunction(int recoverThreshold, double downThreshold,
		   double failKeepRateOfCurrentWeight, double successIncrRateOfMaxWeight) {
		super(recoverThreshold);
		Assert.isTrue(downThreshold >= 0, "downThreshold >= 1");
		Assert.isTrue(failKeepRateOfCurrentWeight >= 0 && failKeepRateOfCurrentWeight <= 1
			, "0 <= failKeepRateOfCurrentWeight <= 1");
		Assert.isTrue(successIncrRateOfMaxWeight >= 0 && successIncrRateOfMaxWeight <= 1
			, "0 <= successIncrRateOfMaxWeight <= 1");
		this.downThreshold = downThreshold;
		this.failDecrRateOfCurrentWeight = failKeepRateOfCurrentWeight;
		this.successIncrRateOfMaxWeight = successIncrRateOfMaxWeight;
	}

	@Override
	protected double computeIfSuccess(double minWeight, double maxWeight, int priority, double currentWeight, T resource) {
		return currentWeight + maxWeight * currentWeight > minWeight ?
			this.successIncrRateOfMaxWeight : this.recoverRateOfMaxWeight;
	}

	@Override
	protected double computeIfFail(double minWeight, double maxWeight, int priority, double currentWeight, T resource) {
		double weight = currentWeight * this.failDecrRateOfCurrentWeight;
		if (downThreshold == 0) {
			return weight < maxWeight * this.downThresholdRateOfMaxWeight ? minWeight : weight;
		}
		return weight < downThreshold ? minWeight : weight;
	}

	public void downThresholdRateOfMaxWeight(double downThresholdRateOfMaxWeight) {
		this.downThresholdRateOfMaxWeight = downThresholdRateOfMaxWeight;
	}

	public void recoverRateOfMaxWeight(double recoverRateOfMaxWeight) {
		this.recoverRateOfMaxWeight = recoverRateOfMaxWeight;
	}

}
