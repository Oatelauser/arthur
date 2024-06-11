package com.arthur.boot.lifecycle;

import org.springframework.beans.factory.Aware;

/**
 * 服务初始器自动注册接口
 *
 * @author DearYang
 * @date 2022-08-02
 * @see ServiceInitializer
 * @since 1.0
 */
@SuppressWarnings("unused")
public interface ServiceInitializerAware extends Aware {

    /**
     * 注入{@link ServiceInitializer}实例
     *
     * @param initializer {@link ServiceInitializer}
     */
    void setServiceInitializer(ServiceInitializer initializer);

}
