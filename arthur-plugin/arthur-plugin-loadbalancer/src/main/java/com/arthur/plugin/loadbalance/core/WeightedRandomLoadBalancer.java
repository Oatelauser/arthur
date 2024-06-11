package com.arthur.plugin.loadbalance.core;

import com.arthur.plugin.loadbalance.context.ServiceRequestContext;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.random.RandomGenerator;

/**
 * 加权随机负载均衡算法
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-01-10
 * @since 1.0
 */
@SuppressWarnings("rawtypes")
public class WeightedRandomLoadBalancer extends AbstractWeightedServiceInstanceLoadBalancer {

	public WeightedRandomLoadBalancer(String serviceId, ObjectProvider<ServiceInstanceListSupplier> serviceProvider) {
		super(serviceId, serviceProvider);
	}

	@Override
	protected ServiceInstance selectInstance(Request request, List<ServiceInstance> instances) {
		int size = instances.size();
		Object context = request.getContext();
		RandomGenerator random = ThreadLocalRandom.current();
		if (!(context instanceof ServiceRequestContext<?>)) {
			return instances.get(random.nextInt(size));
		}

		boolean sameWeight = true;
		int[] weights = new int[size];
		int totalWeight = 0;
		for (int i = 0; i < size; i++) {
			int weight = this.getWeight(request, instances.get(i));
			totalWeight += weight;
			weights[i] = weight;
			if (sameWeight && totalWeight != weight * (i + 1)) {
				sameWeight = false;
			}
		}

		// 实例权重不一样，加权分配
		if (totalWeight > 0 && !sameWeight) {
			int offset = random.nextInt(totalWeight);
			for (int i = 0; i < size; i++) {
				if (offset < weights[i]) {
					return instances.get(i);
				}
			}
		}

		return instances.get(random.nextInt(size));
	}

}
