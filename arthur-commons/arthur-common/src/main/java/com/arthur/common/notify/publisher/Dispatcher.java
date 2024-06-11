package com.arthur.common.notify.publisher;

import com.arthur.common.notify.event.Event;
import com.arthur.common.notify.subscriber.SmartSubscriber;
import com.arthur.common.notify.subscriber.Subscriber;
import com.arthur.common.utils.CollectionUtils;
import com.arthur.common.utils.ConcurrentHashSet;
import com.arthur.common.utils.TypeResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

/**
 * event dispatcher
 *
 * @author DearYang
 * @date 2022-07-27
 * @see Subscriber
 * @see SmartSubscriber
 * @see EventPublisher
 * @since 1.0
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class Dispatcher implements SubscriberRegistry {

    private static final Logger LOG = LoggerFactory.getLogger(Dispatcher.class);

    /**
     * cache {@link SmartSubscriber} object
     */
    private final Set<SmartSubscriber> smartSubscribers = new ConcurrentHashSet<>();

    /**
     * cache {@link Subscriber} object, Class-Subscriber collection relationship
     */
    private final Map<Class<?>, Set<Subscriber>> registry = new ConcurrentHashMap<>();

    /**
     * cache {@link Subscriber} object, Subscriber-Class relationship
     */
    private final Map<Subscriber, Class<?>> subscriberClassMap = new ConcurrentHashMap<>();
    private static final Method ON_EVENT_METHOD;


    static {
        try {
            ON_EVENT_METHOD = Subscriber.class.getMethod("onEvent", Event.class);
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("unable reflect Subscriber onEvent method");
        }
    }

    /**
     * registry subscriber
     *
     * @param subscriber subscribe event
     */
    @Override
    public void addSubscriber(Subscriber subscriber) {
        if (subscriberClassMap.containsKey(subscriber)) {
            return;
        }

        if (subscriber instanceof SmartSubscriber) {
            smartSubscribers.add((SmartSubscriber) subscriber);
            return;
        }

        // 1.考虑普通实现类；2.lambda实现特殊处理
        boolean isSynthetic = subscriber.getClass().isSynthetic();
        Class<?> eventType = isSynthetic ? subscriber.getParameterTypes().get(0) : TypeResolver.resolveParamClasses(ON_EVENT_METHOD, subscriber.getClass())[0];

        if (subscriberClassMap.put(subscriber, eventType) == null) {
            addSubscriber((Class<? extends Event>) eventType, subscriber);
        }
    }

    @Override
    public void addSubscriber(Class<? extends Event> subscribeType, Subscriber subscriber) {
        registry.computeIfAbsent(subscribeType, key -> new ConcurrentHashSet<>()).add(subscriber);
    }

    @Override
    public void removeSubscriber(Subscriber subscriber) {
        if (subscriber instanceof SmartSubscriber) {
            smartSubscribers.remove(subscriber);
            return;
        }

        Class<?> evenType;
        if ((evenType = subscriberClassMap.remove(subscriber)) != null) {
            removeSubscriber((Class<? extends Event>) evenType, subscriber);
        }
    }

    @Override
    public void removeSubscriber(Class<? extends Event> subscribeType, Subscriber subscriber) {
        if (registry.containsKey(subscribeType)) {
            registry.get(subscribeType).remove(subscriber);
        }
    }

    /**
     * post event data
     *
     * @param event event
     */
    public void post(Event event) {
        Class<? extends Event> eventType = event.getClass();

        if (!smartSubscribers.isEmpty()) {
            Class<?> sourceType = event.getEventObject().getClass();
            for (SmartSubscriber subscriber : smartSubscribers) {
                if (subscriber.supportsEventType(eventType) && subscriber.supportsSourceType(sourceType)) {
                    post(event, subscriber);
                }
            }
        }

        Set<Subscriber> subscribers = getSubscribers(eventType);
        if (CollectionUtils.isEmpty(subscribers)) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("The Event [{}] has not matched any one Subscriber", event.getClass().getName());
            }
            return;
        }

        for (Subscriber subscriber : subscribers) {
            post(event, subscriber);
        }
    }

    /**
     * post event data
     *
     * @param event      event
     * @param subscriber subscribe event
     * @see Dispatcher#post(Event)
     */
    private void post(Event event, Subscriber subscriber) {
        Executor executor = subscriber.getExecutor();
        if (executor != null) {
            executor.execute(() -> subscriber.onEvent(event));
            return;
        }

        try {
            subscriber.onEvent(event);
        } catch (Throwable t) {
            LOG.error("The Subscriber consume error", t);
        }
    }

    /**
     * 获取注册的{@link Subscriber}
     * <p>
     * 首先获取有注册{@link Event}的订阅者，然后在去获取指定类型的事件订阅者
     *
     * @param eventType 指定类型的事件
     * @return {@link Subscriber}
     */
    private Set<Subscriber> getSubscribers(Class<? extends Event> eventType) {
        Set<Subscriber> subscribers = new HashSet<>();

        if (eventType != Event.class) {
            Set<Subscriber> eventSubscribers = registry.get(Event.class);
            if (eventSubscribers != null) {
                subscribers.addAll(eventSubscribers);
            }
        }

        Set<Subscriber> registrySubscribers = registry.get(eventType);
        if (registrySubscribers != null) {
            subscribers.addAll(registrySubscribers);
        }

        return subscribers;
    }

}
