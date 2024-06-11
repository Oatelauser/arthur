package com.arthur.plugin.loadbalance.core;

import com.arthur.common.utils.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.DefaultResponse;
import org.springframework.cloud.client.loadbalancer.EmptyResponse;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.loadbalancer.core.NoopServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.SelectedInstanceCallback;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 抽象负载均衡基类
 *
 * @author DearYang
 * @date 2022-07-22
 * @since 1.0
 */
@SuppressWarnings("rawtypes")
public abstract class AbstractServiceInstanceLoadBalancer implements NamedLoadBalancer, ReactorServiceInstanceLoadBalancer {

    private static final Log LOG = LogFactory.getLog(AbstractServiceInstanceLoadBalancer.class);
    private static final Response<ServiceInstance> EMPTY_RESPONSE = new EmptyResponse();

    /**
     * 服务名
     */
    private final String serviceId;
    private final ObjectProvider<ServiceInstanceListSupplier> serviceProvider;

    public AbstractServiceInstanceLoadBalancer(String serviceId, ObjectProvider<ServiceInstanceListSupplier> serviceProvider) {
        this.serviceId = serviceId;
        this.serviceProvider = serviceProvider;
    }

    @Override
    public Mono<Response<ServiceInstance>> choose(Request request) {
        ServiceInstanceListSupplier supplier = this.serviceProvider.getIfAvailable(NoopServiceInstanceListSupplier::new);
        return supplier.get(request).next().map((serviceInstances) ->
			this.processInstanceResponse(request, supplier, serviceInstances));
    }

    private Response<ServiceInstance> processInstanceResponse(Request request, ServiceInstanceListSupplier supplier, List<ServiceInstance> serviceInstances) {
        Response<ServiceInstance> serviceInstanceResponse = this.getInstanceResponse(request, serviceInstances);
        if (supplier instanceof SelectedInstanceCallback && serviceInstanceResponse.hasServer()) {
            ((SelectedInstanceCallback) supplier).selectedServiceInstance(serviceInstanceResponse.getServer());
        }

        return serviceInstanceResponse;
    }

    public Response<ServiceInstance> getInstanceResponse(Request request, List<ServiceInstance> instances) {
        if (CollectionUtils.isEmpty(instances)) {
            if (LOG.isWarnEnabled()) {
                LOG.warn("No servers available for service: " + this.serviceId);
            }
            return EMPTY_RESPONSE;
        }

        if (instances.size() == 1) {
            return new DefaultResponse(instances.get(0));
        }

        ServiceInstance serviceInstance = this.selectInstance(request, instances);
        return serviceInstance == null ? EMPTY_RESPONSE : new DefaultResponse(serviceInstance);
    }

    @SuppressWarnings("unused")
    public String getServiceId() {
        return serviceId;
    }

    protected abstract ServiceInstance selectInstance(Request request, List<ServiceInstance> instances);

}
