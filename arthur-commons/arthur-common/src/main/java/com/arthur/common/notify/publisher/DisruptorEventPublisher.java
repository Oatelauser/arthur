package com.arthur.common.notify.publisher;

import com.arthur.common.concurrent.thread.CompositeSingleThreadPoolExecutor;
import com.arthur.common.concurrent.thread.NamedThreadFactory;
import com.arthur.common.notify.event.*;
import com.arthur.common.notify.subscriber.Subscriber;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.IgnoreExceptionHandler;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * disruptor event publisher
 *
 * @author DearYang
 * @date 2022-07-27
 * @since 1.0
 */
@SuppressWarnings("rawtypes")
public class DisruptorEventPublisher implements EventPublisher {

    private static final Logger LOG = LoggerFactory.getLogger(DisruptorProvider.class);
    private final int consumerSize;
    private final int ringBufferSize;
    private final boolean orderly;
    private final Dispatcher dispatcher = new Dispatcher();
    private volatile boolean initialized = false;
    private volatile boolean shutdown = false;
    private DisruptorProvider<Event> provider;
    private CompositeSingleThreadPoolExecutor executor;

    /**
     * @param consumerSize   消费线程大小
     * @param ringBufferSize 事件环形缓存队列
     * @param orderly        是否顺序消费
     */
    public DisruptorEventPublisher(int consumerSize, int ringBufferSize, boolean orderly) {
        this.orderly = orderly;
        this.consumerSize = consumerSize;
        this.ringBufferSize = ringBufferSize;
    }

    @Override
    public void start() {
        if (initialized) {
            return;
        }
        if (shutdown) {
            LOG.warn("Disruptor server has shutdown");
            return;
        }

        start0();
        initialized = true;
    }

    private void start0() {
        executor = new CompositeSingleThreadPoolExecutor(orderly, consumerSize, new NamedThreadFactory("disruptor-consumer"));
        EventFactory<DataEvent<Event>> eventFactory = orderly ? new SlowEventFactory<>() : new DataEventFactory<>();
        Disruptor<DataEvent<Event>> disruptor = new Disruptor<>(eventFactory, ringBufferSize, new NamedThreadFactory("disruptor-publisher"), ProducerType.MULTI, new BlockingWaitStrategy());
        int size = orderly ? 1 : consumerSize;
        @SuppressWarnings("unchecked") DataEventConsumer<Event>[] consumers = new DataEventConsumer[size];
        for (int i = 0; i < size; i++) {
            consumers[i] = new DataEventConsumer<>(dispatcher, executor);
        }

        disruptor.handleEventsWithWorkerPool(consumers);
        disruptor.setDefaultExceptionHandler(new IgnoreExceptionHandler());
        disruptor.start();
        provider = new DisruptorProvider<>(disruptor.getRingBuffer(), disruptor);
    }

    @Override
    public long currentEventSize() {
        return provider.ringBuffer.getBufferSize() - provider.ringBuffer.remainingCapacity();
    }

    @Override
    public void addSubscriber(Subscriber subscriber) {
        dispatcher.addSubscriber(subscriber);
    }

    @Override
    public void addSubscriber(Class<? extends Event> subscribeType, Subscriber subscriber) {
        dispatcher.addSubscriber(subscribeType, subscriber);
    }

    @Override
    public void removeSubscriber(Class<? extends Event> subscribeType, Subscriber subscriber) {
        dispatcher.removeSubscriber(subscribeType, subscriber);
    }

    @Override
    public void removeSubscriber(Subscriber subscriber) {
        dispatcher.removeSubscriber(subscriber);
    }

    @Override
    public boolean publish(Event event) {
        try {
            return provider.onData(event);
        } catch (Exception e) {
            LOG.error("Disruptor publish data has error", e);
            return false;
        }
    }

    @Override
    public void shutdown() {
        if (!initialized || shutdown) {
            return;
        }

        executor.close();
        shutdown = true;
    }

}
