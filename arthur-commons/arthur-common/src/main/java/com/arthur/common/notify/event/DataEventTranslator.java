package com.arthur.common.notify.event;

import com.lmax.disruptor.EventTranslatorOneArg;

/**
 * data event translator use to {@link com.lmax.disruptor.RingBuffer#publishEvent(EventTranslatorOneArg, Object)}
 *
 * @author DearYang
 * @date 2022-07-27
 * @since 1.0
 */
public class DataEventTranslator<T> implements EventTranslatorOneArg<DataEvent<T>, T> {

    @Override
    public void translateTo(DataEvent<T> event, long sequence, T eventObject) {
        event.setDelegator(eventObject);
    }
}
