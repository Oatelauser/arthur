package com.broadtech.arthur.admin.route.entity.vo;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.broadtech.arthur.admin.route.entity.dto.RouteDefinitionDTO;
import com.broadtech.arthur.admin.route.entity.po.RouteDefinition;
import com.google.common.base.Preconditions;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 路由表
 *
 * @author Machenike
 * @TableName route
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RouteDefinitionV0 implements Serializable {

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
    private Map<String, Object> metadata;

    @NotNull
    /**
     * 断言集合
     */
    private List<Object> predicates;

    /**
     * 过滤器集合
     */
    private List<Object> filters;

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




    public static RouteDefinitionDTO assembleApiRouteDefinition(RouteDefinition apiRouteDefinition) {
        RouteDefinitionDTO.RouteDefinitionDTOBuilder bakBuilder = RouteDefinitionDTO.builder()
                .id(apiRouteDefinition.getId())
                .routeId(apiRouteDefinition.getRouteId())
                .routeOrder(apiRouteDefinition.getRouteOrder())
                .connectTimeout(apiRouteDefinition.getConnectTimeout())
                .responseTimeout(apiRouteDefinition.getResponseTimeout())
                .groupId(apiRouteDefinition.getGroupId())
                .uri(apiRouteDefinition.getUri());

        return assembleApiRouteDefinition0(bakBuilder, apiRouteDefinition);
    }

    private static RouteDefinitionDTO assembleApiRouteDefinition0(RouteDefinitionDTO.RouteDefinitionDTOBuilder bakBuilder, RouteDefinition apiRouteDefinition) {
        String predicates = apiRouteDefinition.getPredicates();
        List<Object> predicateLst = StrUtil.isBlank(predicates) ? Collections.emptyList() : Preconditions.checkNotNull(
                JSON.parseObject(predicates, List.class));
        bakBuilder.predicates(predicateLst);

        String filters = apiRouteDefinition.getFilters();
        List<Object> filterLst = StrUtil.isBlank(filters) ? Collections.emptyList() : Preconditions.checkNotNull(
                JSON.parseObject(filters, List.class));
        bakBuilder.filters(filterLst);

        String metadataStr = apiRouteDefinition.getMetadata();
        Map<String, Object> metadata = StrUtil.isBlank(metadataStr) ? MapUtil.empty() : Preconditions.checkNotNull(
                JSON.parseObject(metadataStr, new TypeReference<Map<String, Object>>() {
                }));
        bakBuilder.metadata(metadata);
        return bakBuilder.build();
    }

}