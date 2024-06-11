package com.arthur.plugin.loadbalance.core;

import com.alibaba.csp.sentinel.node.StatisticNode;
import com.arthur.plugin.loadbalance.metrics.ClusterNodeAdapter;
import com.arthur.plugin.loadbalance.metrics.ServiceInstanceMetrics;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 最小活跃数负载均衡算法
 *
 * @author DearYang
 * @date 2022-08-05
 * @since 1.0
 */
@SuppressWarnings({"rawtypes", "unused"})
public class LeastActiveLoadBalancer extends AbstractWeightedServiceInstanceLoadBalancer {

	private final ServiceInstanceMetrics<ClusterNodeAdapter> metrics;

	public LeastActiveLoadBalancer(String serviceId,
		   ServiceInstanceMetrics<ClusterNodeAdapter> metrics,
		   ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider) {
		super(serviceId, serviceInstanceListSupplierProvider);
		this.metrics = metrics;
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected ServiceInstance selectInstance(Request request, List<ServiceInstance> instances) {
		int leastCount = 0;
		int totalWeight = 0;
		int firstWeight = 0;
		long leastActive = -1;
		boolean sameWeight = true;
		int size = instances.size();
		int[] weights = new int[size];
		int[] leastIndexes = new int[size];

		for (int i = 0; i < size; i++) {
			ServiceInstance serviceInstance = instances.get(i);
			long active = this.getActive(request, serviceInstance);
			int currentWeight = this.getWeight(request, serviceInstance);
			weights[i] = currentWeight;
			if (leastActive == -1 || active < leastActive) {
				leastActive = active;
				leastCount = 1;
				leastIndexes[0] = i;
				totalWeight = currentWeight;
				firstWeight = currentWeight;
				sameWeight = true;
			} else if (active == leastActive) {
				leastIndexes[leastCount++] = i;
				totalWeight += currentWeight;
				if (sameWeight && currentWeight != firstWeight) {
					sameWeight = false;
				}
			}
		}

		if (leastCount == 1) {
			return instances.get(leastIndexes[0]);
		}

		if (!sameWeight && totalWeight > 0) {
			int offsetWeight = ThreadLocalRandom.current().nextInt(totalWeight);
			for (int i = 0; i < leastCount; i++) {
				int leastIndex = leastIndexes[i];
				offsetWeight -= weights[leastIndex];
				if (offsetWeight < 0) {
					return instances.get(leastIndex);
				}
			}
		}

		return instances.get(leastIndexes[ThreadLocalRandom.current().nextInt(leastCount)]);
	}

	/**
	 * 获取当前服务实例的请求活跃数
	 *
	 * @param request         请求
	 * @param serviceInstance 服务实例
	 * @return 请求活跃数
	 */
	private long getActive(Request request, ServiceInstance serviceInstance) {
		StatisticNode statisticNode = this.metrics.getMetrics(serviceInstance);
		return statisticNode == null ? 0 : statisticNode.curThreadNum();
	}

}
