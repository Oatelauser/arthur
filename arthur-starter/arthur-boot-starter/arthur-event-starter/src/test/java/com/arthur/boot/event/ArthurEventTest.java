package com.arthur.boot.event;

import com.arthur.boot.event.annotation.ArthurEventListener;
import com.arthur.boot.event.autoconfigure.ArthurEventAutoConfiguration;
import com.arthur.common.notify.event.Event;
import com.arthur.common.notify.publisher.EventBus;
import com.arthur.common.notify.publisher.EventPublisher;
import com.arthur.common.notify.subscriber.Subscriber;
import org.junit.jupiter.api.Test;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

/**
 * 测试事件
 *
 * @author DearYang
 * @date 2022-07-29
 * @since 1.0
 */
@SpringBootApplication
@ImportAutoConfiguration(ArthurEventAutoConfiguration.class)
public class ArthurEventTest {

    public static void main(String[] args) {
        SpringApplication.run(ArthurEventTest.class, args);
    }

    @Test
    public void t2() {
        EventBus eventBus = new EventBus();
        eventBus.start();
        eventBus.shutdown();
    }

    public static class MyEvent extends Event {

        protected MyEvent(String eventObject) {
            super(eventObject);
        }

    }

    @Component
    public static class Producer implements EventPublisherAware, ApplicationRunner {

        private EventPublisher eventPublisher;

        @Override
        public void setEventPublisher(EventPublisher eventPublisher) {
            this.eventPublisher = eventPublisher;
        }

        @Override
        public void run(ApplicationArguments args) throws Exception {
            eventPublisher.publish(new MyEvent("123"));
        }
    }

    @Component
    public static class Consumer1 implements Subscriber<MyEvent> {
        @Override
        public void onEvent(MyEvent event) {
            String eventObject = (String) event.getEventObject();
            System.out.println("Subscriber listener: " + eventObject);
        }

        @ArthurEventListener
        public void processEvent(MyEvent event) {
            String eventObject = (String) event.getEventObject();
            System.out.println("Subscriber listener: " + eventObject);
        }

    }

}
