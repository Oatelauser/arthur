package com.arthur.boot.lifecycle;

import com.arthur.common.lifecycle.ShutdownHook;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.SmartLifecycle;

import java.util.List;

/**
 * 标准实现优雅关闭
 *
 * @author DearYang
 * @date 2022-08-01
 * @since 1.0
 */
public class SmartGracefulShutdownHandler extends AbstractShutdownHandler implements SmartLifecycle {

    private volatile boolean running = false;

    public SmartGracefulShutdownHandler(ObjectProvider<List<ShutdownHook>> provider) {
        super(provider);
    }

    @Override
    public void start() {
        this.running = true;
    }

    @Override
    public void stop() {
        this.invokeShutdown();
        this.running = false;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public int getPhase() {
        return SmartLifecycle.DEFAULT_PHASE - 1;
    }

}
