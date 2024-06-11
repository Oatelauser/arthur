package com.arthur.common.notify.publisher;

import com.arthur.common.lifecycle.ShutdownHook;
import com.arthur.common.notify.event.Event;

/**
 * Event publisher.
 */
@SuppressWarnings("unused")
public interface EventPublisher extends SubscriberRegistry, ShutdownHook {

    /**
     * init method
     */
    void start();

    /**
     * The number of currently staged events.
     *
     * @return event size
     */
    long currentEventSize();

    /**
     * publish event.
     *
     * @param event event object
     * @return publish event is success
     */
    boolean publish(Event event);

}
