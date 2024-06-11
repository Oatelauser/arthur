package com.arthur.plugin.loadbalance.metrics;

/**
 * 具有时效性的指标适配器
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2022-12-29
 * @since 1.0
 */
public interface TimelineMetrics {

	/**
	 * 是否上锁，防止清理过期指标的时候误删
	 *
	 * @return true-上锁
	 */
	boolean isLock();

	/**
	 * 更新最近一次的更新时间
	 *
	 * @param lastUpdateTime 更新时间
	 */
	void updateLastUpdateTime(long lastUpdateTime);

	/**
	 * 最近一次的更新时间
	 *
	 * @return 更新时间
	 */
	long getLastUpdateTime();

}
