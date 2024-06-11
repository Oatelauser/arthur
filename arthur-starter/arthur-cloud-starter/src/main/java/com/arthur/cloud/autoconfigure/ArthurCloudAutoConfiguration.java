package com.arthur.cloud.autoconfigure;

import com.alibaba.nacos.api.naming.NamingService;
import com.arthur.boot.autoconfigure.ApplicationLifecycleAutoConfiguration;
import com.arthur.boot.lifecycle.ServiceInitializer;
import com.arthur.cloud.lifecycle.MicroServiceInitializer;
import com.arthur.cloud.nacos.NacosInstanceChangeEventListener;
import com.arthur.common.lifecycle.InitializeListener;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.client.serviceregistry.ServiceRegistry;
import org.springframework.cloud.loadbalancer.cache.LoadBalancerCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Spring Cloud 自动配置类
 *
 * @author DearYang
 * @date 2022-09-16
 * @since 1.0
 */
@SuppressWarnings("SpellCheckingInspection")
@AutoConfiguration(before = ApplicationLifecycleAutoConfiguration.class)
public class ArthurCloudAutoConfiguration {

	@ConditionalOnClass(NamingService.class)
	@Configuration(proxyBeanMethods = false)
	static class NacosCloudConfiguration {

		@Bean
		@ConditionalOnClass(LoadBalancerCacheManager.class)
		NacosInstanceChangeEventListener serviceInstanceEventListener() {
			return new NacosInstanceChangeEventListener();
		}


		@Bean
		@ConditionalOnMissingBean
		ServiceInitializer nacosServiceInitializer(ObjectProvider<List<InitializeListener>> provider,
			Registration registration, ServiceRegistry<Registration> serviceRegistry) {
			return new MicroServiceInitializer(provider, registration, serviceRegistry);
		}

	}

}
