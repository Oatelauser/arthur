package com.arthur.plugin.route;

import com.arthur.boot.event.annotation.ArthurEventListener;
import com.arthur.common.config.ConfigMetaData;
import com.arthur.common.config.ConfigMetaUtils;
import com.arthur.common.utils.ConcurrentHashSet;
import com.arthur.plugin.route.core.GatewayRouteDefinitions;
import com.arthur.plugin.route.event.RouteConfigDeleteEvent;
import com.arthur.plugin.route.event.RouteRefreshEventObject;
import com.arthur.plugin.route.subscribe.AbstractRouteSubscriber;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;

import java.io.Serial;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 动态路由适配器
 * <p>
 * 1.监听路由变更事件
 * 2.监听路由配置元数据变更事件
 * 3.适配动态路由api
 *
 * @author DearYang
 * @date 2022-07-29
 * @since 1.0
 */
public class ArthurDynamicRouteAdapter extends AbstractRouteSubscriber implements GatewayDynamicRoute {

    @Serial
	private static final long serialVersionUID = -7584551562982440925L;

    // 路由配置文件删除或者路由元数据删除的时候，需要同步删除路由
    private final Map<String/*配置三元组*/, /*路由ID*/Set<String>> configMetaCache = new ConcurrentHashMap<>();

    public ArthurDynamicRouteAdapter(GatewayDynamicRoute dynamicRoute, GatewayRouteDefinitions gatewayRouteDefinitions) {
        super(dynamicRoute, gatewayRouteDefinitions);
    }

    @Override
    public List<RouteDefinition> getAllRouteDefinition() {
        return List.copyOf(routeIdCache.values());
    }

    @Override
    public RouteDefinition getRouteDefinition(String routeId) {
        return routeIdCache.get(routeId);
    }

    @Override
    public void add(RouteDefinition definition) {
        if (routeIdCache.put(definition.getId(), definition) == null) {
            delegate.add(definition);
        }
    }

    @Override
    public void update(RouteDefinition definition) {
        if (routeIdCache.remove(definition.getId()) != null) {
            routeIdCache.put(definition.getId(), definition);
            delegate.update(definition);
        }
    }

    @Override
    public void delete(RouteDefinition definition) {
        delete(definition.getId());
    }

    @Override
    public void delete(String routeId) {
        if (routeIdCache.remove(routeId) != null) {
            delegate.delete(routeId);
        }
    }

    @Override
    protected void addRoute(RouteRefreshEventObject.Route route) {
        RouteDefinition definition = route.route();
        Set<String> routeIds = configMetaCache.computeIfAbsent(route.configMeta(), key -> new ConcurrentHashSet<>());
        if (routeIds.add(definition.getId())) {
            add(definition);
        }
    }

    @Override
    protected void updateRoute(RouteRefreshEventObject.Route route) {
        RouteDefinition definition = route.route();
        update(definition);
    }

    @Override
    protected void deleteRoute(RouteRefreshEventObject.Route route) {
        RouteDefinition definition = route.route();
        Set<String> routeIds = configMetaCache.get(route.configMeta());
        if (routeIds == null) {
            return;
        }

        if (routeIds.isEmpty()) {
            configMetaCache.remove(route.configMeta());
            return;
        }

        if (routeIds.remove(definition.getId())) {
            delete(definition);
        }
    }

    /**
     * 监听路由元数据删除事件
     *
     * @param event {@link RouteConfigDeleteEvent}
     */
    @ArthurEventListener
    @SuppressWarnings("unused")
    public void onRouteConfigDeleteEvent(RouteConfigDeleteEvent event) {
        ConfigMetaData eventObject = (ConfigMetaData) event.getEventObject();
        String configPosition = ConfigMetaUtils.getConfigPosition(eventObject);
        Set<String> routeIds;
        if ((routeIds = configMetaCache.remove(configPosition)) == null) {
            return;
        }

        routeIds.forEach(this::delete);
        // 通知gateway路由变更
        publisher.publishEvent(new RefreshRoutesEvent(this));
    }

}
