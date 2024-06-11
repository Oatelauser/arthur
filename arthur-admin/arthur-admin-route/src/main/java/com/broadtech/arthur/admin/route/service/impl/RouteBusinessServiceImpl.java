package com.broadtech.arthur.admin.route.service.impl;

import com.broadtech.arthur.admin.common.annotation.CheckRoute;
import com.broadtech.arthur.admin.common.execption.BaseException;
import com.broadtech.arthur.admin.common.respones.ResponseVo;
import com.broadtech.arthur.admin.common.service.IdempotentService;
import com.broadtech.arthur.admin.group.entity.po.Group;
import com.broadtech.arthur.admin.group.repository.GroupRepositoryService;
import com.broadtech.arthur.admin.route.entity.dto.RouteDefinitionDTO;
import com.broadtech.arthur.admin.route.entity.nc.GatewayRouteDefinition;
import com.broadtech.arthur.admin.route.entity.po.RouteDefinition;
import com.broadtech.arthur.admin.route.entity.vo.RouteDefinitionV0;
import com.broadtech.arthur.admin.route.repository.RouteRepositoryService;
import com.broadtech.arthur.admin.route.service.RouteBusinessService;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/9/27
 */
@Service
@Slf4j
public class RouteBusinessServiceImpl implements RouteBusinessService {
    @Resource
    IdempotentService      idempotentService;
    @Resource
    RouteRepositoryService routeRepositoryService;
    @Resource
    GroupRepositoryService groupRepositoryService;


    @Override
    @CheckRoute
    @Transactional(rollbackFor = RuntimeException.class)
    public ResponseVo createRoute(RouteDefinitionDTO routeDto) {

        Preconditions.checkState(
                !idempotentService.checkDupSubmit(routeDto.getRouteId() + "createRoute"),
                BaseException.RepeatedSubmitException.of(routeDto.getRouteId()));

        RouteDefinition        routeDefinition        = RouteDefinition.assembleApiRouteDefinition(routeDto);
        GatewayRouteDefinition gatewayRouteDefinition = GatewayRouteDefinition.assembleRouteDefinition(routeDto);
        Group                  group                  = Preconditions.checkNotNull(groupRepositoryService.queryRouteGroup(routeDefinition.getGroupId()).orNull());

        routeRepositoryService.createRoute(gatewayRouteDefinition, routeDefinition, Group.assembleConfigMetaData(group));
        return ResponseVo.SUCCESS(routeDto.getId());



    }


    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public ResponseVo delRoute(String id, String gId) {
        Group group = Preconditions.checkNotNull(groupRepositoryService.queryRouteGroup(gId).orNull());
            return ResponseVo.SUCCESS(routeRepositoryService.delRoute(id, Group.assembleConfigMetaData(group)));
    }


    @Override
    @CheckRoute
    public ResponseVo updateRoute(RouteDefinitionDTO routeDto) {

        Preconditions.checkState(
                !idempotentService.checkDupSubmit(routeDto.getRouteId() + "createRoute"),
                BaseException.RepeatedSubmitException.of(routeDto.getRouteId()));

        RouteDefinition        routeDefinition        = RouteDefinition.assembleApiRouteDefinition(routeDto);
        GatewayRouteDefinition gatewayRouteDefinition = GatewayRouteDefinition.assembleRouteDefinition(routeDto);
        Group                  group                  = Preconditions.checkNotNull(groupRepositoryService.queryRouteGroup(routeDefinition.getGroupId()).orNull());
            return ResponseVo.SUCCESS(routeRepositoryService.updateRoute(gatewayRouteDefinition, routeDefinition, Group.assembleConfigMetaData(group)));
    }


    @Override
    public ResponseVo queryRoute(String id) {
        Optional<RouteDefinition> routeDefinitionOptional = routeRepositoryService.queryRoute(id);
        return routeDefinitionOptional.orNull() == null ? ResponseVo.SUCCESS(null) : ResponseVo.SUCCESS(routeDefinitionOptional.transform(
                new Function<RouteDefinition, RouteDefinitionV0>() {
                    @Override
                    public @Nullable RouteDefinitionV0 apply(@Nullable RouteDefinition routeDefinition) {
                        RouteDefinitionV0 routeDefinitionV0 = new RouteDefinitionV0();
                        BeanUtils.copyProperties(routeDefinition, routeDefinitionV0);
                        return routeDefinitionV0;
                    }
                }).get());
    }

    @Override
    public ResponseVo queryRouteUnderGroup(String gId) {
        return ResponseVo.SUCCESS(routeRepositoryService.queryRouteUnderGroup(gId));
    }
}
