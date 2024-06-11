package com.arthur.plugin.loadbalance.failover.support;

import com.arthur.plugin.loadbalance.constant.LoadBalancerConstants;
import com.arthur.plugin.loadbalance.failover.function.WeightFunction;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 权重分配配置类
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-01-12
 * @since 1.0
 */
@SuppressWarnings({ "unused", "unchecked" })
public class WeightAllocFailover<T> {

	private final WeightAllocator<T> weightAllocator = new WeightAllocator<>();

	public WeightAllocFailover<T> name(String name) {
		this.weightAllocator.name = name;
		return this;
	}

	public WeightAllocFailover<T> priorityFactor(double factor) {
		this.weightAllocator.factor = factor;
		return this;
	}

	public WeightAllocFailover<T> weightFunction(WeightFunction<T> weightFunction) {
		this.weightAllocator.weightFunction = weightFunction;
		return this;
	}

	public WeightAllocFailover<T> addResource(T resource) {
		return this.addResource(resource, LoadBalancerConstants.DEFAULT_MAX_WEIGHT);
	}

	public WeightAllocFailover<T> addResource(T resource, double maxWeight) {
		return this.addResource(resource, LoadBalancerConstants.DEFAULT_MIN_WEIGHT, maxWeight);
	}

	public WeightAllocFailover<T> addResource(T resource, double minWeight, double maxWeight) {
		return this.addResource(resource, LoadBalancerConstants.DEFAULT_PRIORITY, minWeight, maxWeight);
	}

	public WeightAllocFailover<T> addResource(T resource, int priority, double minWeight, double maxWeight) {
		return this.addResource(resource, priority, maxWeight, minWeight, maxWeight);
	}

	public WeightAllocFailover<T> addResource(T resource, int priority, double initWeight, double minWeight, double maxWeight) {
		Assert.isTrue(resource != null, "resource");
		Assert.isTrue(minWeight >= 0, "minWeight less than zero");
		Assert.isTrue(maxWeight >= 0, "maxWeight less than zero");
		Assert.isTrue(maxWeight >= minWeight, "maxWeight < minWeight");
		Assert.isTrue(initWeight >= minWeight && initWeight <= maxWeight,
			"initWeight rule: minWeight <= initWeight <= maxWeight");
		WeightResource weightResource = WeightResource.createResource(priority, initWeight, minWeight, maxWeight);
		weightAllocator.addWeightResource(resource, weightResource);
		return this;
	}

	/**
	 * 权重资源
	 *
	 * @param priority   优先级
	 * @param initWeight 初始化权重
	 * @param minWeight  最小权重
	 * @param maxWeight  最大权重
	 */
	@SuppressWarnings("SameParameterValue")
	protected record WeightResource(int priority, double initWeight, double minWeight, double maxWeight)
		implements Cloneable {

		static WeightResource createResource() {
			return createResource(LoadBalancerConstants.DEFAULT_MAX_WEIGHT);
		}

		static WeightResource createResource(double maxWeight) {
			return createResource(LoadBalancerConstants.DEFAULT_MIN_WEIGHT, maxWeight);
		}

		static WeightResource createResource(double minWeight, double maxWeight) {
			return createResource(LoadBalancerConstants.DEFAULT_PRIORITY, minWeight, maxWeight);
		}

		static WeightResource createResource(int priority, double minWeight, double maxWeight) {
			return new WeightResource(priority, maxWeight, minWeight, maxWeight);
		}

		static WeightResource createResource(int priority, double initWeight, double minWeight, double maxWeight) {
			return new WeightResource(priority, initWeight, minWeight, maxWeight);
		}

		@Nullable
		@Override
		protected WeightResource clone() {
			try {
				return (WeightResource) super.clone();
			} catch (CloneNotSupportedException e) {
				return null;
			}
		}

	}

	static class WeightAllocator<T> implements Cloneable {

		private String name;
		private double factor = 1.4d;
		private int aliasMethodThreshold = 10;
		private WeightFunction<T> weightFunction;
		private Map<T, WeightResource> weightResources = new HashMap<>();

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public double getFactor() {
			return factor;
		}

		public void setFactor(double factor) {
			this.factor = factor;
		}

		public int getAliasMethodThreshold() {
			return aliasMethodThreshold;
		}

		public void setAliasMethodThreshold(int aliasMethodThreshold) {
			this.aliasMethodThreshold = aliasMethodThreshold;
		}

		public WeightFunction<T> getWeightFunction() {
			return weightFunction;
		}

		public void setWeightFunction(WeightFunction<T> weightFunction) {
			this.weightFunction = weightFunction;
		}

		public Map<T, WeightResource> getWeightResources() {
			return weightResources;
		}

		public void addWeightResource(T resource, WeightResource weightResource) {
			this.weightResources.put(resource, weightResource);
		}

		@Nullable
		@Override
		public Object clone() {
			try {
				WeightAllocator<T> allocBuilder = (WeightAllocator<T>) super.clone();
				if (!CollectionUtils.isEmpty(weightResources)) {
					allocBuilder.weightResources = new HashMap<>(this.weightResources);
				}
				return allocBuilder;
			} catch (CloneNotSupportedException e) {
				return null;
			}
		}
	}

}
