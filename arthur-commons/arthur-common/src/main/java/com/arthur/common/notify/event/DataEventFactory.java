package com.arthur.common.notify.event;

import com.lmax.disruptor.EventFactory;

/**
 * create event instance factory
 *
 * @author DearYang
 * @date 2022-07-27
 * @since 1.0
 */
public class DataEventFactory<T> implements EventFactory<DataEvent<T>> {

    @Override
    public DataEvent<T> newInstance() {
        return new DataEvent<>();
    }

}
