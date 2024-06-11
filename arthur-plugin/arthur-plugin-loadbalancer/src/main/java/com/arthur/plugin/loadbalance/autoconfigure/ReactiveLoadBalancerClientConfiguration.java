package com.arthur.plugin.loadbalance.autoconfigure;

import com.arthur.plugin.loadbalance.core.*;
import com.arthur.plugin.loadbalance.metrics.ClusterNodeAdapter;
import com.arthur.plugin.loadbalance.metrics.ServiceInstanceMetrics;
import com.arthur.plugin.loadbalance.support.LoadBalanceLocator;
import com.arthur.plugin.loadbalance.support.ReactiveLoadBalanceLocator;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.RandomLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.cloud.loadbalancer.core.RoundRobinLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;

import java.util.List;

/**
 * 负载均衡算法配置类
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-01-04
 * @since 1.0
 */
public class ReactiveLoadBalancerClientConfiguration {

    private final ServiceInstanceMetrics<ClusterNodeAdapter> serviceInstanceMetrics;

    public ReactiveLoadBalancerClientConfiguration(ServiceInstanceMetrics<ClusterNodeAdapter> metrics) {
        this.serviceInstanceMetrics = metrics;
    }

	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public ReactorLoadBalancer<ServiceInstance> noopLoadBalancer() {
		return NoopLoadBalancer.getInstance();
	}

    @Bean
    public ReactorLoadBalancer<ServiceInstance> leastActiveLoadBalancer(Environment environment,
			LoadBalancerClientFactory loadBalancerClientFactory) {
		String serviceId = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
		return new LeastActiveLoadBalancer(serviceId, serviceInstanceMetrics,
			loadBalancerClientFactory.getLazyProvider(serviceId, ServiceInstanceListSupplier.class));
    }

    @Bean
    public ReactorLoadBalancer<ServiceInstance> shortestResponseLoadBalancer(Environment environment,
			LoadBalancerClientFactory loadBalancerClientFactory) {
		String serviceId = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
		return new ShortestResponseLoadBalancer(serviceId, serviceInstanceMetrics,
			loadBalancerClientFactory.getLazyProvider(serviceId, ServiceInstanceListSupplier.class));
    }

    @Bean
    public ReactorLoadBalancer<ServiceInstance> weightedRoundRobinLoadBalancer(Environment environment,
			 LoadBalancerClientFactory loadBalancerClientFactory) {
		String serviceId = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
		return new WeightedRoundRobinLoadBalancer(serviceId,
			loadBalancerClientFactory.getLazyProvider(serviceId, ServiceInstanceListSupplier.class));
    }

	@Bean
	public ReactorLoadBalancer<ServiceInstance> remoteIpHashLoadBalancer(Environment environment,
			LoadBalancerClientFactory loadBalancerClientFactory) {
		String serviceId = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
		return new RemoteIpHashLoadBalancer(serviceId,
			loadBalancerClientFactory.getLazyProvider(serviceId, ServiceInstanceListSupplier.class));
	}

	@Bean
	public ReactorLoadBalancer<ServiceInstance> randomLoadBalancer(Environment environment,
			LoadBalancerClientFactory loadBalancerClientFactory) {
		String serviceId = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
		return new RandomLoadBalancer(loadBalancerClientFactory.getLazyProvider(serviceId, ServiceInstanceListSupplier.class), serviceId);
	}

	@Bean
	public ReactorLoadBalancer<ServiceInstance> weightedRandomLoadBalancer(Environment environment,
			 LoadBalancerClientFactory loadBalancerClientFactory) {
		String serviceId = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
		return new WeightedRandomLoadBalancer(serviceId, loadBalancerClientFactory.getLazyProvider(serviceId, ServiceInstanceListSupplier.class));
	}

	@Bean
	public ReactorLoadBalancer<ServiceInstance> destinationHashLoadBalancer(Environment environment,
			LoadBalancerClientFactory loadBalancerClientFactory) {
		String serviceId = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
		return new DestinationHashLoadBalancer(serviceId,
			loadBalancerClientFactory.getLazyProvider(serviceId, ServiceInstanceListSupplier.class));
	}

	@Bean
	public ReactorLoadBalancer<ServiceInstance> consistentHashLoadBalancer(Environment environment,
			LoadBalancerClientFactory loadBalancerClientFactory) {
		String serviceId = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
		return new ConsistentHashLoadBalancer(serviceId,
			loadBalancerClientFactory.getLazyProvider(serviceId, ServiceInstanceListSupplier.class));
	}

	@Bean
	@Primary
	public ReactorLoadBalancer<ServiceInstance> roundRobinLoadBalancer(Environment environment,
	   		LoadBalancerClientFactory loadBalancerClientFactory) {
		String serviceId = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
		return new RoundRobinLoadBalancer(loadBalancerClientFactory.getLazyProvider(serviceId, ServiceInstanceListSupplier.class), serviceId);
	}

	@Bean
	@ConditionalOnMissingBean
	public LoadBalanceLocator<ReactorLoadBalancer<ServiceInstance>> loadBalanceLocator(
			ObjectProvider<List<ReactorLoadBalancer<ServiceInstance>>> provider) {
		return new ReactiveLoadBalanceLocator<>(provider);
	}

}
