package com.arthur.cloud.nacos.discovery;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.NacosServiceManager;
import com.alibaba.cloud.nacos.registry.NacosRegistration;
import com.alibaba.cloud.nacos.registry.NacosServiceRegistry;

/**
 * Nacos服务注册上下文
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-01
 * @see NacosDiscoveryProperties
 * @see NacosRegistration
 * @see NacosContextManager
 * @see NacosServiceRegistry
 * @since 1.0
 */
@SuppressWarnings("SpellCheckingInspection")
public record NacosServiceContext(NacosDiscoveryProperties discoveryProperties, NacosRegistration registration,
	  NacosServiceManager serviceManager, NacosServiceRegistry serviceRegistry) {

	/**
	 * Nacos服务注册
	 */
	public void register() {
		serviceRegistry.register(registration);
	}

}
