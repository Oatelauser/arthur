package com.broadtech.arthur.admin.route.entity.dto;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.annotation.TableField;
import com.broadtech.arthur.admin.route.entity.po.RouteDefinition;
import com.google.common.base.Preconditions;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 路由表
 *
 * @author Machenike
 */

@Data
@Builder
public class RouteDefinitionDTO implements Serializable {
    /**
     *
     */

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

    @Min(1)
    private Integer responseTimeout;
    @Min(1)
    private Integer connectTimeout;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        RouteDefinitionDTO other = (RouteDefinitionDTO) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getGroupId() == null ? other.getGroupId() == null : this.getGroupId().equals(other.getGroupId()))
                && (this.getUri() == null ? other.getUri() == null : this.getUri().equals(other.getUri()))
                && (this.getMetadata() == null ? other.getMetadata() == null : this.getMetadata().equals(other.getMetadata()))
                && (this.getPredicates() == null ? other.getPredicates() == null : this.getPredicates().equals(
                other.getPredicates()))
                && (this.getFilters() == null ? other.getFilters() == null : this.getFilters().equals(other.getFilters()))
                && (this.getRouteOrder() == null ? other.getRouteOrder() == null : this.getRouteOrder().equals(
                other.getRouteOrder()))
                && (this.getRouteId() == null ? other.getRouteId() == null : this.getRouteId().equals(other.getRouteId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getGroupId() == null) ? 0 : getGroupId().hashCode());
        result = prime * result + ((getUri() == null) ? 0 : getUri().hashCode());
        result = prime * result + ((getMetadata() == null) ? 0 : getMetadata().hashCode());
        result = prime * result + ((getPredicates() == null) ? 0 : getPredicates().hashCode());
        result = prime * result + ((getFilters() == null) ? 0 : getFilters().hashCode());
        result = prime * result + ((getRouteOrder() == null) ? 0 : getRouteOrder().hashCode());
        result = prime * result + ((getRouteId() == null) ? 0 : getRouteId().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", groupId=").append(groupId);
        sb.append(", uri=").append(uri);
        sb.append(", metadata=").append(metadata);
        sb.append(", predicates=").append(predicates);
        sb.append(", filters=").append(filters);
        sb.append(", routeOrder=").append(routeOrder);
        sb.append(", routeId=").append(routeId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

    public static RouteDefinitionDTO assembleApiRouteDefinition(RouteDefinition apiRouteDefinition) {
        RouteDefinitionDTOBuilder bakBuilder = RouteDefinitionDTO.builder()
                .id(apiRouteDefinition.getId())
                .routeId(apiRouteDefinition.getRouteId())
                .routeOrder(apiRouteDefinition.getRouteOrder())
                .connectTimeout(apiRouteDefinition.getConnectTimeout())
                .responseTimeout(apiRouteDefinition.getResponseTimeout())
                .groupId(apiRouteDefinition.getGroupId())
                .uri(apiRouteDefinition.getUri());

        return assembleApiRouteDefinition0(bakBuilder, apiRouteDefinition);
    }

    private static RouteDefinitionDTO assembleApiRouteDefinition0(RouteDefinitionDTOBuilder bakBuilder, RouteDefinition apiRouteDefinition) {
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