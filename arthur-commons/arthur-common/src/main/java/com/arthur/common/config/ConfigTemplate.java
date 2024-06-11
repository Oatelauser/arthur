package com.arthur.common.config;

/**
 * 配置中心模板操作抽象接口
 *
 * @author DearYang
 * @date 2022-07-22
 * @since 1.0
 */
@SuppressWarnings("unused")
public interface ConfigTemplate {

    /**
     * 订阅配置
     *
     * @param group          组
     * @param dataId         服务名
     * @param namespace      命名空间
     * @param configListener 配置监听器
     * @return 配置监听器
     * @throws Exception 订阅异常
     */
    default Object onSubscribe(String namespace, String group, String dataId, ConfigListener configListener) throws Exception {
        return onSubscribe(new ConfigMetaData(namespace, group, dataId), configListener);
    }

    /**
     * 订阅配置
     *
     * @param group          组
     * @param dataId         服务名
     * @param configListener 配置监听器
     * @return 配置监听器
     * @throws Exception 订阅异常
     */
    default Object onSubscribe(String group, String dataId, ConfigListener configListener) throws Exception {
        return onSubscribe(new ConfigMetaData(group, dataId), configListener);
    }

    /**
     * 订阅配置
     *
     * @param meta           配置的三元组信息
     * @param configListener 配置监听器
     * @return 配置监听器
     * @throws Exception 订阅异常
     */
    Object onSubscribe(ConfigMetaData meta, ConfigListener configListener) throws Exception;

    /**
     * 退订
     *
     * @param group     组
     * @param dataId    服务名
     * @param namespace 命名空间
     * @param listener  配置监听器
     */
    default void unSubscribe(String namespace, String group, String dataId, Object listener) {
        unSubscribe(new ConfigMetaData(namespace, group, dataId), listener);
    }

    /**
     * 退订
     *
     * @param group    组
     * @param dataId   服务名
     * @param listener 配置监听器
     */
    default void unSubscribe(String group, String dataId, Object listener) {
        unSubscribe(new ConfigMetaData(group, dataId), listener);
    }

    /**
     * 退订
     *
     * @param meta     配置的三元组信息
     * @param listener 配置监听器
     */
    void unSubscribe(ConfigMetaData meta, Object listener);

    /**
     * 配置数据推送
     *
     * @param group     组
     * @param dataId    服务名
     * @param config    配置数据
     * @param namespace 命名空间
     * @return 是否成功
     * @throws Exception 连接异常
     */
    default boolean pushConfig(String namespace, String group, String dataId, String config) throws Exception {
        return pushConfig(new ConfigMetaData(namespace, group, dataId), config);
    }

    /**
     * 配置数据推送
     *
     * @param group  组
     * @param dataId 服务名
     * @param config 配置数据
     * @return 是否成功
     * @throws Exception 连接异常
     */
    default boolean pushConfig(String group, String dataId, String config) throws Exception {
        return pushConfig(new ConfigMetaData(group, dataId), config);
    }

    /**
     * 配置数据推送
     *
     * @param meta   配置的三元组信息
     * @param config 配置数据
     * @return 是否成功
     * @throws Exception 连接异常
     */
    boolean pushConfig(ConfigMetaData meta, String config) throws Exception;

    /**
     * 删除配置中心的文件
     *
     * @param group     组
     * @param dataId    服务名
     * @param namespace 命名空间
     * @return 是否成功
     * @throws Exception 连接异常
     */
    default boolean removeConfig(String namespace, String group, String dataId) throws Exception {
        return removeConfig(new ConfigMetaData(namespace, group, dataId));
    }

    /**
     * 删除配置中心的文件
     *
     * @param group  组
     * @param dataId 服务名
     * @return 是否成功
     * @throws Exception 连接异常
     */
    default boolean removeConfig(String group, String dataId) throws Exception {
        return removeConfig(new ConfigMetaData(group, dataId));
    }

    /**
     * 删除配置中心的文件
     *
     * @param meta 配置的三元组信息
     * @return 是否成功
     * @throws Exception 连接异常
     */
    boolean removeConfig(ConfigMetaData meta) throws Exception;

    /**
     * 获取配置中心的配置数据
     *
     * @param group     组
     * @param dataId    服务名
     * @param namespace 命名空间
     * @return 配置数据
     * @throws Exception 连接异常
     */
    default String getConfig(String namespace, String group, String dataId) throws Exception {
        return getConfig(new ConfigMetaData(namespace, group, dataId));
    }

    /**
     * 获取配置中心的配置数据
     *
     * @param group  组
     * @param dataId 服务名
     * @return 配置数据
     * @throws Exception 连接异常
     */
    default String getConfig(String group, String dataId) throws Exception {
        return getConfig(new ConfigMetaData(group, dataId));
    }

    /**
     * 获取配置中心的配置数据
     *
     * @param meta 配置的三元组信息
     * @return 配置数据
     * @throws Exception 连接异常
     */
    String getConfig(ConfigMetaData meta) throws Exception;

    /**
     * 获取配置，并且注册配置监听
     *
     * @param group     组
     * @param dataId    服务名
     * @param namespace 命名空间
     * @param listener  监听器
     * @return 配置数据
     * @throws Exception 配置获取异常或者订阅异常
     */
    default Object getConfigAndSubscribe(String namespace, String group, String dataId, ConfigListener listener) throws Exception {
        return getConfigAndSubscribe(new ConfigMetaData(namespace, group, dataId), listener);
    }

    /**
     * 获取配置，并且注册配置监听
     *
     * @param group    组
     * @param dataId   服务名
     * @param listener 监听器
     * @return 配置数据
     * @throws Exception 配置获取异常或者订阅异常
     */
    default Object getConfigAndSubscribe(String group, String dataId, ConfigListener listener) throws Exception {
        return getConfigAndSubscribe(new ConfigMetaData(group, dataId), listener);
    }

    /**
     * 获取配置，并且注册配置监听
     *
     * @param meta     配置的三元组信息
     * @param listener 监听器
     * @return 配置数据
     * @throws Exception 配置获取异常或者订阅异常
     */
    Object getConfigAndSubscribe(ConfigMetaData meta, ConfigListener listener) throws Exception;

}
