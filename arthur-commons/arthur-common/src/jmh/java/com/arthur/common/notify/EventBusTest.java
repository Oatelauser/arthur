package com.arthur.common.notify;

import com.arthur.common.notify.publisher.EventBus;
import com.arthur.common.notify.subscriber.Subscriber;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

@Fork(1)
@Threads(8)
@State(value = Scope.Benchmark)
@Warmup(iterations = 3, time = 1)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Measurement(iterations = 5, time = 5)
@BenchmarkMode({Mode.AverageTime, Mode.Throughput})
public class EventBusTest {

    private EventBus eventBus;

    @Setup
    public void setup() {
        eventBus = new EventBus();
        eventBus.start();
    }

    @TearDown
    public void tearDown() {
        eventBus.shutdown();
    }

    @Benchmark
    public void event(Blackhole blackhole) {
        eventBus.addSubscriber((Subscriber<MyEvent>) event -> {
            String eventObject = (String) event.getEventObject();
            blackhole.consume(eventObject);
        });
        eventBus.publish(new MyEvent("123"));
    }

}
