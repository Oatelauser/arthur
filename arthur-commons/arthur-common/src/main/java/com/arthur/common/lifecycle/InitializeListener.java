package com.arthur.common.lifecycle;

/**
 * 服务初始化监听器
 *
 * @author DearYang
 * @date 2022-08-02
 * @since 1.0
 */
public interface InitializeListener extends Ordered {

    /**
     * 初始化操作
     *
     * @throws Exception 初始化异常
     */
    void start() throws Exception;

}
