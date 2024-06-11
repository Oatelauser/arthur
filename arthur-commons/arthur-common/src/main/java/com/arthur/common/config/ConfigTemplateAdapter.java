package com.arthur.common.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 概要描述
 * <p>
 * 详细描述
 *
 * @author DearYang
 * @date 2022-07-25
 * @since 1.0
 */
public abstract class ConfigTemplateAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(ConfigTemplateAdapter.class);

    private final String group;
    private final String dataId;
    private final ConfigTemplate configTemplate;

    public ConfigTemplateAdapter(String group, String dataId, ConfigTemplate configTemplate) {
        this.group = group;
        this.dataId = dataId;
        this.configTemplate = configTemplate;
    }

    public Object onSubscribe(ConfigListener consumer) throws Exception {
        LOG.info("Subscribe {} config from {} server, group={}, dataId={}", getLogMessage(), getConfigType(), group, dataId);
        return configTemplate.onSubscribe(group, dataId, consumer);
    }

    public void unSubscribe(Object listener) {
        LOG.info("Unsubscribe {} config from {} server, group={}, dataId={}", getLogMessage(), getConfigType(), group, dataId);
        configTemplate.unSubscribe(group, dataId, listener);
    }

    public boolean push(String config) throws Exception {
        LOG.info("Push {} config from {} server, group={}, dataId={}", getLogMessage(), getConfigType(), group, dataId);
        return configTemplate.pushConfig(group, dataId, config);
    }

    public boolean remove() throws Exception {
        LOG.info("Remove {} config from {} server, group={}, dataId={}", getLogMessage(), getConfigType(), group, dataId);
        return configTemplate.removeConfig(group, dataId);
    }

    public String getConfig() throws Exception {
        LOG.info("Get {} config from {} server, group={}, dataId={}", getLogMessage(), getConfigType(), group, dataId);
        return configTemplate.getConfig(group, dataId);
    }

    public void close() throws Exception {
        LOG.info("Shutting down config from {} service...", getConfigType());
    }

    public String getGroup() {
        return group;
    }

    public String getDataId() {
        return dataId;
    }

    protected abstract String getLogMessage();

    protected abstract String getConfigType();
}
