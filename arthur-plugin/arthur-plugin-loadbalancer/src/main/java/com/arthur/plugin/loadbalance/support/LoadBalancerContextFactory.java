package com.arthur.plugin.loadbalance.support;

import org.springframework.cloud.client.loadbalancer.LoadBalancerClientsProperties;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.support.GenericApplicationContext;

import java.util.Map;

/**
 * 通用负载均衡工厂，提供上下文的获取和创建
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-01-05
 * @since 1.0
 */
public class LoadBalancerContextFactory extends LoadBalancerClientFactory {

	public LoadBalancerContextFactory(LoadBalancerClientsProperties properties) {
		super(properties);
	}

	public LoadBalancerContextFactory(LoadBalancerClientsProperties properties, Map<String, ApplicationContextInitializer<GenericApplicationContext>> applicationContextInitializers) {
		super(properties, applicationContextInitializers);
	}

	@Override
	public GenericApplicationContext getContext(String name) {
		return super.getContext(name);
	}

}
