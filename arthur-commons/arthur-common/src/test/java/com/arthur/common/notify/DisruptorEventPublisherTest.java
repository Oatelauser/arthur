package com.arthur.common.notify;

import com.arthur.common.notify.event.Event;
import com.arthur.common.notify.publisher.DisruptorEventPublisher;
import com.arthur.common.notify.publisher.EventBus;
import com.arthur.common.notify.subscriber.Subscriber;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class DisruptorEventPublisherTest {

    DisruptorEventPublisher eventPublisher;

    static class MyEvent extends Event {

        private static final long serialVersionUID = 5299414712353005216L;

        protected MyEvent(String eventObject) {
            super(eventObject);
        }
    }

    @BeforeEach
    public void setup() {
        eventPublisher = new EventBus();
        eventPublisher.start();
    }

    @AfterEach
    public void tearDown() {
        eventPublisher.shutdown();
    }

    @Test
    public void t1() {
        eventPublisher.addSubscriber((Subscriber<MyEvent>) event -> System.out.println(event.getEventObject()));
        eventPublisher.publish(new MyEvent("123"));
        int a = 1;
    }



}
