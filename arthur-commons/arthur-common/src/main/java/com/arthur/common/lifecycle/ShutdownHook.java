package com.arthur.common.lifecycle;

/**
 * 优雅关闭监听器
 *
 * @author DearYang
 * @date 2022-08-01
 * @since 1.0
 */
public interface ShutdownHook extends Ordered {

    /**
     * 执行关闭服务的操作
     */
    void shutdown() throws Exception;

}
