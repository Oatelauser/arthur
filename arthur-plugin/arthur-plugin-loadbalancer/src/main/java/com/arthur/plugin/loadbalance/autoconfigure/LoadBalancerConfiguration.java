package com.arthur.plugin.loadbalance.autoconfigure;

import com.alibaba.csp.sentinel.node.ClusterNode;
import com.arthur.plugin.loadbalance.context.LoadBalancerProcessor;
import com.arthur.plugin.loadbalance.context.ServiceRequestContext;
import com.arthur.plugin.loadbalance.filter.GatewayLoadBalancerFilter;
import com.arthur.plugin.loadbalance.metrics.MicroServiceLoadBalancerLifecycle;
import com.arthur.plugin.loadbalance.metrics.ServiceInstanceMetrics;
import com.arthur.plugin.loadbalance.support.LoadBalanceContextStarter;
import com.arthur.plugin.loadbalance.support.LoadBalancerContextFactory;
import org.jctools.maps.NonBlockingHashMap;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.reactive.DeferringLoadBalancerExchangeFilterFunction;
import org.springframework.cloud.gateway.config.GatewayLoadBalancerProperties;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.config.conditional.ConditionalOnEnabledGlobalFilter;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.netty.http.client.HttpClient;

import java.util.List;

/**
 * 网关负载均衡配置类
 *
 * @author DearYang
 * @date 2022-07-24
 * @since 1.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnBean({LoadBalancerClientFactory.class, GatewayLoadBalancerProperties.class})
public class LoadBalancerConfiguration {

	private final GatewayLoadBalancerProperties properties;
	private final LoadBalancerClientFactory clientFactory;

	@SuppressWarnings("all")
	public LoadBalancerConfiguration(GatewayLoadBalancerProperties properties, LoadBalancerClientFactory clientFactory) {
		this.properties = properties;
		this.clientFactory = clientFactory;
	}

	@Bean
	@ConditionalOnMissingBean(ServiceInstanceMetrics.class)
	@ConditionalOnClass(value = {NonBlockingHashMap.class, ClusterNode.class})
	public MicroServiceLoadBalancerLifecycle loadBalancerLifecycle() {
		return new MicroServiceLoadBalancerLifecycle();
	}

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnEnabledGlobalFilter
	@ConditionalOnBean(LoadBalancerClientFactory.class)
	public GatewayLoadBalancerFilter gatewayLoadBalancerClientFilter(
			 ObjectProvider<List<LoadBalancerProcessor<ServiceRequestContext<ServiceInstance>, ServiceInstance>>> provider) {
		return new GatewayLoadBalancerFilter(properties, clientFactory, provider);
	}

	@Bean
	@ConditionalOnMissingBean
	@SuppressWarnings("rawtypes")
	@ConditionalOnBean({HttpClient.class, LoadBalancerContextFactory.class})
	public LoadBalanceContextStarter loadBalanceStarter(GatewayProperties properties,
			LoadBalancerProperties lbProperties,
			List<LoadBalancerProcessor> lbHandlers,
			DeferringLoadBalancerExchangeFilterFunction exchangeFilterFunction) {
		return new LoadBalanceContextStarter(properties, lbProperties, lbHandlers, exchangeFilterFunction);
	}

}
