package com.arthur.plugin.route.subscribe;

import com.arthur.boot.event.EventPublisherAware;
import com.arthur.boot.lifecycle.ShutdownHandler;
import com.arthur.boot.lifecycle.ShutdownHandlerAware;
import com.arthur.common.config.ConfigListener;
import com.arthur.common.config.ConfigMetaData;
import com.arthur.common.config.ConfigMetaUtils;
import com.arthur.common.config.ConfigTemplate;
import com.arthur.common.constant.ChangeType;
import com.arthur.common.notify.publisher.EventPublisher;
import com.arthur.common.notify.subscriber.Subscriber;
import com.arthur.common.utils.ConcurrentHashSet;
import com.arthur.common.utils.JsonUtils;
import com.arthur.common.utils.StringUtils;
import com.arthur.plugin.route.constant.GatewayRouteConstants;
import com.arthur.plugin.route.event.RouteConfigDeleteEvent;
import com.arthur.plugin.route.event.RouteRefreshMetaEvent;
import com.arthur.plugin.route.event.RouteRefreshMetaEventObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serial;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 基于路由元数据配置多路配置监听
 *
 * @author DearYang
 * @date 2022-07-25
 * @since 1.0
 */
public class RouteMultiConfigListener implements RouteListener, ConfigListener, Subscriber<RouteRefreshMetaEvent>, EventPublisherAware, ShutdownHandlerAware {

    @Serial
	private static final long serialVersionUID = 229620172660788807L;
    private static final Logger LOG = LoggerFactory.getLogger(RouteMultiConfigListener.class);
    private final ConfigMetaData multiRoute;
    private final ConfigTemplate configTemplate;
    private final AbstractRouteSubscriber routeSubscriber;
    private final Set<ConfigMetaData> routeMetaData = new ConcurrentHashSet<>();
    private final Map<String/*配置三元组*/, /*配置监听器*/Object> configListeners = new ConcurrentHashMap<>();
    private EventPublisher eventPublisher;
    private ShutdownHandler shutdownHandler;

    public RouteMultiConfigListener(ConfigMetaData multiRoute, ConfigTemplate configTemplate, AbstractRouteSubscriber routeSubscriber) {
        this.multiRoute = multiRoute;
        this.configTemplate = configTemplate;
        this.routeSubscriber = routeSubscriber;
    }

    @Override
    public void accept(String metadata, ConfigMetaData configMetaData, Object listener) {
        Set<ConfigMetaData> metaData = JsonUtils.toSet(metadata, ConfigMetaData.class);
        RouteRefreshMetaEventObject eventObject = new RouteRefreshMetaEventObject();
        Set<ConfigMetaData> cachingMetaData = new HashSet<>(routeMetaData);
        for (ConfigMetaData meta : metaData) {
            if (routeMetaData.add(meta)) {
                eventObject.addMetaData(ChangeType.ADD, meta);
            } else {
                cachingMetaData.remove(meta);
            }
        }

        for (ConfigMetaData delete : cachingMetaData) {
            routeMetaData.remove(delete);
            eventObject.addMetaData(ChangeType.DELETE, delete);
        }

        eventPublisher.publish(new RouteRefreshMetaEvent(eventObject));
    }

    @Override
    public void onEvent(RouteRefreshMetaEvent event) {
        Object source = event.getEventObject();
        if (!(source instanceof RouteRefreshMetaEventObject eventObject)) {
            return;
        }

		for (RouteRefreshMetaEventObject.MetaData meta : eventObject.getMetaData()) {
            ConfigMetaData routeMetaData = meta.routeMetaData();
            String key = ConfigMetaUtils.getConfigPosition(routeMetaData);
			switch (meta.type()) {
				case ADD -> addMeta(key, routeMetaData);
				case DELETE -> deleteMeta(key, routeMetaData);
			}
        }
    }

    void addMeta(String key, ConfigMetaData configMeta) {
        if (configListeners.put(key, Boolean.TRUE) != null) {
            return;
        }

        try {
            Object configListener = configTemplate.getConfigAndSubscribe(configMeta, routeSubscriber);
            configListeners.put(key, configListener);
            LOG.info("Route onSubscribe config data: namespace-[{}], group-[{}], dataId-[{}]", configMeta.getNamespace(), configMeta.getGroup(), configMeta.getDataId());
        } catch (Exception e) {
            LOG.error("Config center subscribe error", e);
        }
    }

    void deleteMeta(String key, ConfigMetaData configMeta) {
        Object listener;
        if ((listener = configListeners.remove(key)) == null) {
            return;
        }

        configTemplate.unSubscribe(configMeta, listener);
        LOG.info("Route unSubscribe config data: namespace-[{}], group-[{}], dataId-[{}]", configMeta.getNamespace(), configMeta.getGroup(), configMeta.getDataId());

        // 发布路由元数据数据删除事件
        eventPublisher.publish(new RouteConfigDeleteEvent(configMeta));
    }

    @Override
    public void setEventPublisher(EventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void setShutdownHandler(ShutdownHandler shutdownHandler) {
        this.shutdownHandler = shutdownHandler;
    }

    @Override
    public void start() throws Exception {
        ConfigMetaData configMeta = multiRoute;
        if (configMeta == null) {
            configMeta = new ConfigMetaData(GatewayRouteConstants.META_ROUTE_NAMESPACE, GatewayRouteConstants.META_ROUTE_GROUP, GatewayRouteConstants.META_ROUTE_DATA_ID);
        }

        String group = StringUtils.getOrDefault(configMeta.getGroup(), GatewayRouteConstants.META_ROUTE_GROUP);
        String dataId = StringUtils.getOrDefault(configMeta.getDataId(), GatewayRouteConstants.META_ROUTE_DATA_ID);
        String namespace = StringUtils.getOrDefault(configMeta.getNamespace(), GatewayRouteConstants.META_ROUTE_NAMESPACE);

        Object routeMultiListener = configTemplate.getConfigAndSubscribe(namespace, group, dataId, this);
        shutdownHandler.addShutdownListener(() -> {
            configTemplate.unSubscribe(namespace, group, dataId, routeMultiListener);
            shutdownListeners();
        });

        LOG.info("RouteMeta onSubscribe meta config: namespace: [{}], group: [{}], dataId: [{}]", namespace, group, dataId);
    }

    private void shutdownListeners() {
        if (configListeners.isEmpty()) {
            return;
        }

        for (Map.Entry<String, Object> entry : configListeners.entrySet()) {
            ConfigMetaData configMeta = ConfigMetaUtils.getConfigMeta(entry.getKey());
            configTemplate.unSubscribe(configMeta, entry.getValue());
        }
    }

}
