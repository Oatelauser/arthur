package com.arthur.common.notify.event;

import com.lmax.disruptor.EventFactory;

/**
 * This event share one event-queue fac
 *
 * @author DearYang
 * @date 2022-07-27
 * @since 1.0
 */
public class SlowEventFactory<T> implements EventFactory<DataEvent<T>> {

    @Override
    public SlowEvent<T> newInstance() {
        return new SlowEvent<>();
    }

}
