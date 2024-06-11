package com.arthur.plugin.loadbalance.context;

import com.arthur.plugin.loadbalance.transformer.LoadBalancerDataTransformer;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.DefaultRequest;
import org.springframework.cloud.client.loadbalancer.LoadBalancerProperties;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.RequestData;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;
import java.util.Map;

/**
 * 默认的负载均衡请求处理器
 *
 * @author DearYang
 * @date 2022-09-19
 * @see LoadBalancerProcessor
 * @since 1.0
 */
public class LoadBalancerClusterProcessor implements LoadBalancerProcessor<ServiceRequestContext<ServiceInstance>, ServiceInstance> {

    private final LoadBalancerClientFactory clientFactory;
	private final List<LoadBalancerDataTransformer<ServiceInstance>> lbDataTransfers;

	public LoadBalancerClusterProcessor(LoadBalancerClientFactory clientFactory,
			List<LoadBalancerDataTransformer<ServiceInstance>> lbDataTransfers) {
		this.clientFactory = clientFactory;
		this.lbDataTransfers = lbDataTransfers;
    }

    @Override
    public String name() {
        return "lb";
    }

    @Override
    public Request<ServiceRequestContext<ServiceInstance>> createRequest(String serviceId, Route route,
			 ServerWebExchange exchange, ReactorLoadBalancer<ServiceInstance> loadBalancer) {
        RequestData requestData = null;
        LoadBalancerDataTransformer<ServiceInstance> transformer = null;
        for (LoadBalancerDataTransformer<ServiceInstance> lbDataTransfer : lbDataTransfers) {
            if (lbDataTransfer.supports(loadBalancer)) {
                requestData = lbDataTransfer.transformRequestData(serviceId, route, exchange);
                transformer = lbDataTransfer;
                break;
            }
        }

		ServiceRequestContext<ServiceInstance> dataContext = new ServiceRequestContext<>(this.getHint(serviceId),
                requestData == null ? new RequestData(exchange.getRequest()) : requestData);
        dataContext.setLb(loadBalancer);
        dataContext.setTransformer(transformer);
        return new DefaultRequest<>(dataContext);
    }

    private String getHint(String serviceId) {
        LoadBalancerProperties loadBalancerProperties = clientFactory.getProperties(serviceId);
        Map<String, String> hints = loadBalancerProperties.getHint();
        String defaultHint = hints.getOrDefault("default", "default");
        String hintPropertyValue = hints.get(serviceId);
        return hintPropertyValue != null ? hintPropertyValue : defaultHint;
    }

}
