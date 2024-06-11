package com.arthur.plugin.loadbalance.core;

import com.alibaba.csp.sentinel.node.StatisticNode;
import com.alibaba.csp.sentinel.util.TimeUtil;
import com.arthur.plugin.loadbalance.metrics.ClusterNodeAdapter;
import com.arthur.plugin.loadbalance.metrics.ServiceInstanceMetrics;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 最小响应时间负载均衡算法
 *
 * @author DearYang
 * @date 2022-08-05
 * @since 1.0
 */
@SuppressWarnings({"unused","rawtypes"})
public class ShortestResponseLoadBalancer extends AbstractWeightedServiceInstanceLoadBalancer {

	private int slidePeriod = 30_000;
	private final ServiceInstanceMetrics<ClusterNodeAdapter> metrics;
	private volatile long lastUpdateTime = System.currentTimeMillis();

	public ShortestResponseLoadBalancer(String serviceId,
			ServiceInstanceMetrics<ClusterNodeAdapter> metrics,
			ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider) {
		super(serviceId, serviceInstanceListSupplierProvider);
		this.metrics = metrics;
	}

	@Override
	protected ServiceInstance selectInstance(Request request, List<ServiceInstance> instances) {
		int size = instances.size();
		double shortestResponse = Double.MAX_VALUE;
		int shortestCount = 0;
		int[] shortestIndexes = new int[size];
		int[] weights = new int[size];
		int totalWeight = 0;
		int firstWeight = 0;
		boolean sameWeight = true;

		for (int i = 0; i < size; i++) {
			ServiceInstance serviceInstance = instances.get(i);
			double estimateResponse = this.getShortestResponse(request, serviceInstance);
			int afterWarmup = this.getWeight(request, serviceInstance);
			weights[i] = afterWarmup;
			if (estimateResponse < shortestResponse) {
				shortestResponse = estimateResponse;
				shortestCount = 1;
				shortestIndexes[0] = i;
				totalWeight = afterWarmup;
				firstWeight = afterWarmup;
				sameWeight = true;
			} else if (estimateResponse == shortestResponse) {
				shortestIndexes[shortestCount++] = i;
				totalWeight += afterWarmup;
				if (sameWeight && i > 0 && afterWarmup != firstWeight) {
					sameWeight = false;
				}
			}
		}

		long currentTimeMillis = TimeUtil.currentTimeMillis();
		if (currentTimeMillis - lastUpdateTime > slidePeriod) {
			lastUpdateTime = currentTimeMillis;
		}

		if (shortestCount == 1) {
			return instances.get(0);
		}
		if (!sameWeight && totalWeight > 0) {
			int offsetWeight = ThreadLocalRandom.current().nextInt(totalWeight);
			for (int i = 0; i < offsetWeight; i++) {
				int shortestIndex = shortestIndexes[i];
				offsetWeight = weights[shortestIndex];
				if (offsetWeight < 0) {
					return instances.get(shortestIndex);
				}
			}
		}

		return instances.get(shortestIndexes[ThreadLocalRandom.current().nextInt(shortestCount)]);
	}

	public void setSlidePeriod(int slidePeriod) {
		this.slidePeriod = slidePeriod;
	}

	private double getShortestResponse(Request request, ServiceInstance serviceInstance) {
		StatisticNode statisticNode = metrics.getMetrics(serviceInstance);
		return statisticNode == null ? 0 : statisticNode.avgRt();
	}

}
