package com.arthur.plugin.config.nacos.constant;

import com.arthur.plugin.config.nacos.NacosConfigManager;
import com.arthur.plugin.config.nacos.NacosConfigTemplate;

/**
 * Nacos配置中心常量
 *
 * @author DearYang
 * @date 2022-07-25
 * @since 1.0
 */
public interface NacosConstant {

    /**
     * nacos监听超时时间
     *
     * @see NacosConfigTemplate
     */
    long NACOS_TIMEOUT = 30000;

    /**
     * 默认命名空间
     *
     * @see NacosConfigManager
     */
    String DEFAULT_NAMESPACE = "public";


}
