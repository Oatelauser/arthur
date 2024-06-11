package com.arthur.plugin.config.nacos;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.common.utils.StringUtils;
import com.arthur.plugin.config.nacos.autoconfigure.NacosProperties;
import com.arthur.plugin.config.nacos.constant.NacosConstant;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Nacos配置管理器
 *
 * @author DearYang
 * @date 2022-07-25
 * @since 1.0
 */
@SuppressWarnings("unused")
public class NacosConfigManager implements AutoCloseable {

    /**
     * namespace - ConfigService
     */
    private final Map<String, ConfigService> configServiceMap = new ConcurrentHashMap<>();

    public ConfigService addConfigService(NacosProperties nacosProperties) throws NacosException {
        String namespace = nacosProperties.getNamespace();
        if (StringUtils.isBlank(namespace)) {
            namespace = NacosConstant.DEFAULT_NAMESPACE;
        }

        ConfigService configService = configServiceMap.get(namespace);
        if (configService != null) {
            return configService;
        }

        configService = create(nacosProperties);
        configServiceMap.put(namespace, configService);
        return configService;
    }

    public ConfigService create(NacosProperties nacosProperties) throws NacosException {
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.SERVER_ADDR, nacosProperties.getUrl());
        String namespace = nacosProperties.getNamespace();
        if (StringUtils.isNotBlank(namespace)) {
            properties.put(PropertyKeyConst.NAMESPACE, namespace);
        }
        if (nacosProperties.getUsername() != null) {
            properties.put(PropertyKeyConst.USERNAME, nacosProperties.getUsername());
        }
        if (nacosProperties.getPassword() != null) {
            properties.put(PropertyKeyConst.PASSWORD, nacosProperties.getPassword());
        }

        ConfigService configService = NacosFactory.createConfigService(properties);
        configServiceMap.put(namespace == null ? NacosConstant.DEFAULT_NAMESPACE : namespace, configService);
        return configService;
    }

    public ConfigService getConfigService(String namespace) {
        return configServiceMap.get(namespace);
    }

    public ConfigService removeConfigService(String namespace) {
        return configServiceMap.remove(namespace);
    }

    @Override
    public void close() throws Exception {
        for (ConfigService configService : configServiceMap.values()) {
            configService.shutDown();
        }
    }
}
