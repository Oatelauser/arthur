package com.arthur.plugin.loadbalance.autoconfigure;

import com.arthur.plugin.loadbalance.context.LoadBalancerClusterProcessor;
import com.arthur.plugin.loadbalance.context.LoadBalancerProcessor;
import com.arthur.plugin.loadbalance.context.ServiceRequestContext;
import com.arthur.plugin.loadbalance.support.JaninoLoadBalancerCompiler;
import com.arthur.plugin.loadbalance.transformer.ConsistentHashDataTransformer;
import com.arthur.plugin.loadbalance.transformer.LoadBalancerDataTransformer;
import com.arthur.plugin.loadbalance.transformer.RemoteIpDataTransformer;
import com.arthur.plugin.loadbalance.transformer.WeightedDataTransformer;
import org.codehaus.commons.compiler.IClassBodyEvaluator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;

/**
 * 负载均衡上下文配置类
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-01-06
 * @since 1.0
 */
@Configuration(proxyBeanMethods = false)
public class LoadBalancerContextConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(IClassBodyEvaluator.class)
    public JaninoLoadBalancerCompiler janinoLoadBalancerCompiler() {
        return new JaninoLoadBalancerCompiler();
    }

    @Bean
    public LoadBalancerDataTransformer<ServiceInstance> weightedDataTransfer() {
        return new WeightedDataTransformer();
    }

    @Bean
    public LoadBalancerDataTransformer<ServiceInstance> remoteIpDataTransfer() {
        return new RemoteIpDataTransformer<>();
    }

	@Bean
	public LoadBalancerDataTransformer<ServiceInstance> consistentHashDataTransformer(JaninoLoadBalancerCompiler janinoLoadBalancerCompiler) {
		return new ConsistentHashDataTransformer<>(janinoLoadBalancerCompiler);
	}

    @Bean
    @Primary
    public LoadBalancerProcessor<ServiceRequestContext<ServiceInstance>, ServiceInstance> defaultLoadBalancerProcessor(
            LoadBalancerClientFactory clientFactory,
            List<LoadBalancerDataTransformer<ServiceInstance>> lbDataTransfers) {
        return new LoadBalancerClusterProcessor(clientFactory, lbDataTransfers);
    }

}
