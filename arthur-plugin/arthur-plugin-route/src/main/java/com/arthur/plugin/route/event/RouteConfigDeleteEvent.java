package com.arthur.plugin.route.event;

import com.arthur.plugin.route.ArthurDynamicRouteAdapter;
import com.arthur.common.config.ConfigMetaData;
import com.arthur.common.notify.event.Event;
import com.arthur.plugin.route.subscribe.RouteMultiConfigListener;

import java.io.Serial;

/**
 * 路由元配置数据删除事件
 *
 * @author DearYang
 * @date 2022-07-29
 * @see RouteMultiConfigListener
 * @see ArthurDynamicRouteAdapter
 * @since 1.0
 */
public class RouteConfigDeleteEvent extends Event {

    @Serial
	private static final long serialVersionUID = -594260847346108478L;

    public RouteConfigDeleteEvent(ConfigMetaData routeMetaData) {
        super(routeMetaData);
    }

}
