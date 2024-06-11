package com.arthur.plugin.loadbalance.metrics;

import com.arthur.common.concurrent.thread.ExecutorServices;
import com.arthur.common.concurrent.thread.NamedThreadFactory;
import com.arthur.common.lifecycle.InitializeListener;
import com.arthur.common.lifecycle.ShutdownHook;
import com.arthur.plugin.loadbalance.utils.ServiceInstanceUtils;
import org.jctools.maps.NonBlockingHashMap;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.util.CollectionUtils;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * 服务指标接口的适配器
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2022-12-29
 * @since 1.0
 */
public class ServiceInstanceMetricsAdapter<T extends TimelineMetrics> implements InitializeListener, ShutdownHook, ServiceInstanceMetrics<T> {

	private long evictTimeInMs = 1000 * 60;
	private ScheduledExecutorService scheduledExecutor;
	protected final ConcurrentMap<String, T> clusterNodes = new NonBlockingHashMap<>();

	@Override
	public String getInstanceKey(ServiceInstance serviceInstance) {
		return ServiceInstanceUtils.getServiceInstanceKey(serviceInstance);
	}

	@Override
	public ConcurrentMap<String, T> getAllMetrics() {
		return this.clusterNodes;
	}

	@Override
	public T getMetrics(String instanceKey) {
		return this.clusterNodes.get(instanceKey);
	}

	@Override
	public T getMetrics(ServiceInstance serviceInstance) {
		return this.getMetrics(this.getInstanceKey(serviceInstance));
	}

	@Override
	public T computeIfAbsent(String instanceKey, Function<String, T> metrics) {
		return clusterNodes.computeIfAbsent(instanceKey, metrics);
	}

	@Override
	public void start() {
		this.scheduledExecutor = Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory("LB-METRICS-CLEANER"));
		this.scheduledExecutor.scheduleAtFixedRate(this::cleanup, 1, 1, TimeUnit.MINUTES);
	}

	@Override
	public void shutdown() {
		if (this.scheduledExecutor != null) {
			ExecutorServices.shutdown(this.scheduledExecutor);
		}
	}

	private void cleanup() {
		if (CollectionUtils.isEmpty(clusterNodes)) {
			return;
		}

		long currentTimeMillis = System.currentTimeMillis();
		for (Iterator<Map.Entry<String, T>> iterator = clusterNodes.entrySet().iterator(); iterator.hasNext(); ) {
			Map.Entry<String, T> entry = iterator.next();
			T clusterNode = entry.getValue();
			if (!clusterNode.isLock() && (currentTimeMillis - clusterNode.getLastUpdateTime()) > this.evictTimeInMs) {
				iterator.remove();
			}
		}
	}

	public void setEvictTimeInMs(long evictTimeInMs) {
		this.evictTimeInMs = evictTimeInMs;
	}

}
