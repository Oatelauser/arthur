package com.arthur.plugin.route.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.cloud.gateway.config.GatewayAutoConfiguration;
import org.springframework.context.annotation.Import;

import static org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type.REACTIVE;

/**
 * 网关路由统一自动配置类
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2022-12-30
 * @since 1.0
 */
@ConditionalOnWebApplication(type = REACTIVE)
@ConditionalOnClass(GatewayAutoConfiguration.class)
@AutoConfiguration(before = GatewayAutoConfiguration.class)
@Import({GatewayRouteConfiguration.class, GatewayDynamicRouteConfiguration.class})
public class ArthurRouteAutoConfiguration {

}
