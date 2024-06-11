package com.arthur.plugin.loadbalance.core;

import com.arthur.plugin.loadbalance.context.ServiceRequestContext;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;

import java.net.URI;

/**
 * 请求地址一致性哈希负载均衡算法
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-01-13
 * @since 1.0
 */
@SuppressWarnings({ "rawtypes", "unused" })
public class DestinationHashLoadBalancer extends AbstractConsistentHashLoadBalancer {

	public DestinationHashLoadBalancer(String serviceId, ObjectProvider<ServiceInstanceListSupplier> serviceProvider) {
		super(serviceId, serviceProvider);
	}

	@Override
	protected String getHashValue(Request request) {
		Object context = request.getContext();
		if (context instanceof ServiceRequestContext<?> requestContext) {
			URI url = requestContext.getClientRequest().getUrl();
			return url.toString();
		}

		return null;
	}

}
