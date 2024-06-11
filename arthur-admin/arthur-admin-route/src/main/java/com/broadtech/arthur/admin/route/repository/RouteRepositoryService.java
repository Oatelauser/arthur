package com.broadtech.arthur.admin.route.repository;

import com.broadtech.arthur.admin.common.respones.ResponseVo;
import com.broadtech.arthur.admin.route.entity.dto.RouteDefinitionDTO;
import com.broadtech.arthur.admin.route.entity.nc.GatewayRouteDefinition;
import com.broadtech.arthur.admin.route.entity.po.RouteDefinition;
import com.broadtech.arthur.admin.route.entity.vo.RouteDefinitionV0;
import com.broadtech.arthur.common.config.ConfigMetaData;
import com.google.common.base.Optional;

import java.util.List;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/9/27
 */
public interface RouteRepositoryService {


    /**
     * 创建路线
     *
     * @param gatewayRouteDefinition 网关路由定义
     * @param routeDefinition        路由定义
     * @param configMetaData         配置元数据
     * @return {@link ResponseVo}
     */
    boolean createRoute(GatewayRouteDefinition gatewayRouteDefinition, RouteDefinition routeDefinition, ConfigMetaData configMetaData);


    /**
     * 德尔路线
     *
     * @param id             id
     * @param configMetaData 配置元数据
     * @return {@link ResponseVo}
     */
    boolean delRoute(String id, ConfigMetaData configMetaData);


    /**
     * 更新路线
     *
     * @param gatewayRouteDefinition 网关路由定义
     * @param routeDefinition        路由定义
     * @param configMetaData         配置元数据
     * @return {@link ResponseVo}
     */
    boolean updateRoute(GatewayRouteDefinition gatewayRouteDefinition, RouteDefinition routeDefinition,ConfigMetaData configMetaData);


    /**
     * 查询路线
     *
     * @param id id
     * @return {@link Optional}<{@link RouteDefinitionV0}>
     */
    Optional<RouteDefinition> queryRoute(String id);


    /**
     * 查询路线下组
     *
     * @param gId g id
     * @return {@link List}<{@link RouteDefinitionV0}>
     */
    List<RouteDefinition> queryRouteUnderGroup(String gId);

}
