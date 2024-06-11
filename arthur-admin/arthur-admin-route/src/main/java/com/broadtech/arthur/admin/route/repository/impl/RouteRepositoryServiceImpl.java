package com.broadtech.arthur.admin.route.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.broadtech.arthur.admin.route.entity.nc.GatewayRouteDefinition;
import com.broadtech.arthur.admin.route.entity.po.RouteDefinition;
import com.broadtech.arthur.admin.route.repository.RouteRepositoryService;
import com.broadtech.arthur.admin.route.repository.RouteService;
import com.broadtech.arthur.common.config.ConfigMetaData;
import com.broadtech.arthur.common.config.ConfigTemplate;
import com.google.common.base.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/9/27
 */
@Service
@Slf4j
public class RouteRepositoryServiceImpl implements RouteRepositoryService {


    @Resource
    ConfigTemplate template;


    @Resource
    NcRouteServiceImpl ncRouteService;

    @Resource
    RouteService routeService;



    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean createRoute(GatewayRouteDefinition gatewayRouteDefinition, RouteDefinition routeDefinition, ConfigMetaData configMetaData) {



        //insert db
        return routeService.save(routeDefinition)&&ncRouteService.save(template
                , configMetaData
                , routeDefinition.getId()
                , gatewayRouteDefinition);

    }

    @CacheEvict(value = "route_cache", key = "#id")
    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public boolean delRoute(String id, ConfigMetaData configMetaData) {

        return  routeService.removeById(id)&&
        ncRouteService.removeById(template, configMetaData, id);

    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    @Caching(evict = {@CacheEvict(value = "route_cache", key = "#id"), @CacheEvict(value = "route_cache", key = "#gatewayRouteDefinition.id")})
    public boolean updateRoute(GatewayRouteDefinition gatewayRouteDefinition, RouteDefinition routeDefinition, ConfigMetaData configMetaData) {


        return   routeService.updateById(routeDefinition)&&
        //insert nc
        ncRouteService.save(template
                , configMetaData
                , routeDefinition.getId()
                , gatewayRouteDefinition);




    }

    @Override
    @Cacheable(value = "route_cache", key = "#id")
    public Optional<RouteDefinition> queryRoute(String id) {


        RouteDefinition route = routeService.getById(id);
        return route==null?Optional.absent():Optional.of(route);

    }

    @Cacheable(value = "route_cache", key = "#gId")
    @Override
    public List<RouteDefinition> queryRouteUnderGroup(String gId) {
        QueryWrapper<RouteDefinition> queryWrapper = new QueryWrapper<>();
        List<RouteDefinition>         routes       = routeService.list(queryWrapper.lambda().eq(RouteDefinition::getGroupId, gId));
        return routes;
    }
}
