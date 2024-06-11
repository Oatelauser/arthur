package com.arthur.common.config;

/**
 * 配置监听拦截器
 * 消费者
 *
 * @author DearYang
 * @date 2022-07-22
 * @since 1.0
 */
public interface ConfigListener {

    /**
     * 在监听到数据后，会传入该方法进行拦截
     *
     * @param config   配置数据
     * @param meta     配置三元组
     * @param listener 配置监听器
     * @see ConfigMetaData
     */
    void accept(String config, ConfigMetaData meta, Object listener);

}
