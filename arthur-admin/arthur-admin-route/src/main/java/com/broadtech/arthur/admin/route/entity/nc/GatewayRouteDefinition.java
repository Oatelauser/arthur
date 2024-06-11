package com.broadtech.arthur.admin.route.entity.nc;

import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson2.JSON;
import com.broadtech.arthur.admin.route.entity.dto.RouteDefinitionDTO;
import com.broadtech.arthur.admin.common.detect.CheckAble;
import com.google.common.base.Preconditions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author Machenike
 * @Date 2022/7/20
 * @Version 1.0.0
 * @Description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class GatewayRouteDefinition {

    @NotNull
    private String id;

    @NotNull
    private String uri;

    private Map<String, Object> metadata = new HashMap<>();

    private int order = 0;

    @NotEmpty
    @Valid
    private List<GatewayPredicateDefinition> predicates = new ArrayList<>();


    private List<GatewayFilterDefinition> filters = new ArrayList<>();




    public static GatewayRouteDefinition assembleRouteDefinition(RouteDefinitionDTO routeDefinitionDTO) {
        GatewayRouteDefinition gatewayRouteDefinition = new GatewayRouteDefinition();
        assembleRouteDefinition0(routeDefinitionDTO, gatewayRouteDefinition);
        assembleRouteDefinition1(routeDefinitionDTO, gatewayRouteDefinition);
        return gatewayRouteDefinition;
    }

    private static void assembleRouteDefinition0(RouteDefinitionDTO src, GatewayRouteDefinition dst) {
        dst.setId(src.getRouteId());
        dst.setUri(src.getUri());
        dst.setOrder(src.getRouteOrder());
    }



    /***
     * str to obj
     * @param src
     * @param dst
     */


    private static void assembleRouteDefinition1(RouteDefinitionDTO src, GatewayRouteDefinition dst) {
        assembleMetadata(src, dst);
        assemblePredicate(src, dst);
        assembleFilter(src, dst);
    }

    private static void assembleMetadata(RouteDefinitionDTO src, GatewayRouteDefinition dst) {;
        HashMap<String, Object> map = MapUtil.newHashMap(1);
        map.put("response-timeout", src.getResponseTimeout());
        map.put("connect-timeout", src.getConnectTimeout());
        dst.setMetadata(map);
    }

    private static void assemblePredicate(RouteDefinitionDTO src, GatewayRouteDefinition dst) {
        List<Object> list = Preconditions.checkNotNull(src.getPredicates());
        List<GatewayPredicateDefinition> predicateDefinitions = list.stream()
                .map(JSON::toJSONString)
                .map(o -> JSON.parseObject(o, GatewayPredicateDefinition.class))
                .collect(Collectors.toList());
        dst.setPredicates(predicateDefinitions);
    }

    private static void assembleFilter(RouteDefinitionDTO src, GatewayRouteDefinition dst) {
        List<Object> list = Preconditions.checkNotNull(src.getFilters());
        List<GatewayFilterDefinition> predicateDefinitions = list.stream()
                .map(JSON::toJSONString)
                .map(o -> JSON.parseObject(o, GatewayFilterDefinition.class))
                .collect(Collectors.toList());
        dst.setFilters(predicateDefinitions);
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Validated
    public static class GatewayPredicateDefinition extends CheckAble {
        @NotNull
        private String name;

        @NotEmpty
        @Valid
        private Map<String, String> args = new LinkedHashMap<>();


    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Validated
    public static class GatewayFilterDefinition extends CheckAble  {
        @NotNull
        private String name;
        private Map<String, String> args = new LinkedHashMap<>();


    }




}
