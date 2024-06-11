package com.arthur.plugin.route.autoconfigure;

import com.arthur.common.config.ConfigMetaData;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 路由配置信息
 *
 * @author DearYang
 * @date 2022-07-25
 * @since 1.0
 */
@ConfigurationProperties(prefix = "arthur.route")
public class ArthurRouteProperties extends ConfigMetaData {

    private ConfigMetaData multiRoute;

    public ConfigMetaData getMultiRoute() {
        return multiRoute;
    }

    public void setMultiRoute(ConfigMetaData multiRoute) {
        this.multiRoute = multiRoute;
    }

}
