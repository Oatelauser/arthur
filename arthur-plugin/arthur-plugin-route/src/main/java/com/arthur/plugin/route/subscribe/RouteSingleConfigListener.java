package com.arthur.plugin.route.subscribe;

import com.arthur.boot.lifecycle.ShutdownHandler;
import com.arthur.boot.lifecycle.ShutdownHandlerAware;
import com.arthur.common.config.ConfigTemplate;
import com.arthur.common.utils.StringUtils;
import com.arthur.plugin.route.autoconfigure.ArthurRouteProperties;
import com.arthur.plugin.route.constant.GatewayRouteConstants;

/**
 * 只监听一个路由配置
 *
 * @author DearYang
 * @date 2022-07-26
 * @since 1.0
 */
public class RouteSingleConfigListener implements RouteListener, ShutdownHandlerAware {

    private final ConfigTemplate configTemplate;
    private final ArthurRouteProperties routeProperties;
    private final AbstractRouteSubscriber routeSubscriber;
    private ShutdownHandler shutdownHandler;

    public RouteSingleConfigListener(ArthurRouteProperties routeProperties, ConfigTemplate configTemplate, AbstractRouteSubscriber routeSubscriber) {
        this.routeProperties = routeProperties;
        this.configTemplate = configTemplate;
        this.routeSubscriber = routeSubscriber;
    }

    @Override
    public void start() throws Exception {
        String namespace = StringUtils.getOrDefault(routeProperties.getNamespace(), GatewayRouteConstants.META_ROUTE_NAMESPACE);
        String group = StringUtils.getOrDefault(routeProperties.getGroup(), GatewayRouteConstants.META_ROUTE_GROUP);
        String dataId = StringUtils.getOrDefault(routeProperties.getDataId(), GatewayRouteConstants.META_ROUTE_DATA_ID);
        Object listener = configTemplate.onSubscribe(namespace, group, dataId, routeSubscriber);
        shutdownHandler.addShutdownListener(() -> configTemplate.unSubscribe(namespace, group, dataId, listener));
    }

    @Override
    public void setShutdownHandler(ShutdownHandler shutdownHandler) {
        this.shutdownHandler = shutdownHandler;
    }

}
