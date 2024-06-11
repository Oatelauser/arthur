package com.arthur.plugin.loadbalance.failover.core;

import com.arthur.common.utils.StringUtils;
import com.arthur.plugin.loadbalance.core.AbstractWeightedServiceInstanceLoadBalancer;
import com.arthur.plugin.loadbalance.failover.support.AliasMethodSampling;
import com.arthur.plugin.loadbalance.utils.ServiceInstanceUtils;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自适应权重负载均衡算法
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-01-10
 * @since 1.0
 */
@SuppressWarnings("rawtypes")
public class WeightedFailoverLoadBalancer extends AbstractWeightedServiceInstanceLoadBalancer {

	private int aliasMethodThreshold = 10;

	private final Cache<String, WeightedFailoverResource> weightedResources =
		Caffeine.newBuilder()
			.softValues()
			.expireAfterAccess(Duration.ofMinutes(1))
			.build();

	public WeightedFailoverLoadBalancer(String serviceId, ObjectProvider<ServiceInstanceListSupplier> serviceProvider) {
		super(serviceId, serviceProvider);
	}

	@Override
	protected ServiceInstance selectInstance(Request request, List<ServiceInstance> instances) {
		String serviceId = this.getServiceId();
		int size = instances.size();
		WeightedFailoverResource serviceResource = weightedResources.get(serviceId,
			key -> new WeightedFailoverResource(size));

		int totalMaxWeight = 0;
		int totalCurrentWeight = 0;
		boolean maxWeightSame = true;
		int firstMaxWeight = 1;
		int[] currentWeights = new int[size];

		Map<Integer, Integer> sampling = new HashMap<>(size);
		for (int i = 0; i < size; i++) {
			ServiceInstance serviceInstance = instances.get(i);
			int minWeight = 1;
			int maxWeight = 1;
			int initWeight = 1;

			String instanceKey = ServiceInstanceUtils.getServiceInstanceKey(serviceInstance);
			sampling.put(i, maxWeight);
			totalCurrentWeight += initWeight;
			totalMaxWeight += maxWeight;
			currentWeights[i] = initWeight;
			if (firstMaxWeight != maxWeight) {
				maxWeightSame = false;
			}
		}

		if (maxWeightSame && totalMaxWeight == totalCurrentWeight && totalMaxWeight > 0) {
			int idx = serviceResource.getIndex().getAndIncrement() % instances.size();
			return instances.get(idx);
		}

		if (!maxWeightSame && instances.size() > aliasMethodThreshold) {
			AliasMethodSampling<Integer> aliasMethodSampling = new AliasMethodSampling<>(sampling);
			return instances.get(aliasMethodSampling.get());
		}

		if (totalCurrentWeight > 0) {
			int x = 0;
			int randomWeight = ThreadLocalRandom.current().nextInt(totalCurrentWeight);
			for (int j = 0; j < size; j++) {
				x += currentWeights[j];
				if (randomWeight < x) {
					return instances.get(j);
				}
			}
		}

		return ServiceInstanceUtils.random(instances);
	}

	private final static class WeightedFailoverResource {

		/**
		 * 当前权重
		 */
		private int currentWeight;

		private volatile AtomicInteger index;

		/**
		 * 0-minWeight，1-maxWeight，2-initWeight
		 */
		private final String[] serviceWeightInfos;

		private WeightedFailoverResource(int capacity) {
			this.serviceWeightInfos = new String[capacity];
		}

		private void addServiceInstanceWeight(int index, int minWeight, int maxWeight, int initWeight) {
			this.serviceWeightInfos[index] = minWeight + "," + maxWeight + "," + initWeight;
			this.currentWeight = initWeight;
		}


		/**
		 * 0-minWeight，1-maxWeight，2-initWeight
		 */
		public int[] getWeights(int index) {
			return StringUtils.split(serviceWeightInfos[index], ",")
				.stream().mapToInt(Integer::parseInt)
				.toArray();
		}

		public AtomicInteger getIndex() {
			if (index == null) {
				synchronized (this) {
					if (index == null) {
						index = new AtomicInteger(0);
					}
				}
			}
			return index;
		}
	}

}
