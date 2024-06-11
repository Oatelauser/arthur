package com.arthur.plugin.loadbalance.constant;

import com.arthur.plugin.route.core.GatewayRouteAccessor;

/**
 * 负载均衡常量
 *
 * @author DearYang
 * @date 2022-07-24
 * @since 1.0
 */
public interface LoadBalancerConstants {

    /**
     * 一致性哈希虚拟节点数
     */
    int VIRTUAL_NODE_NUMBER = 40;

    /**
     * 负载均衡初始化URL中的占位符参数名
     */
    String LB_URL_ATTR_NAME = "serviceId";
    String LB_URL_ATTR = "{" + LB_URL_ATTR_NAME + "}";

    /**
     * 负载均衡算法路由元数据key
     */
    String LB_ATTR = GatewayRouteAccessor.getMetadataKey("lb");

    /**
     * 负载均衡参数
     */
    String LB_ARG_ATTR = GatewayRouteAccessor.getMetadataKey("args");

    /**
     * 负载均衡权重路由元数据key
     */
    String WEIGHT_ATTR = GatewayRouteAccessor.getMetadataKey("weight");

    /**
     * 负载均衡表达式
     */
    String LB_EXPRESS_ATTR = GatewayRouteAccessor.getMetadataArgsKey("lb-express");

    /**
     * 默认权重
     */
    int DEFAULT_WEIGHT = 100;

    /**
     * 负载均衡预热路由元数据key
     */
    String WARMUP_ATTR = GatewayRouteAccessor.getMetadataKey("warmup");

    /**
     * 服务预热时间，默认10分钟
     */
    int DEFAULT_WARMUP = 10 * 60 * 1000;

    /**
     * 服务启动时间参数名
     */
    String STARTUP_KEY = GatewayRouteAccessor.getMetadataKey("startup");

    /**
     * 默认服务启动时间
     */
    long DEFAULT_STARTUP_TIME = 0;

    /**
     * 默认最大权重
     */
    double DEFAULT_MAX_WEIGHT = 100.0d;

    /**
     * 默认最小权重
     */
    double DEFAULT_MIN_WEIGHT = 0.0d;

    /**
     * 默认优先级
     */
    int DEFAULT_PRIORITY = 0;

}
