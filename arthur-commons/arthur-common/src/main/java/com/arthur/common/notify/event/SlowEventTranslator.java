package com.arthur.common.notify.event;

import com.lmax.disruptor.EventTranslatorTwoArg;

/**
 * slow data event translator use to {@link com.lmax.disruptor.RingBuffer#publishEvent(EventTranslatorTwoArg, Object, Object)}
 *
 * @author DearYang
 * @date 2022-07-27
 * @since 1.0
 */
public class SlowEventTranslator<T> implements EventTranslatorTwoArg<DataEvent<T>, T, String> {

    @Override
    public void translateTo(DataEvent<T> event, long order, T eventObject, String sequence) {
        if (event instanceof SlowEvent) {
            ((SlowEvent<T>) event).setSequence(sequence);
        }

        event.setDelegator(eventObject);
    }
}
