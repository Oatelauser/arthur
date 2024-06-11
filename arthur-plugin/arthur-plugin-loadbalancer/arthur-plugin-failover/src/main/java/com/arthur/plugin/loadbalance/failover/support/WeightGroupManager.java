package com.arthur.plugin.loadbalance.failover.support;


import org.springframework.util.Assert;

import java.util.*;

/**
 * 概要描述
 * <p>
 * 详细描述
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-01-12
 * @since 1.0
 */
public class WeightGroupManager<T> {

    private final int totalCoreGroups;
    private volatile List<T> coreSources;
    private volatile List<T> remainingResources;


    public WeightGroupManager(Set<T> resources, int... coreGroupSize) {
        Assert.notEmpty(resources, "resources is empty");

        int totalCoreGroups = 0;
        for (int size : coreGroupSize) {
            Assert.isTrue(size >= 0, "invalid coreGroupSize: " + size);
            totalCoreGroups += size;
        }
        this.totalCoreGroups = totalCoreGroups;

        List<T> totalSources = new ArrayList<>(resources);
        Collections.shuffle(totalSources);
        int size = totalSources.size();
        if (totalCoreGroups < size) {
            this.coreSources = totalSources.subList(0, totalCoreGroups);
            this.remainingResources = totalSources.subList(totalCoreGroups, size);
        } else {
            this.coreSources = totalSources;
            this.remainingResources = Collections.emptyList();
        }

    }

    static <T> Map<T, int[]> getRemainingResources(List<T> coreResources, List<T> remainingResources,
            int[] coreGroupSizes) {
        int totalGroupCount = coreGroupSizes.length;
        Map<T, int[]> remainingResourceMap = new HashMap<>();

        int index = 0;
        for (int i = 0, priority = 0, groupIndex = 0; i < coreResources.size(); i++, index++, groupIndex++) {
            while (groupIndex >= coreGroupSizes[priority]) {
                priority++;
                groupIndex = 0;
            }
            remainingResourceMap.put(coreResources.get(i), new int[]{ priority, index });
        }

        for (T remainingResource : remainingResources) {
            remainingResourceMap.put(remainingResource, new int[]{ totalGroupCount, index });
            index++;
        }

        return remainingResourceMap;
    }

}
