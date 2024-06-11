package com.arthur.plugin.loadbalance.failover.support;

import java.util.Map;
import java.util.Objects;

/**
 * 概要描述
 * <p>
 * 详细描述
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-01-10
 * @since 1.0
 */
public class WeightFailover<T> {

    private final WeightAllocFailover.WeightAllocator<T> allocator;

    public WeightFailover(WeightAllocFailover.WeightAllocator<T> allocator) {
        this.allocator = Objects.requireNonNull(allocator, "allocator");

        int totalResources = allocator.getWeightResources().size();
        for (Map.Entry<T, WeightAllocFailover.WeightResource> entry : allocator.getWeightResources().entrySet()) {

        }
    }

}
