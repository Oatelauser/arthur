package com.arthur.common.notify.publisher;

import com.arthur.common.constant.BaseConstants;
import com.arthur.common.notify.event.Event;

/**
 * 事件发布
 *
 * @author DearYang
 * @date 2022-07-27
 * @since 1.0
 */
public class EventBus extends DisruptorEventPublisher {

    public EventBus() {
        this(BaseConstants.DISRUPTOR_CONSUMER_SIZE, BaseConstants.RING_BUFFER_SIZE);
    }

    public EventBus(int consumerSize) {
        this(consumerSize, BaseConstants.RING_BUFFER_SIZE);
    }

    public EventBus(int consumerSize, int ringBufferSize) {
        this(consumerSize, ringBufferSize, false);
    }

    public EventBus(int consumerSize, int ringBufferSize, boolean orderly) {
        super(consumerSize, ringBufferSize, orderly);
    }

    /**
     * publish event
     *
     * @param event event
     * @return success or fail
     */
    public boolean post(Event event) {
        return publish(event);
    }

    public static EventPublisher getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final EventPublisher INSTANCE = new EventBus();
    }

}
