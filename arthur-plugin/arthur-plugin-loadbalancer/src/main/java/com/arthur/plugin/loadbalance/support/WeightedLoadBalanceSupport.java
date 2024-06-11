package com.arthur.plugin.loadbalance.support;

import com.arthur.plugin.loadbalance.core.AbstractWeightedServiceInstanceLoadBalancer;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.Request;

/**
 * 计算权重预热的负载均衡算法通用方法接口
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2022-12-27
 * @see AbstractWeightedServiceInstanceLoadBalancer
 * @since 1.0
 */
@SuppressWarnings("rawtypes")
public interface WeightedLoadBalanceSupport {

	/**
	 * 获取lb的权重
	 *
	 * @param request         请求对象
	 * @param serviceInstance 服务实例
	 * @return 权重
	 */
	int getWeight(Request request, ServiceInstance serviceInstance);

}
