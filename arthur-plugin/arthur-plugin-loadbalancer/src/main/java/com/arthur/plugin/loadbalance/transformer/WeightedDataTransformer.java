package com.arthur.plugin.loadbalance.transformer;

import com.arthur.plugin.loadbalance.constant.LoadBalancerConstants;
import com.arthur.plugin.loadbalance.context.ServiceRequestContext;
import com.arthur.plugin.loadbalance.data.WeightRequestData;
import com.arthur.plugin.loadbalance.support.WeightedLoadBalanceSupport;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.RequestData;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;

import java.util.Map;

/**
 * 处理实现{@link WeightedLoadBalanceSupport}负载均衡算法的数据
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-01-06
 * @since 1.0
 */
@SuppressWarnings("SpellCheckingInspection")
public class WeightedDataTransformer implements LoadBalancerDataTransformer<ServiceInstance>, EnvironmentAware {

    private static volatile String WEIGHT_ATTR_NAME;

    @Override
    public boolean supports(ReactorLoadBalancer<ServiceInstance> lb) {
        return lb instanceof WeightedLoadBalanceSupport;
    }

    @Override
    public RequestData transformRequestData(String serviceId, Route route, ServerWebExchange exchange) {
        WeightRequestData requestData = new WeightRequestData(exchange.getRequest());
        Map<String, Object> metadata = route.getMetadata();
        Object weight = metadata.get(LoadBalancerConstants.WEIGHT_ATTR);
        if (weight != null) {
            requestData.setWeight((int) weight);
        }
        Object warmup = metadata.get(LoadBalancerConstants.WARMUP_ATTR);
        if (warmup != null) {
            requestData.setWarmup((int) warmup);
        }

        return requestData;
    }

    @Override
    public void transformServiceInstance(ServiceInstance serviceInstance, ServiceRequestContext<ServiceInstance> requestContext) {
        Map<String, String> metadata = serviceInstance.getMetadata();
        WeightRequestData requestData = (WeightRequestData) requestContext.getClientRequest();
        if (requestData.getWeight() == WeightRequestData.DEFAULT_WEIGHT) {
            String weight = metadata.get(WEIGHT_ATTR_NAME);
            requestData.setWeight(StringUtils.hasText(weight) ? Integer.parseInt(weight) : LoadBalancerConstants.DEFAULT_WEIGHT);
        }

        if (requestData.getWarmup() == WeightRequestData.DEFAULT_WARMUP) {
            String warmup = metadata.get(LoadBalancerConstants.WARMUP_ATTR);
            requestData.setWarmup(StringUtils.hasText(warmup) ? Integer.parseInt(warmup) : LoadBalancerConstants.DEFAULT_WARMUP);
        }

        String startupTime = serviceInstance.getMetadata().get(LoadBalancerConstants.STARTUP_KEY);
        requestData.setStartupTime(StringUtils.hasText(startupTime)
                ? Long.parseLong(startupTime) : LoadBalancerConstants.DEFAULT_STARTUP_TIME);
    }

    @Override
    public void setEnvironment(@NonNull Environment environment) {
        if (WEIGHT_ATTR_NAME == null) {
            boolean nacosEnabled = environment.getProperty("spring.cloud.nacos.discovery.enabled", boolean.class, true);
            WEIGHT_ATTR_NAME = nacosEnabled ? "nacos.weight" : LoadBalancerConstants.WEIGHT_ATTR;
        }
    }

}
