package com.arthur.plugin.loadbalance.core;

import com.arthur.plugin.loadbalance.context.ServiceRequestContext;
import com.arthur.plugin.loadbalance.data.ConsistentHashRequestData;
import com.arthur.plugin.loadbalance.support.ConsistentHashCalculator;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;

/**
 * 基于动态表达式实现的一致性哈希负载均衡算法
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-01-15
 * @since 1.0
 */
@SuppressWarnings("rawtypes")
public class ConsistentHashLoadBalancer extends AbstractConsistentHashLoadBalancer {

	public ConsistentHashLoadBalancer(String serviceId,
			ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider) {
		super(serviceId, serviceInstanceListSupplierProvider);
	}

	@Override
	protected String getHashValue(Request request) {
		Object context = request.getContext();
		if (!(context instanceof ServiceRequestContext<?> requestContext)) {
			return null;
		}

		ConsistentHashRequestData requestData = (ConsistentHashRequestData) requestContext.getClientRequest();
		ConsistentHashCalculator calculator = requestData.getConsistentHashCalculator();
		return calculator == null ? null : calculator.getHashValue(requestData);
	}

}
