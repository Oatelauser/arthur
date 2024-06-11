package com.arthur.plugin.loadbalance.metrics;

import com.alibaba.csp.sentinel.node.ClusterNode;

/**
 * 统计指标{@link ClusterNode}
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2022-12-29
 * @since 1.0
 */
public class ClusterNodeAdapter extends ClusterNode implements TimelineMetrics {

	volatile long lastUpdateTime;
	volatile boolean lock = false;

	public ClusterNodeAdapter(String name, int resourceType) {
		super(name, resourceType);
		this.lastUpdateTime = System.currentTimeMillis();
	}

	@Override
	public boolean isLock() {
		return this.lock;
	}

	@Override
	public void updateLastUpdateTime(long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	@Override
	public long getLastUpdateTime() {
		return this.lastUpdateTime;
	}

}
