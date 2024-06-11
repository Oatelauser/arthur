package com.arthur.common.utils;

import com.arthur.common.notify.event.Event;
import com.lmax.disruptor.dsl.Disruptor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * 概要描述
 * <p>
 * 详细描述
 *
 * @author DearYang
 * @date 2022-07-27
 * @since 1.0
 */
public class DisruptorTest {

    private Disruptor<Event> disruptor;
    private final static Integer RING_BUFFER_SIZE = 1024 * 1024;

    @AfterEach
    public void tearDown() throws Exception{
        //为了保证消费者线程已经启动，留足足够的时间
        Thread.sleep(1000);
        disruptor.shutdown();
    }

    @BeforeEach
    public void startup() {
        //disruptor = new Disruptor<>(factory, RING_BUFFER_SIZE,
        //        Executors.defaultThreadFactory(), ProducerType.SINGLE,
        //        new YieldingWaitStrategy());
    }

    @Test
    public void t1() {

    }


}
