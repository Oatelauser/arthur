package com.arthur.plugin.discovery.autoconfigure;

import org.jctools.maps.NonBlockingHashMap;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.discovery.simple.SimpleDiscoveryProperties;

import java.util.List;
import java.util.Map;

/**
 * 实现并发安全的静态服务注册配置信息
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-01
 * @since 1.0
 */
@ConfigurationProperties(prefix = "spring.cloud.discovery.client.simple")
public class ConcurrentDiscoveryProperties extends SimpleDiscoveryProperties {

    private final Map<String, List<DefaultServiceInstance>> instances = new NonBlockingHashMap<>();

    public ConcurrentDiscoveryProperties() {
        super.setInstances(null);
    }

    @Override
    public void setInstances(Map<String, List<DefaultServiceInstance>> instances) {
        this.instances.putAll(instances);
    }

    @Override
    public Map<String, List<DefaultServiceInstance>> getInstances() {
        return instances;
    }

}
