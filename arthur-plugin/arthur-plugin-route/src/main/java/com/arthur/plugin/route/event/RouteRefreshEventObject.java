package com.arthur.plugin.route.event;

import com.arthur.common.constant.ChangeType;
import org.springframework.cloud.gateway.route.RouteDefinition;

import java.util.ArrayList;
import java.util.List;

/**
 * 路由变更事件源
 *
 * @author DearYang
 * @date 2022-07-25
 * @see RouteRefreshEvent
 * @since 1.0
 */
public class RouteRefreshEventObject {

    private final List<Route> routes = new ArrayList<>();

    public void addRoute(final ChangeType type, final RouteDefinition definition, final String configMeta) {
        routes.add(new Route(type, definition, configMeta));
    }

    public List<Route> getRoutes() {
        return routes;
    }

	public record Route(ChangeType type, RouteDefinition route, String configMeta) {

	}

}
