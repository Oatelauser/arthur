package com.arthur.plugin.loadbalance.core;

import com.alibaba.csp.sentinel.util.TimeUtil;
import com.arthur.plugin.loadbalance.utils.ServiceInstanceUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jctools.maps.NonBlockingHashMap;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 加权轮询负载均衡算法
 *
 * @author DearYang
 * @date 2022-07-17
 * @since 1.0
 */
@SuppressWarnings({ "rawtypes", "unused" })
public class WeightedRoundRobinLoadBalancer extends AbstractWeightedServiceInstanceLoadBalancer {

    private static final Log LOG = LogFactory.getLog(WeightedRoundRobinLoadBalancer.class);

    /**
     * 回收期
     */
    private static final int RECYCLE_PERIOD = 60_000;

    private final Map<String/* IP:PORT */, WeightedRoundRobin> serviceGroupWeights = new NonBlockingHashMap<>();

    public WeightedRoundRobinLoadBalancer(String serviceId,
            ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider) {
        super(serviceId, serviceInstanceListSupplierProvider);
    }

    @Override
    public ServiceInstance selectInstance(Request request, List<ServiceInstance> instances) {
        int totalWeight = 0;
        long maxCurrent = Long.MIN_VALUE;
        long now = TimeUtil.currentTimeMillis();
        ServiceInstance selectedInstance = null;
        WeightedRoundRobin selectWeightRoundRobin = null;

        for (ServiceInstance serviceInstance : instances) {
            int weight = this.getWeight(request, serviceInstance);
            String instanceKey = ServiceInstanceUtils.getServiceInstanceKey(serviceInstance);
            WeightedRoundRobin weightedRoundRobin = serviceGroupWeights.computeIfAbsent(instanceKey,
                    k -> new WeightedRoundRobin(weight));

            // 不相等说明权重发生变化
            if (weight != weightedRoundRobin.weight) {
                weightedRoundRobin.setWeight(weight);
            }

            long current = weightedRoundRobin.increaseCurrent();
            weightedRoundRobin.lastUpdate = now;
            if (current > maxCurrent) {
                maxCurrent = current;
                selectedInstance = serviceInstance;
                selectWeightRoundRobin = weightedRoundRobin;
            }

            totalWeight += weight;
        }

        // 服务实例发生变化
        if (instances.size() != serviceGroupWeights.size()) {
            serviceGroupWeights.entrySet().removeIf(item -> now - item.getValue().lastUpdate > RECYCLE_PERIOD);
        }

        if (selectedInstance != null) {
            selectWeightRoundRobin.sel(totalWeight);
            return selectedInstance;
        }

        return instances.get(0);
    }

    private final static class WeightedRoundRobin {
        private int weight;
        private long lastUpdate;
        private final AtomicLong current = new AtomicLong(0);

        private WeightedRoundRobin(int weight) {
            this.setWeight(weight);
        }

        void setWeight(int weight) {
            this.weight = weight;
            current.set(0);
        }

        long increaseCurrent() {
            return current.addAndGet(weight);
        }

        void sel(int total) {
            current.addAndGet(-1L * total);
        }

    }

}
