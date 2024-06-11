package com.arthur.cloud.nacos.discovery;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.NacosServiceManager;
import com.alibaba.cloud.nacos.registry.NacosRegistration;
import com.alibaba.cloud.nacos.registry.NacosServiceRegistry;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.arthur.NacosPropertyHolder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static com.arthur.cloud.nacos.constant.NacosApplicationContext.EMPTY_APPLICATION_CONTEXT;


class NacosContextManagerTest {

	static NacosContextManager contextManager;

	@BeforeAll
	static void beforeAll() {
		NacosDiscoveryProperties properties = NacosPropertyHolder.discoveryProperties();
		NacosServiceManager serviceManager = new NacosServiceManager();
		serviceManager.setNacosDiscoveryProperties(properties);
		NacosRegistration registration = new NacosRegistration(List.of(), properties, EMPTY_APPLICATION_CONTEXT);
		NacosServiceRegistry serviceRegistry = new NacosServiceRegistry(serviceManager, properties);
		NacosRegistrationFactory registrationFactory = new NacosRegistrationFactory(List.of());
		contextManager = new NacosContextManager(properties, serviceManager, registration, serviceRegistry, registrationFactory);
		contextManager.start();
	}

	@AfterAll
	static void afterAll() {
		contextManager.shutdown();
	}

	@Test
	void obtainsNacosContext() {
		NacosServiceContext nacosServiceContext = contextManager.obtainsNacosContext();
		Assertions.assertNotNull(nacosServiceContext);
	}

	@Test
	void testObtainsNacosContext() {
		NacosServiceContext nacosServiceContext = contextManager.obtainsNacosContext("public", "demo1");
		Assertions.assertNotNull(nacosServiceContext);
	}

	@Test
	void testObtainsNacosContext1() throws NacosException {
		NacosDiscoveryProperties properties = NacosPropertyHolder.discoveryProperties();
		NacosServiceContext nacosServiceContext = contextManager.obtainsNacosContext(properties);
		Assertions.assertEquals(nacosServiceContext, contextManager.obtainsNacosContext());

		properties.setService("test");
		NacosServiceContext serviceContext = contextManager.obtainsNacosContext(properties);
		serviceContext.register();
		List<Instance> allInstances = serviceContext.serviceManager().getNamingService().getAllInstances("test");
		Assertions.assertFalse(CollectionUtils.isEmpty(allInstances));
	}

	@Test
	void switchNacosContext() {
		NacosDiscoveryProperties properties = NacosPropertyHolder.discoveryProperties();
		NacosServiceContext nacosServiceContext = contextManager.obtainsNacosContext(properties);
		Assertions.assertEquals(nacosServiceContext, contextManager.obtainsNacosContext());
	}

	@Test
	void register() throws NacosException {
		contextManager.obtainsNacosContext().register();
		NacosServiceContext nacosContext = contextManager.obtainsNacosContext("public", "demo1");
		List<Instance> allInstances = nacosContext.serviceManager()
			.getNamingService().getAllInstances("demo1");
		Assertions.assertFalse(CollectionUtils.isEmpty(allInstances));
	}

}
