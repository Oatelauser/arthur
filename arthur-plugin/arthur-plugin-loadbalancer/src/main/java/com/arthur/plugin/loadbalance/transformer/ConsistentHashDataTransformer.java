package com.arthur.plugin.loadbalance.transformer;

import com.arthur.plugin.loadbalance.constant.LoadBalancerConstants;
import com.arthur.plugin.loadbalance.core.ConsistentHashLoadBalancer;
import com.arthur.plugin.loadbalance.data.ConsistentHashRequestData;
import com.arthur.plugin.loadbalance.support.ConsistentHashCalculator;
import com.arthur.plugin.loadbalance.support.JaninoLoadBalancerCompiler;
import org.springframework.cloud.client.loadbalancer.RequestData;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;

/**
 * 一致性哈希负载均衡算法转换器
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-01-16
 * @since 1.0
 */
public class ConsistentHashDataTransformer<R> implements LoadBalancerDataTransformer<R> {

    private final JaninoLoadBalancerCompiler janinoLoadBalancerCompiler;

    public ConsistentHashDataTransformer(JaninoLoadBalancerCompiler janinoLoadBalancerCompiler) {
        this.janinoLoadBalancerCompiler = janinoLoadBalancerCompiler;
    }

    @Override
    public boolean supports(ReactorLoadBalancer<R> lb) {
        return lb instanceof ConsistentHashLoadBalancer;
    }

    @Override
    public RequestData transformRequestData(String serviceId, Route route, ServerWebExchange exchange) {
        ConsistentHashRequestData requestData = new ConsistentHashRequestData(exchange.getRequest());
        String lbExpression = (String) route.getMetadata().get(LoadBalancerConstants.LB_EXPRESS_ATTR);
        if (StringUtils.hasText(lbExpression)) {
            ConsistentHashCalculator consistentHashCalculator = this.janinoLoadBalancerCompiler
                    .getConsistentHashCalculator(route.getId(), lbExpression);
            requestData.setConsistentHashCalculator(consistentHashCalculator);
        }
        return requestData;
    }

}
