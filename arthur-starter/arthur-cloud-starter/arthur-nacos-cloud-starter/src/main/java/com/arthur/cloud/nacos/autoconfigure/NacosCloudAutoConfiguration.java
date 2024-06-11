package com.arthur.cloud.nacos.autoconfigure;

import com.alibaba.cloud.nacos.ConditionalOnNacosDiscoveryEnabled;
import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.NacosServiceManager;
import com.alibaba.cloud.nacos.registry.NacosRegistration;
import com.alibaba.cloud.nacos.registry.NacosRegistrationCustomizer;
import com.alibaba.cloud.nacos.registry.NacosServiceRegistry;
import com.alibaba.cloud.nacos.registry.NacosServiceRegistryAutoConfiguration;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.arthur.cloud.nacos.discovery.NacosContextManager;
import com.arthur.cloud.nacos.discovery.NacosRegistrationFactory;
import com.arthur.nacos.client.namespace.NacosNamespaceMaintainService;
import com.arthur.nacos.client.namespace.NamespaceMaintainService;
import com.arthur.nacos.client.namespace.NamespaceService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.CommonsClientAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Properties;

/**
 * Nacos cloud自动配置类
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-03-02
 * @since 1.0
 */
@SuppressWarnings("SpellCheckingInspection")
@AutoConfiguration(after = { CommonsClientAutoConfiguration.class,
	NacosServiceRegistryAutoConfiguration.class })
public class NacosCloudAutoConfiguration {

	@ConditionalOnClass(NamingService.class)
	@Configuration(proxyBeanMethods = false)
	static class NacosNamespaceServiceConfiguration {

		@ConditionalOnMissingBean
		@Bean(destroyMethod = "shutdown")
		NamespaceMaintainService namespaceMaintainService() {
			return new NacosNamespaceMaintainService();
		}

		@Bean
		@ConditionalOnMissingBean
		@ConditionalOnBean(NacosDiscoveryProperties.class)
		NamespaceService defaultNamespaceService(NacosDiscoveryProperties discoveryProperties,
				NamespaceMaintainService namespaceMaintainService) throws NacosException {
			Properties properties = discoveryProperties.getNacosProperties();
			return namespaceMaintainService.addNamespaceService(properties);
		}

	}

	@EnableDiscoveryClient
	@ConditionalOnNacosDiscoveryEnabled
	@Configuration(proxyBeanMethods = false)
	@ConditionalOnClass({ NacosServiceManager.class })
	static class NacosServiceRegistrationConfiguration {

		@Bean
		@ConditionalOnMissingBean
		NacosRegistrationFactory nacosRegistrationCreator(ObjectProvider<List<NacosRegistrationCustomizer>> registrationCustomizers) {
			return new NacosRegistrationFactory(registrationCustomizers.getIfAvailable());
		}

		@Bean
		@ConditionalOnMissingBean
		NacosContextManager nacosContextManager(
				ObjectProvider<NacosDiscoveryProperties> discoveryProperties,
				ObjectProvider<NacosServiceManager> serviceManager,
				ObjectProvider<NacosRegistration> registration,
				ObjectProvider<NacosServiceRegistry> serviceRegistry,
				NacosRegistrationFactory nacosRegistrationFactory) {
			return new NacosContextManager(discoveryProperties.getIfAvailable(), serviceManager.getIfAvailable(),
				registration.getIfAvailable(), serviceRegistry.getIfAvailable(), nacosRegistrationFactory);
		}

	}

}
