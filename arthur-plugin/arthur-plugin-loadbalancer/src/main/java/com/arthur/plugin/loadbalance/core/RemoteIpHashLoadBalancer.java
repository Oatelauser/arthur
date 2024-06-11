package com.arthur.plugin.loadbalance.core;

import com.arthur.common.algorithms.HashAlgorithm;
import com.arthur.plugin.loadbalance.context.ServiceRequestContext;
import com.arthur.plugin.loadbalance.data.RemoteIpRequestData;
import com.arthur.plugin.loadbalance.utils.ServiceInstanceUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Remote IP Hash路由负载均衡算法
 *
 * @author DearYang
 * @date 2022-07-24
 * @since 1.0
 */
public class RemoteIpHashLoadBalancer extends AbstractServiceInstanceLoadBalancer {

    public RemoteIpHashLoadBalancer(String serviceId, ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider) {
        super(serviceId, serviceInstanceListSupplierProvider);
    }

    @Override
    @SuppressWarnings("rawtypes")
    protected ServiceInstance selectInstance(Request request, List<ServiceInstance> instances) {
		String remoteIp = null;
		Object context = request.getContext();
		if (context instanceof ServiceRequestContext dataContext) {
            RemoteIpRequestData requestData = (RemoteIpRequestData) dataContext.getClientRequest();
			remoteIp = requestData.getRemoteIp();
		}

        if (StringUtils.hasText(remoteIp)) {
            int hash = HashAlgorithm.bkdrHash(remoteIp);
            int pos = hash % instances.size();
            return instances.get(pos);
        }
        return ServiceInstanceUtils.random(instances);
    }

}
