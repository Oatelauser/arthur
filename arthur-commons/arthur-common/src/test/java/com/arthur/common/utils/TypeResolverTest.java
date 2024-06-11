package com.arthur.common.utils;

import com.arthur.common.notify.event.Event;
import com.arthur.common.notify.subscriber.Subscriber;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

/**
 * 概要描述
 * <p>
 * 详细描述
 *
 * @author DearYang
 * @date 2022-07-30
 * @since 1.0
 */
public class TypeResolverTest {


    static class MyEvent extends Event {

        private static final long serialVersionUID = -8523482923511387302L;

        protected MyEvent(String eventObject) {
            super(eventObject);
        }
    }


    static class MySubscriber implements Subscriber<MyEvent> {

        @Override
        public void onEvent(MyEvent event) {

        }
    }


    @Test
    public void t1() throws Exception {
        Method method = Subscriber.class.getMethod("onEvent", Event.class);
        Class<?> aClass = TypeResolver.resolveParamClasses(method, MySubscriber.class)[0];
        System.out.println(aClass);
    }

}
