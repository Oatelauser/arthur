package com.arthur.plugin.loadbalance.core;

import com.alibaba.csp.sentinel.util.TimeUtil;
import com.arthur.plugin.loadbalance.context.ServiceRequestContext;
import com.arthur.plugin.loadbalance.data.WeightRequestData;
import com.arthur.plugin.loadbalance.support.WeightedLoadBalanceSupport;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;

import java.util.Optional;

import static com.arthur.plugin.loadbalance.constant.LoadBalancerConstants.*;

/**
 * 具有权重预热的负载均衡算法抽象接口
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-01-05
 * @since 1.0
 */
@SuppressWarnings("rawtypes")
public abstract class AbstractWeightedServiceInstanceLoadBalancer
        extends AbstractServiceInstanceLoadBalancer implements WeightedLoadBalanceSupport {

    public AbstractWeightedServiceInstanceLoadBalancer(String serviceId,
            ObjectProvider<ServiceInstanceListSupplier> serviceProvider) {
        super(serviceId, serviceProvider);
    }

    @Override
    @SuppressWarnings("unchecked")
    public int getWeight(Request request, ServiceInstance serviceInstance) {
        if (request.getContext() instanceof ServiceRequestContext) {
            ServiceRequestContext<ServiceInstance> requestContext = (ServiceRequestContext<ServiceInstance>) request.getContext();
            Optional.ofNullable(requestContext.getTransformer()).ifPresent(transformer ->
                    transformer.transformServiceInstance(serviceInstance, requestContext));
            WeightRequestData requestData = (WeightRequestData) requestContext.getClientRequest();
            return this.getWeight(requestData.getStartupTime(), requestData.getWarmup(), requestData.getWeight());
        }

        return this.getWeight(DEFAULT_STARTUP_TIME, DEFAULT_WARMUP, DEFAULT_WEIGHT);
    }

    private int getWeight(long timestamp, int warmup, int weight) {
        if (weight > 0 && timestamp > 0) {
            long uptime = TimeUtil.currentTimeMillis() - timestamp;
            if (uptime < 0) {
                return 1;
            }

            if (uptime > 0 && uptime < warmup) {
                int warmupWeight = (int) ((float) uptime / ((float) warmup / (float) weight));
                return warmupWeight < 1 ? 1 : (Math.min(warmupWeight, weight));
            }
        }

        return Math.max(weight, 0);
    }

}
