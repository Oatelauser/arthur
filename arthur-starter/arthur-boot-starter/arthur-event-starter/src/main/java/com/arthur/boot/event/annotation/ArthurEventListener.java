package com.arthur.boot.event.annotation;

import com.arthur.boot.event.process.ArthurEventListenerMethodProcessor;
import com.arthur.common.notify.subscriber.Subscriber;

import java.lang.annotation.*;

/**
 * 事件监听注解
 * <p>
 * 实现订阅者{@link Subscriber}功能
 *
 * @author DearYang
 * @date 2022-07-28
 * @see ArthurEventListenerMethodProcessor
 * @since 1.0
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ArthurEventListener {
}
