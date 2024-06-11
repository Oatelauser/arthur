package com.arthur.plugin.route.event;

import com.arthur.common.notify.event.Event;

import java.io.Serial;

/**
 * 刷新路由的元数据信息
 *
 * @author DearYang
 * @date 2022-07-25
 * @since 1.0
 */
public class RouteRefreshMetaEvent extends Event {

    @Serial
	private static final long serialVersionUID = -8747648055019202346L;

    public RouteRefreshMetaEvent(RouteRefreshMetaEventObject eventObject) {
        super(eventObject);
    }

}
