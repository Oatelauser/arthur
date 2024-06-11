package com.arthur.plugin.loadbalance.transformer;

import com.arthur.plugin.loadbalance.context.ServiceRequestContext;
import org.springframework.cloud.client.loadbalancer.RequestData;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.util.Assert;
import org.springframework.web.server.ServerWebExchange;

/**
 * 负载均衡数据传输委托代理类
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-01-06
 * @since 1.0
 */
public class ForwardingLoadBalancerDataTransformer<R> implements LoadBalancerDataTransformer<R> {

    private final LoadBalancerDataTransformer<R> delegator;

    public ForwardingLoadBalancerDataTransformer(LoadBalancerDataTransformer<R> lbDataTransfer) {
        Assert.notNull(lbDataTransfer, "lbDataTransfer cannot be null");
        this.delegator = lbDataTransfer;
    }

    @Override
    public boolean supports(ReactorLoadBalancer<R> lb) {
        return this.delegator.supports(lb);
    }

    @Override
    public RequestData transformRequestData(String serviceId, Route route, ServerWebExchange exchange) {
        return this.delegator.transformRequestData(serviceId, route, exchange);
    }

    @Override
    public void transformServiceInstance(R serviceInstance, ServiceRequestContext<R> requestContext) {
        this.delegator.transformServiceInstance(serviceInstance, requestContext);
    }

    public LoadBalancerDataTransformer<R> getLoadBalancerDataTransfer() {
        return delegator;
    }

}
