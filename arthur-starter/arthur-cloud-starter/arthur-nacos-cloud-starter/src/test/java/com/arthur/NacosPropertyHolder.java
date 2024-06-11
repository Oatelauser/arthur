package com.arthur;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.NacosServiceManager;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Properties;

/**
 * 概要描述
 * <p>
 * 详细描述
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-03-02
 * @since 1.0
 */
public class NacosPropertyHolder {

    public static void initializeNacosDiscoveryProperties(NacosDiscoveryProperties discoveryProperties) {
        NacosServiceManager serviceManager = new NacosServiceManager();

        //Field eventPublisherField = ReflectionUtils.findField(NacosDiscoveryProperties.class, "applicationEventPublisher");
        //ReflectionUtils.makeAccessible(Objects.requireNonNull(eventPublisherField, "applicationEventPublisher"));
        //ReflectionUtils.setField(eventPublisherField, discoveryProperties, applicationEventPublisher);

        Field serviceManagerField = ReflectionUtils.findField(NacosDiscoveryProperties.class, "nacosServiceManager");
        ReflectionUtils.makeAccessible(Objects.requireNonNull(serviceManagerField, "nacosServiceManager"));
        ReflectionUtils.setField(serviceManagerField, discoveryProperties, serviceManager);

        Field environmentField = ReflectionUtils.findField(NacosDiscoveryProperties.class, "environment");
        ReflectionUtils.makeAccessible(Objects.requireNonNull(environmentField, "environment"));
        ReflectionUtils.setField(environmentField, discoveryProperties, new AbstractEnvironment() {});

        Assert.isTrue(StringUtils.hasText(discoveryProperties.getIp()), "ip is empty");
        Assert.isTrue(discoveryProperties.getPort() > 0, "port > 0");

        try {
            discoveryProperties.init();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

	public static NacosDiscoveryProperties discoveryProperties() {
		NacosDiscoveryProperties discoveryProperties = new NacosDiscoveryProperties();
		discoveryProperties.setIp("192.168.5.153");
		discoveryProperties.setPort(11111);
		discoveryProperties.setService("demo1");
		//discoveryProperties.setNamespace("dev");
		discoveryProperties.setUsername("nacos");
		discoveryProperties.setPassword("nacos");
		discoveryProperties.setServerAddr("192.168.5.153:8848");
		discoveryProperties.setFailureToleranceEnabled(true);
		initializeNacosDiscoveryProperties(discoveryProperties);
		return discoveryProperties;
	}

    public static Properties get() {
        return discoveryProperties().getNacosProperties();
    }

}
