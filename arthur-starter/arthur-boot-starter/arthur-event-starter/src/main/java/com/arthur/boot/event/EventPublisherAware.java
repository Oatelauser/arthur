package com.arthur.boot.event;

import com.arthur.common.notify.publisher.EventPublisher;
import org.springframework.beans.factory.Aware;

/**
 * automatic injection {@link EventPublisher} interface
 *
 * @author DearYang
 * @date 2022-07-28
 * @since 1.0
 */
@SuppressWarnings("unused")
public interface EventPublisherAware extends Aware {

    /**
     * set {@link EventPublisher} implement object instance
     *
     * @param eventPublisher {@link EventPublisher}
     */
    void setEventPublisher(EventPublisher eventPublisher);

}
