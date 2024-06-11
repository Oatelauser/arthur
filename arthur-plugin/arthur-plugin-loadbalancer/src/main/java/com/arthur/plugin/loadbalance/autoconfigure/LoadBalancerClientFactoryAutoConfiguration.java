package com.arthur.plugin.loadbalance.autoconfigure;

import com.arthur.plugin.loadbalance.support.LoadBalancerContextFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClientsProperties;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClientSpecification;
import org.springframework.cloud.loadbalancer.config.LoadBalancerAutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.Collections;
import java.util.List;

/**
 * 负载均衡客户端工厂配置类
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-01-05
 * @since 1.0
 */
@AutoConfiguration(before = LoadBalancerAutoConfiguration.class)
@ConditionalOnProperty(value = "spring.cloud.loadbalancer.enabled", havingValue = "true", matchIfMissing = true)
public class LoadBalancerClientFactoryAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public LoadBalancerContextFactory loadBalancerClientFactory(LoadBalancerClientsProperties properties,
        	ObjectProvider<List<LoadBalancerClientSpecification>> configurations) {
		LoadBalancerContextFactory clientFactory = new LoadBalancerContextFactory(properties);
		clientFactory.setConfigurations(configurations.getIfAvailable(Collections::emptyList));
		return clientFactory;
	}

}
