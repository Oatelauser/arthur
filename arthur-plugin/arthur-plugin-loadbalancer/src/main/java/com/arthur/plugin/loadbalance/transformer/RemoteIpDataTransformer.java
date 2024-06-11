package com.arthur.plugin.loadbalance.transformer;

import com.arthur.plugin.loadbalance.core.RemoteIpHashLoadBalancer;
import com.arthur.plugin.loadbalance.data.RemoteIpRequestData;
import org.springframework.cloud.client.loadbalancer.RequestData;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.web.server.ServerWebExchange;

import static com.arthur.sentinel.constant.WebfluxConstants.REMOTE_IP_ATTR;

/**
 * 获取远程IP负载均衡数据传输
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-01-06
 * @since 1.0
 */
public class RemoteIpDataTransformer<R> implements LoadBalancerDataTransformer<R> {

    @Override
    public boolean supports(ReactorLoadBalancer<R> lb) {
        return lb instanceof RemoteIpHashLoadBalancer;
    }

    @Override
    public RequestData transformRequestData(String serviceId, Route route, ServerWebExchange exchange) {
        RemoteIpRequestData requestData = new RemoteIpRequestData(exchange.getRequest());
        requestData.setRemoteIp(exchange.getAttribute(REMOTE_IP_ATTR));
        return requestData;
    }

}
