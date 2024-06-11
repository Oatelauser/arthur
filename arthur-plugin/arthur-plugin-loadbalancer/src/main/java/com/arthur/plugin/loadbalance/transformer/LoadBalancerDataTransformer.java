package com.arthur.plugin.loadbalance.transformer;

import com.arthur.plugin.loadbalance.context.ServiceRequestContext;
import org.springframework.cloud.client.loadbalancer.RequestData;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.web.server.ServerWebExchange;

/**
 * 负载均衡请求数据上下文转换器
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-01-06
 * @see org.springframework.cloud.client.loadbalancer.RequestDataContext
 * @since 1.0
 */
public interface LoadBalancerDataTransformer<R> {

	/**
	 * 是否支持传输的负载均衡算法
	 *
	 * @param lb 负载均衡算法
	 * @return true-支持
	 */
	boolean supports(ReactorLoadBalancer<R> lb);

	/**
	 * 创建负载均衡的请求数据
	 *
	 * @param exchange {@link ServerWebExchange}
	 * @return 请求数据
	 */
	default RequestData transformRequestData(String serviceId, Route route, ServerWebExchange exchange) {
		return new RequestData(exchange.getRequest());
	}

	/**
	 * 处理服务实例数据上下文
	 *
	 * @param serviceInstance 服务实例
	 * @param requestContext  负载均衡请求上下文
	 */
	default void transformServiceInstance(R serviceInstance, ServiceRequestContext<R> requestContext) {}

}
