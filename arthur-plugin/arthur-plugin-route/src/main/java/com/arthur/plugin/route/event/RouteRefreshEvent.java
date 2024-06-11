package com.arthur.plugin.route.event;

import com.arthur.common.notify.event.Event;
import com.arthur.plugin.route.subscribe.AbstractRouteSubscriber;

import java.io.Serial;

/**
 * 路由刷新事件
 *
 * @author DearYang
 * @date 2022-07-29
 * @see AbstractRouteSubscriber
 * @since 1.0
 */
public class RouteRefreshEvent extends Event {

    @Serial
	private static final long serialVersionUID = -7175197857605897255L;

    public RouteRefreshEvent(RouteRefreshEventObject eventObject) {
        super(eventObject);
    }

}
