package com.arthur.common.notify.event;

import com.arthur.common.concurrent.thread.CompositeSingleThreadPoolExecutor;
import com.arthur.common.notify.publisher.Dispatcher;
import com.arthur.common.utils.StringUtils;
import com.lmax.disruptor.WorkHandler;

import java.util.concurrent.Executor;

/**
 * consume {@link DataEvent} by disruptor
 *
 * @author DearYang
 * @date 2022-07-27
 * @since 1.0
 */
public class DataEventConsumer<T extends Event> implements WorkHandler<DataEvent<T>> {

    private final Dispatcher dispatcher;
    private final CompositeSingleThreadPoolExecutor executor;

    public DataEventConsumer(Dispatcher dispatcher, CompositeSingleThreadPoolExecutor executor) {
        this.executor = executor;
        this.dispatcher = dispatcher;
    }

    @Override
    public void onEvent(DataEvent<T> event) {
        if (event == null) {
            return;
        }

        Executor eventExecutor = executor;
        if (event instanceof SlowEvent) {
            String sequence = ((SlowEvent<T>) event).getSequence();
            if (!StringUtils.isEmpty(sequence)) {
                eventExecutor = executor.getSingleExecutor(sequence);
            }
        }

        eventExecutor.execute(() -> dispatcher.post(event.getDelegator()));
    }

}
