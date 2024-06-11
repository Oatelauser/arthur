package com.arthur.common.notify.publisher;

import com.arthur.common.notify.event.Event;
import com.arthur.common.notify.subscriber.Subscriber;

/**
 * registry subscriber
 *
 * @author DearYang
 * @date 2022-07-28
 * @since 1.0
 */
@SuppressWarnings("rawtypes")
public interface SubscriberRegistry {

    /**
     * Add listener.
     *
     * @param subscriber {@link Subscriber}
     */
    void addSubscriber(Subscriber subscriber);

    /**
     * Add listener for default share publisher.
     *
     * @param subscriber    {@link Subscriber}
     * @param subscribeType subscribe event type, such as slow event or general event.
     */
    void addSubscriber(Class<? extends Event> subscribeType, Subscriber subscriber);

    /**
     * Remove listener.
     *
     * @param subscriber {@link Subscriber}
     */
    void removeSubscriber(Subscriber subscriber);

    /**
     * Remove listener for default share publisher.
     *
     * @param subscriber    {@link Subscriber}
     * @param subscribeType subscribe event type, such as slow event or general event.
     */
    void removeSubscriber(Class<? extends Event> subscribeType, Subscriber subscriber);

}
