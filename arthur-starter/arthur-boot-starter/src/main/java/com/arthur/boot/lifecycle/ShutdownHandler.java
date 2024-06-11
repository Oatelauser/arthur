package com.arthur.boot.lifecycle;

import com.arthur.common.lifecycle.ServiceShutdownEvent;
import com.arthur.common.lifecycle.ShutdownHook;

import java.util.List;

/**
 * 服务优雅关闭执行器
 * <p>
 * 1.动态加载{@link ShutdownHook}实现，在服务关闭的时候触发实现类
 * 2.发出{@link ServiceShutdownEvent}事件，可以监听该事件
 * 3.自定义排序执行
 *
 * @author DearYang
 * @date 2022-08-01
 * @see ShutdownHook
 * @since 1.0
 */
@SuppressWarnings("unused")
public interface ShutdownHandler {

    /**
     * 添加服务下线监听器
     *
     * @param shutdownHook {@link ShutdownHook}
     */
    void addShutdownListener(ShutdownHook shutdownHook);

    /**
     * 获取所有的监听器{@link ShutdownHandler}
     *
     * @return {@link ShutdownHook}
     */
    List<ShutdownHook> getShutdownListeners();

    /**
     * 回调所有的监听器{@link ShutdownHandler}
     */
    void invokeShutdown();

}
