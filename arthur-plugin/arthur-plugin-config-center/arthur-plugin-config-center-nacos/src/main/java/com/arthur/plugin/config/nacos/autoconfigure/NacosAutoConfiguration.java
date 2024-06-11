package com.arthur.plugin.config.nacos.autoconfigure;

import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.arthur.common.config.ConfigTemplate;
import com.arthur.plugin.config.nacos.NacosConfigManager;
import com.arthur.plugin.config.nacos.NacosConfigTemplate;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * Nacos配置
 *
 * @author DearYang
 * @date 2022-07-25
 * @since 1.0
 */
@AutoConfiguration
@EnableConfigurationProperties(NacosProperties.class)
@ConditionalOnProperty(value = "arthur.config.nacos.enabled", havingValue = "true")
public class NacosAutoConfiguration {

    @Bean
    public NacosConfigManager configManager() {
        return new NacosConfigManager();
    }

    @Bean
    @ConditionalOnMissingBean
    public ConfigTemplate configTemplate(NacosProperties nacosProperties, NacosConfigManager configManager) throws NacosException {
        ConfigService configService = configManager.addConfigService(nacosProperties);
        return new NacosConfigTemplate(null, configService);
    }

}
