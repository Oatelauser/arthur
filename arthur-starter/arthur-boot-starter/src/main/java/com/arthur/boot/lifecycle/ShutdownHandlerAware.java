package com.arthur.boot.lifecycle;

import org.springframework.beans.factory.Aware;

/**
 * automatic injection {@link ShutdownHandler} interface
 *
 * @author DearYang
 * @date 2022-08-02
 * @since 1.0
 */
public interface ShutdownHandlerAware extends Aware {

    /**
     * set {@link ShutdownHandler} implement object instance
     *
     * @param shutdownHandler {@link ShutdownHandler}
     */
    void setShutdownHandler(ShutdownHandler shutdownHandler);

}
