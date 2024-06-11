package com.arthur.plugin.route.event;

import com.arthur.common.config.ConfigMetaData;
import com.arthur.common.constant.ChangeType;

import java.util.ArrayList;
import java.util.List;

/**
 * 路由元数据刷新具体配置信息
 *
 * @author DearYang
 * @date 2022-07-25
 * @see RouteRefreshMetaEvent
 * @since 1.0
 */
public class RouteRefreshMetaEventObject {

    private final List<MetaData> metaData = new ArrayList<>();

    public void addMetaData(ChangeType type, ConfigMetaData routeMetaData) {
        metaData.add(new MetaData(type, routeMetaData));
    }

    public List<MetaData> getMetaData() {
        return metaData;
    }

	public record MetaData(ChangeType type, ConfigMetaData routeMetaData) {
	}

}
