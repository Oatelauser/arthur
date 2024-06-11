package com.arthur.plugin.loadbalance.context;

import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;

/**
 * 负载均衡数据传输上下文
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-01-06
 * @since 1.0
 */
public record LbDataContext<R>(String serviceId, Route route, ReactorLoadBalancer<R> loadBalancer) {
}
