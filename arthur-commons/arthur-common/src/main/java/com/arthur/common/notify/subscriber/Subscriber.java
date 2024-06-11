package com.arthur.common.notify.subscriber;

import com.arthur.common.notify.event.Event;
import com.arthur.common.utils.SerializableLambda;

import java.util.concurrent.Executor;

/**
 * An abstract subscriber class for subscriber interface.
 */
public interface Subscriber<T extends Event> extends SerializableLambda {

    /**
     * Event callback.
     *
     * @param event {@link Event}
     */
    void onEvent(T event);

    /**
     * It is up to the listener to determine whether the callback is asynchronous or synchronous.
     *
     * @return {@link Executor}
     */
    default Executor getExecutor() {
        return null;
    }

    /**
     * Whether to ignore expired events.
     *
     * @return default value is {@link Boolean#FALSE}
     */
    default boolean ignoreExpireEvent() {
        return false;
    }

}
