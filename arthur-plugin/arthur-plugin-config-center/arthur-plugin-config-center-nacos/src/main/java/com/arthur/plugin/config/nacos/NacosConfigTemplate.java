package com.arthur.plugin.config.nacos;

import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.AbstractListener;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.arthur.plugin.config.nacos.constant.NacosConstant;
import com.arthur.common.config.ConfigDeletedEvent;
import com.arthur.common.config.ConfigListener;
import com.arthur.common.config.ConfigMetaData;
import com.arthur.common.config.ConfigTemplate;
import com.arthur.common.notify.publisher.EventPublisher;
import com.arthur.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;

import java.util.Objects;

/**
 * Nacos配置中心操作模板
 *
 * @author DearYang
 * @date 2022-07-22
 * @see ConfigTemplate
 * @since 1.0
 */
public class NacosConfigTemplate implements ConfigTemplate, ApplicationContextAware {

    private static final Logger LOG = LoggerFactory.getLogger(NacosConfigTemplate.class);

    private final long timeout;
    private final ConfigService delegate;
    private EventPublisher eventPublisher;

    public NacosConfigTemplate(Long timeout, ConfigService configService) {
        this.timeout = timeout == null ? NacosConstant.NACOS_TIMEOUT : timeout;
        this.delegate = configService;
    }

    @Override
    public Object onSubscribe(ConfigMetaData meta, ConfigListener configListener) throws NacosException {
        Listener listener = createListener(meta, configListener);
        delegate.addListener(meta.getDataId(), meta.getGroup(), listener);
        return listener;
    }

    @Override
    public void unSubscribe(ConfigMetaData meta, Object listener) {
        if (listener == null) {
            return;
        }

        delegate.removeListener(meta.getDataId(), meta.getGroup(), (Listener) listener);
    }

    @Override
    public boolean pushConfig(ConfigMetaData meta, String config) throws Exception {
        return delegate.publishConfig(meta.getDataId(), meta.getGroup(), config);
    }

    @Override
    public boolean removeConfig(ConfigMetaData meta) throws Exception {
        return delegate.removeConfig(meta.getDataId(), meta.getGroup());
    }

    @Override
    public String getConfig(ConfigMetaData meta) throws Exception {
        return delegate.getConfig(meta.getDataId(), meta.getGroup(), timeout);
    }

    @Override
    public Listener getConfigAndSubscribe(ConfigMetaData meta, ConfigListener configListener) throws Exception {
        Listener listener = createListener(meta, configListener);
        String config = delegate.getConfigAndSignListener(meta.getDataId(), meta.getGroup(), timeout, listener);
        if (!StringUtils.isEmpty(config)) {
            configListener.accept(config, meta, listener);
        }

        return listener;
    }

    public Listener createListener(ConfigMetaData meta, ConfigListener configListener) {
        return new AbstractListener() {
            @Override
            public void receiveConfigInfo(String configInfo) {
                // 传过来的数据为null，说明删除配置了
                if (configInfo == null) {
                    unSubscribe(meta, this);
                    if (Objects.nonNull(eventPublisher)) {
                        eventPublisher.publish(new ConfigDeletedEvent(meta));
                    }
                    LOG.info("Config unSubscribe: namespace: [{}], group: [{}], dataId: [{}], because config is deleted", meta.getNamespace(), meta.getGroup(), meta.getDataId());
                }

                configListener.accept(configInfo, meta, this);
            }

        };
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.eventPublisher = applicationContext.getBeanProvider(EventPublisher.class).getIfAvailable();
    }

}
