package com.arthur.plugin.loadbalance.core;

import com.arthur.common.utils.StringUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.DefaultRequestContext;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.RequestData;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.http.HttpHeaders;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 灰度发布负载均衡算法
 *
 * @author DearYang
 * @date 2022-08-04
 * @since 1.0
 */
public class GrayLoadBalancer extends AbstractServiceInstanceLoadBalancer {

	private final AtomicInteger position;

	public GrayLoadBalancer(int seedPosition, String serviceId, ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider) {
		super(serviceId, serviceInstanceListSupplierProvider);
		this.position = new AtomicInteger(seedPosition);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected ServiceInstance selectInstance(Request request, List<ServiceInstance> instances) {
		DefaultRequestContext requestContext = (DefaultRequestContext) request.getContext();
		RequestData clientRequest = (RequestData) requestContext.getClientRequest();
		HttpHeaders headers = clientRequest.getHeaders();
		String version = headers.getFirst("version");
		if (StringUtils.isBlank(version)) {
			return null;
		}

		List<ServiceInstance> serviceInstances = instances.stream().filter(instance -> version.equals(instance.getMetadata().get("version")))
			.collect(Collectors.toList());


		return null;
	}

}
