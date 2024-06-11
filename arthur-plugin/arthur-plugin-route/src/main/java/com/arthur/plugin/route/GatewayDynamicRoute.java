package com.arthur.plugin.route;

import org.springframework.cloud.gateway.route.RouteDefinition;

import java.util.List;

/**
 * 网关动态路由抽象接口
 *
 * @author DearYang
 * @date 2022-07-26
 * @since 1.0
 */
public interface GatewayDynamicRoute {

    /**
     * 获取所有的路由定义
     *
     * @return 路由定义集合
     */
    List<RouteDefinition> getAllRouteDefinition();

    /**
     * 单路由查询
     *
     * @param routeId 路由ID
     * @return {@link RouteDefinition}
     */
    RouteDefinition getRouteDefinition(String routeId);

    /**
     * 新增路由
     *
     * @param definition 路由定义
     */
    void add(RouteDefinition definition);

    /**
     * 更新路由
     *
     * @param definition 路由定义
     */
    void update(RouteDefinition definition);

    /**
     * 删除路由
     *
     * @param definition 路由定义
     */
    void delete(RouteDefinition definition);

    /**
     * 删除路由
     *
     * @param routeId 路由ID
     */
    void delete(String routeId);

}
