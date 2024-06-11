package com.arthur.plugin.loadbalance.cluster.core;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.RequestData;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 选择相同的区域
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-01-06
 * @since 1.0
 */
public class ZoneAvoidanceServiceInstanceListSupplier extends AbstractRequestBasedServiceInstanceListSupplier<RequestData> {

	public ZoneAvoidanceServiceInstanceListSupplier(ServiceInstanceListSupplier delegate) {
		super(delegate);
	}

	@Override
	public List<ServiceInstance> selectInstance(Request<RequestData> request, List<ServiceInstance> serviceInstances) {
		List<ServiceInstance> filteredInstances = new ArrayList<>();
		for (ServiceInstance serviceInstance : serviceInstances) {
			String instanceZone = getZone(serviceInstance);
			//if (zone.equalsIgnoreCase(instanceZone)) {
			//	filteredInstances.add(serviceInstance);
			//}
		}
		if (filteredInstances.size() > 0) {
			return filteredInstances;
		}
		// If the zone is not set or there are no zone-specific instances available,
		// we return all instances retrieved for given service id.
		return serviceInstances;
	}

	//private String getZone(Request<RequestData> request) {
	//	if (request.getContext() instanceof NamespaceRequestData requestData) {
	//		return requestData.getEnv()
	//	}
	//
	//}

	private String getZone(ServiceInstance serviceInstance) {
		Map<String, String> metadata = serviceInstance.getMetadata();
		if (metadata != null) {
			//return metadata.get(ZONE);
		}
		return null;
	}

}
