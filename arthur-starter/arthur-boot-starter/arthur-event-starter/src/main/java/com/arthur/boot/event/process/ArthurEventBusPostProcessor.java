package com.arthur.boot.event.process;

import com.arthur.common.notify.publisher.EventPublisher;
import com.arthur.common.notify.subscriber.Subscriber;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;

/**
 * 自动注册事件订阅者{@link Subscriber}
 *
 * @author DearYang
 * @date 2022-08-05
 * @since 1.0
 */
public class ArthurEventBusPostProcessor implements InstantiationAwareBeanPostProcessor, ApplicationContextAware {

    private EventPublisher eventPublisher;

    @Override
    @SuppressWarnings("rawtypes")
    public boolean postProcessAfterInstantiation(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        if (bean instanceof Subscriber) {
            eventPublisher.addSubscriber((Subscriber) bean);
        }

        return true;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        eventPublisher = applicationContext.getBean(EventPublisher.class);
    }

}
