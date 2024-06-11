package com.arthur.common.notify.publisher;

import com.arthur.common.notify.event.DataEvent;
import com.arthur.common.notify.event.DataEventTranslator;
import com.arthur.common.notify.event.SlowEvent;
import com.arthur.common.notify.event.SlowEventTranslator;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.EventTranslatorTwoArg;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

import java.io.Closeable;

/**
 * disruptor support
 *
 * @author DearYang
 * @date 2022-07-27
 * @since 1.0
 */
@SuppressWarnings("unused")
public class DisruptorProvider<T> implements Closeable {

    final RingBuffer<DataEvent<T>> ringBuffer;
    final Disruptor<DataEvent<T>> disruptor;
    private final EventTranslatorOneArg<DataEvent<T>, T> eventTranslator = new DataEventTranslator<>();
    private final EventTranslatorTwoArg<DataEvent<T>, T, String> slowEventTranslator = new SlowEventTranslator<>();


    public DisruptorProvider(RingBuffer<DataEvent<T>> ringBuffer, Disruptor<DataEvent<T>> disruptor) {
        this.ringBuffer = ringBuffer;
        this.disruptor = disruptor;
    }

    public boolean onData(T event) {
        ringBuffer.publishEvent(eventTranslator, event);
        return true;
    }

    public boolean onData(T event, String sequence) {
        if (!(event instanceof SlowEvent)) {
            return false;
        }

        ringBuffer.publishEvent(slowEventTranslator, event, sequence);
        return true;
    }

    @Override
    public void close() {
        if (disruptor != null) {
            disruptor.shutdown();
        }
    }

    public Disruptor<DataEvent<T>> getDisruptor() {
        return disruptor;
    }
}
