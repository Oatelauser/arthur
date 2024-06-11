package com.arthur.plugin.route.core;

import org.springframework.util.ObjectUtils;

import java.util.Map;

/**
 * 针对网关路由拓展的args参数特殊处理
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-01-17
 * @see GatewayRouteDefinition#getArgs()
 * @since 1.0
 */
public class GatewayRouteArgsAccessor implements GatewayRouteAccessor {

    @Override
    public String attributeName() {
        return "args";
    }

    @Override
    @SuppressWarnings("unchecked")
    public void writeTo(Object value, Map<String, Object> metadata) {
        if (ObjectUtils.isEmpty(value)) {
            return;
        }

        Map<String, String> args = (Map<String, String>) value;
        args.forEach((k, v) -> metadata.put(GatewayRouteAccessor.getMetadataArgsKey(k), v));
    }

}
