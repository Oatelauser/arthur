package com.arthur.plugin.route.constant;

import com.arthur.plugin.route.subscribe.RouteMultiConfigListener;
import com.arthur.plugin.route.subscribe.RouteSingleConfigListener;

/**
 * 路由常量
 *
 * @author DearYang
 * @date 2022-07-25
 * @since 1.0
 */
public interface GatewayRouteConstants {

    /**
     * 默认路由配置
     *
     * @see RouteMultiConfigListener
     * @see RouteSingleConfigListener
     */
    String META_ROUTE_DATA_ID = "arthur-dynamic-route.json";

    /**
     * 默认路由配置所有组
     *
     * @see RouteMultiConfigListener
     * @see RouteSingleConfigListener
     */
    String META_ROUTE_GROUP = "META_GROUP";

    /**
     * 路由默认配置命名空间
     */
    String META_ROUTE_NAMESPACE = "public";

}
