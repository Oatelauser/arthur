package com.arthur.common.notify.subscriber;

import com.arthur.common.notify.event.Event;

/**
 * Subscribers to multiple events can be listened to.
 */
public interface SmartSubscriber extends Subscriber<Event> {

    /**
     * Returns which event type are SmartSubscriber interested in.
     *
     * @return The interestd event types.
     */
    boolean supportsEventType(Class<? extends Event> eventType);

    /**
     * Determine whether this listener actually supports the given source type.
     * <p>The default implementation always returns {@code true}.
     * @param sourceType the source type, or {@code null} if no source
     */
    default boolean supportsSourceType(Class<?> sourceType) {
        return true;
    }

}
