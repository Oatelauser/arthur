package com.arthur.plugin.loadbalance.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.ConditionalOnDiscoveryEnabled;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.context.annotation.Import;

/**
 * 负载均衡自动配置类
 *
 * @author DearYang
 * @date 2022-08-04
 * @since 1.0
 */
@AutoConfiguration
@ConditionalOnDiscoveryEnabled
@EnableConfigurationProperties(LoadBalancerProperties.class)
@Import({LoadBalancerConfiguration.class, LoadBalancerContextConfiguration.class})
@LoadBalancerClients(defaultConfiguration = ReactiveLoadBalancerClientConfiguration.class)
@ConditionalOnProperty(value = "spring.cloud.loadbalancer.enabled", havingValue = "true", matchIfMissing = true)
public class ArthurLoadBalancerAutoConfiguration {

}
