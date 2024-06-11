package com.arthur.cloud.nacos.discovery;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.registry.NacosRegistration;
import com.alibaba.cloud.nacos.registry.NacosRegistrationCustomizer;

import java.util.List;

import static com.arthur.cloud.nacos.constant.NacosApplicationContext.EMPTY_APPLICATION_CONTEXT;

/**
 * {@link NacosRegistration}简单构造工厂
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-01
 * @since 1.0
 */
@SuppressWarnings("SpellCheckingInspection")
public class NacosRegistrationFactory {

	private final List<NacosRegistrationCustomizer> registrationCustomizers;

	public NacosRegistrationFactory(List<NacosRegistrationCustomizer> registrationCustomizers) {
		this.registrationCustomizers = registrationCustomizers;
	}

	public NacosRegistration obtainsRegistration(NacosDiscoveryProperties discoveryProperties) {
		NacosRegistration registration = new NacosRegistration(registrationCustomizers,
			discoveryProperties, EMPTY_APPLICATION_CONTEXT);
		registration.init();
		return registration;
	}

}
