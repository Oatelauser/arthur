package com.arthur.plugin.loadbalance.context;

import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.web.server.ServerWebExchange;

/**
 * 负载均衡请求处理器
 *
 * @author DearYang
 * @date 2022-09-19
 * @since 1.0
 */
public interface LoadBalancerProcessor<C, R> {

    /**
     * 负载均衡支持的标识符，比如：默认SpringCloud支持标识符是“lb”
     *
     * @return 标识符
     */
    String name();

    /**
     * 创建{@link Request}对象，保存请求信息
     *
     * @param serviceId    服务名
     * @param route        路由对象{@link Route}
     * @param exchange     {@link ServerWebExchange}
     * @param loadBalancer 负载均衡算法对象{@link ReactorLoadBalancer}
     * @return {@link Request}
     */
    Request<C> createRequest(String serviceId, Route route, ServerWebExchange exchange, ReactorLoadBalancer<R> loadBalancer);

}
