package com.arthur.plugin.route.subscribe;

import com.arthur.boot.event.EventPublisherAware;
import com.arthur.common.config.ConfigListener;
import com.arthur.common.config.ConfigMetaData;
import com.arthur.common.config.ConfigMetaUtils;
import com.arthur.common.constant.ChangeType;
import com.arthur.common.lifecycle.InitializeListener;
import com.arthur.common.notify.publisher.EventPublisher;
import com.arthur.common.notify.subscriber.Subscriber;
import com.arthur.common.utils.JsonUtils;
import com.arthur.plugin.route.GatewayDynamicRoute;
import com.arthur.plugin.route.core.GatewayRouteDefinition;
import com.arthur.plugin.route.core.GatewayRouteDefinitions;
import com.arthur.plugin.route.event.RouteRefreshEvent;
import com.arthur.plugin.route.event.RouteRefreshEventObject;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.lang.NonNull;
import org.springframework.util.CollectionUtils;

import java.io.Serial;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 路由数据订阅者抽象基类
 *
 * @author DearYang
 * @date 2022-07-29
 * @since 1.0
 */
public abstract class AbstractRouteSubscriber implements ConfigListener, Subscriber<RouteRefreshEvent>, InitializeListener, EventPublisherAware, ApplicationEventPublisherAware {

    @Serial
	private static final long serialVersionUID = 7346458053833168212L;
    protected final GatewayDynamicRoute delegate;
    protected final Map<String/*routeId*/, RouteDefinition> routeIdCache = new ConcurrentHashMap<>();
	private final GatewayRouteDefinitions gatewayRouteDefinitions;
	protected EventPublisher eventPublisher;
    protected ApplicationEventPublisher publisher;

    public AbstractRouteSubscriber(GatewayDynamicRoute dynamicRoute, GatewayRouteDefinitions gatewayRouteDefinitions) {
        this.delegate = dynamicRoute;
		this.gatewayRouteDefinitions = gatewayRouteDefinitions;
    }

    @Override
    public void accept(String config, ConfigMetaData meta, Object listener) {
        Map<String, GatewayRouteDefinition> routes = JsonUtils.toMap(config, String.class, GatewayRouteDefinition.class);
		routes.values().forEach(gatewayRouteDefinitions::asyncRouteMetadata);

        String configMeta = ConfigMetaUtils.getConfigPosition(meta);
        RouteRefreshEventObject routeRefreshEventObject = createRouteEventObject(routes.values(), configMeta);
        eventPublisher.publish(new RouteRefreshEvent(routeRefreshEventObject));
    }

    private RouteRefreshEventObject createRouteEventObject(Collection<GatewayRouteDefinition> routes, String configMeta) {
        RouteRefreshEventObject routeRefreshEventObject = new RouteRefreshEventObject();
        Set<String> cacheRouteIds = new HashSet<>(routeIdCache.keySet());
        for (GatewayRouteDefinition route : routes) {
            String routeId = route.getId();
            cacheRouteIds.remove(routeId);
            RouteDefinition cacheRoute = routeIdCache.get(routeId);
            if (cacheRoute == null) {
                routeRefreshEventObject.addRoute(ChangeType.ADD, route, configMeta);
            } else if (!route.equals(cacheRoute)) {
                routeRefreshEventObject.addRoute(ChangeType.UPDATE, route, configMeta);
            }
        }

        cacheRouteIds.forEach(routeId -> routeRefreshEventObject.addRoute(ChangeType.DELETE, routeIdCache.get(routeId), configMeta));
        return routeRefreshEventObject;
    }

    @SuppressWarnings("unused")
    public void onEvent(RouteRefreshEvent event) {
        Object eventSource = event.getEventObject();
        if (!(eventSource instanceof RouteRefreshEventObject eventObject)) {
            return;
        }

		List<RouteRefreshEventObject.Route> routes = eventObject.getRoutes();
        if (CollectionUtils.isEmpty(routes)) {
            return;
        }

        for (RouteRefreshEventObject.Route route : routes) {
			switch (route.type()) {
				case DELETE -> deleteRoute(route);
				case UPDATE -> updateRoute(route);
				case ADD -> addRoute(route);
			}
        }

        // 通知gateway路由变更
        publisher.publishEvent(new RefreshRoutesEvent(this));
    }

    /**
     * 路由删除事件
     *
     * @param route {@link RouteRefreshEventObject.Route}
     */
    protected abstract void deleteRoute(RouteRefreshEventObject.Route route);

    /**
     * 路由更新事件
     *
     * @param route {@link RouteRefreshEventObject.Route}
     */
    protected abstract void updateRoute(RouteRefreshEventObject.Route route);

    /**
     * 路由新增事件
     *
     * @param route {@link RouteRefreshEventObject.Route}
     */
    protected abstract void addRoute(RouteRefreshEventObject.Route route);

    @Override
    public void setEventPublisher(EventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void setApplicationEventPublisher(@NonNull ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void start() {
        List<RouteDefinition> definitions = delegate.getAllRouteDefinition();
        if (CollectionUtils.isEmpty(definitions)) {
            definitions.forEach(definition -> routeIdCache.put(definition.getId(), definition));
        }
    }

}
