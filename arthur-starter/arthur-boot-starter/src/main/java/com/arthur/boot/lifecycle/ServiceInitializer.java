package com.arthur.boot.lifecycle;

import com.arthur.common.lifecycle.ServiceStartupEvent;
import com.arthur.common.lifecycle.InitializeListener;

import java.util.List;

/**
 * 服务启动初始化接口
 * <p>
 * 1.动态加载{@link InitializeListener}
 * 2.发出{@link ServiceStartupEvent}事件
 * 3.自定义排序执行
 *
 * @author DearYang
 * @date 2022-08-01
 * @see InitializeListener
 * @since 1.0
 */
@SuppressWarnings("unused")
public interface ServiceInitializer {

    /**
     * 执行注册的{@link InitializeListener}
     */
    void invokeStart();

    /**
     * 添加{@link InitializeListener}
     *
     * @param listener {@link InitializeListener}
     */
    void addInitializeListener(InitializeListener listener);

    /**
     * 获取所有的{@link InitializeListener}
     *
     * @return {@link InitializeListener}
     */
    List<InitializeListener> getInitializeListener();

}
