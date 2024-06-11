package com.arthur.plugin.route;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 网关动态路由实现
 *
 * @author DearYang
 * @date 2022-07-17
 * @since 1.0
 */
public class ArthurDynamicRoute implements GatewayDynamicRoute {

    private static final Logger LOG = LoggerFactory.getLogger(ArthurDynamicRoute.class);

    /**
     * 写路由定义
     */
    private final RouteDefinitionWriter routeDefinitionWriter;

    /**
     * 获取路由定义
     */
    private final RouteDefinitionLocator routeDefinitionLocator;

    /**
     * id
     */
    public ArthurDynamicRoute(RouteDefinitionWriter writer, RouteDefinitionLocator locator) {
        this.routeDefinitionWriter = writer;
        this.routeDefinitionLocator = locator;
    }

    @Override
    public List<RouteDefinition> getAllRouteDefinition() {
        return routeDefinitionLocator.getRouteDefinitions().buffer().blockFirst();
    }

    @Override
    public RouteDefinition getRouteDefinition(String routeId) {
        return getAllRouteDefinition().stream()
                .filter(definition -> routeId.equals(definition.getId())).findAny()
                .orElse(null);
    }

    /**
     * 增加路由定义
     */
    @Override
    public void add(RouteDefinition definition) {
        // 保存路由配置并发布
        routeDefinitionWriter.save(Mono.just(definition)).subscribe();
        LOG.info("gateway add route: [{}]", definition);
    }

    @Override
    public void update(RouteDefinition definition) {
        routeDefinitionWriter.delete(Mono.just(definition.getId())).subscribe();
        routeDefinitionWriter.save(Mono.just(definition)).subscribe();
        LOG.info("gateway update route: [{}]", definition);
    }

    @Override
    public void delete(RouteDefinition definition) {
        delete(definition.getId());
    }

    @Override
    public void delete(String routeId) {
        routeDefinitionWriter.delete(Mono.just(routeId)).subscribe();
        LOG.info("gateway delete route id: [{}]", routeId);
    }

}
