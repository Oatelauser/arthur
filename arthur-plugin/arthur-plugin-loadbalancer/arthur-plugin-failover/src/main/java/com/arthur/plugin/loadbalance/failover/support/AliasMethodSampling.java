package com.arthur.plugin.loadbalance.failover.support;

import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.random.RandomGenerator;

/**
 * <a href="http://www.keithschwarz.com/darts-dice-coins/">离散随机采样算法</a>
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2022-12-30
 * @since 1.0
 */
public class AliasMethodSampling<T> {

	private final int[] alias;
	private final List<T> values;
	private final double[] probability;

	public AliasMethodSampling(Map<T, ? extends Number> weightMap) {
		Assert.isTrue(CollectionUtils.isEmpty(weightMap), "weightMap is empty");

		double sum = 0;
		List<T> values = new ArrayList<>(weightMap.size());
		for (Map.Entry<T, ? extends Number> entry : weightMap.entrySet()) {
			double weight = entry.getValue().doubleValue();
			if (weight > 0) {
				sum += weight;
				values.add(entry.getKey());
			}
		}
		Assert.isTrue(sum > 0, "invalid weight map: " + weightMap);
		this.values = values;

		List<Double> probabilities = new ArrayList<>(values.size());
		for (T key : values) {
			double weight = weightMap.get(key).doubleValue();
			probabilities.add(weight / sum);
		}

		int size = probabilities.size();
		this.probability = new double[size];
		this.alias = new int[size];

		this.disperse(size, probabilities);
	}

	private void disperse(int size, List<Double> probabilities) {
		double average = 1.0 / size;
		Deque<Integer> small = new ArrayDeque<>();
		Deque<Integer> large = new ArrayDeque<>();
		for (int i = 0; i < size; i++) {
			if (probabilities.get(i) >= average) {
				large.add(i);
			} else {
				small.add(i);
			}
		}

		while (!small.isEmpty() && !large.isEmpty()) {
			int less = small.removeLast();
			int more = large.removeLast();

			this.probability[less] = probabilities.get(less) * size;
			this.alias[less] = more;
			probabilities.set(more, probabilities.get(more) + probabilities.get(less) - average);

			if (probabilities.get(more) >= average) {
				large.add(more);
			} else {
				small.add(more);
			}
		}

		while (!small.isEmpty()) {
			this.probability[small.removeLast()] = 1.0;
		}
		while (!large.isEmpty()) {
			this.probability[large.removeLast()] = 1.0;
		}
	}

	public T get() {
		RandomGenerator random = ThreadLocalRandom.current();
		int column = random.nextInt(this.probability.length);
		boolean coinToss = random.nextDouble() < this.probability[column];
		return this.values.get(coinToss ? column : this.alias[column]);
	}

}
