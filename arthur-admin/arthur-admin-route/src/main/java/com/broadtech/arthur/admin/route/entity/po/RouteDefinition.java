package com.broadtech.arthur.admin.route.entity.po;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.broadtech.arthur.admin.route.entity.dto.RouteDefinitionDTO;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 路由表
 *
 * @author Machenike
 * @TableName route
 */
@TableName(value = "route")
@Data
@Builder
public class RouteDefinition implements Serializable {
    /**
     *
     */
    @TableId
    private String id;

    @NotNull
    /**
     * 所属组
     */
    private String groupId;
    @NotNull
    /**
     * 匹配路由
     */
    private String uri;

    /**
     *
     */
    private String metadata;

    @NotNull
    /**
     * 断言集合
     */
    private String predicates;

    /**
     * 过滤器集合
     */
    private String filters;

    /**
     *
     */
    private Integer routeOrder;
    @NotNull
    /**
     * 路由id
     */
    private String routeId;

    private Integer responseTimeout;

    private Integer connectTimeout;


    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }


    /***
     * obj to str
     * @param apiRouteDefinition
     * @return
     */
    public static RouteDefinition assembleApiRouteDefinition(RouteDefinitionDTO apiRouteDefinition) {
        RouteDefinitionBuilder bakBuilder = RouteDefinition.builder()
                .id(apiRouteDefinition.getId())
                .routeId(apiRouteDefinition.getRouteId())
                .routeOrder(apiRouteDefinition.getRouteOrder())
                .connectTimeout(apiRouteDefinition.getConnectTimeout())
                .responseTimeout(apiRouteDefinition.getResponseTimeout())
                .groupId(apiRouteDefinition.getGroupId())
                .uri(apiRouteDefinition.getUri());

        return assembleApiRouteDefinition0(bakBuilder, apiRouteDefinition);
    }

    private static RouteDefinition assembleApiRouteDefinition0(RouteDefinitionBuilder bakBuilder, RouteDefinitionDTO apiRouteDefinition) {

        if (CollectionUtil.isNotEmpty(apiRouteDefinition.getPredicates()))
            bakBuilder.predicates(JSON.toJSONString(apiRouteDefinition.getPredicates()));

        if (CollectionUtil.isNotEmpty(apiRouteDefinition.getFilters()))
            bakBuilder.filters(JSON.toJSONString(apiRouteDefinition.getFilters()));

        if (MapUtil.isNotEmpty(apiRouteDefinition.getMetadata()))
            bakBuilder.metadata(JSON.toJSONString(apiRouteDefinition.getMetadata()));

        return bakBuilder.build();
    }

}