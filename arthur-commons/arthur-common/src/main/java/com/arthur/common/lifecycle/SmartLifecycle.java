package com.arthur.common.lifecycle;

/**
 * 生命周期接口
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-03-03
 * @since 1.0
 */
public interface SmartLifecycle extends InitializeListener, ShutdownHook, Ordered {
}
