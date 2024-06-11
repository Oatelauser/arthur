package com.arthur.boot.event.process;

import com.arthur.boot.event.EventPublisherAware;
import com.arthur.boot.event.annotation.ArthurEventListener;
import com.arthur.boot.process.AnnotationListenerMethodProcessor;
import com.arthur.common.notify.event.Event;
import com.arthur.common.notify.publisher.EventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

/**
 * 处理{@link ArthurEventListener}的bean对象
 *
 * @author DearYang
 * @date 2022-07-28
 * @see ArthurEventListenerAnnotationMethodProcessor
 * @since 1.0
 */
@Deprecated
public class ArthurEventListenerMethodProcessor extends AnnotationListenerMethodProcessor<ArthurEventListener> implements EventPublisherAware {

    private static final Logger LOG = LoggerFactory.getLogger(ArthurEventListenerMethodProcessor.class);
    private EventPublisher eventPublisher;

    @Override
    @SuppressWarnings("unchecked")
    protected void processListenerMethod(String beanName, Object bean, Class<?> beanClass, ArthurEventListener annotation, Method method, ApplicationContext applicationContext) {
        Class<?> subscribeType = method.getParameterTypes()[0];
        eventPublisher.addSubscriber((Class<? extends Event>) subscribeType, event -> ReflectionUtils.invokeMethod(method, bean, event));
    }

    @Override
    public boolean isCandidateMethod(Object bean, Class<?> beanClass, ArthurEventListener annotation, Method method, ApplicationContext applicationContext) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length != 1) {
            if (LOG.isWarnEnabled()) {
                LOG.warn("Listener method [" + method + "] parameters count must be one");
            }
            return false;
        }

        Class<?> eventType = parameterTypes[0];
        if (!Event.class.isAssignableFrom(eventType)) {
            if (LOG.isWarnEnabled()) {
                LOG.warn("Listener method [" + method + "] parameter type must be [" + Event.class + "]");
                return false;
            }
        }

        return true;
    }

    @Override
    public void setEventPublisher(EventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

}
