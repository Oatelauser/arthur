package com.arthur.boot.lifecycle;

import com.arthur.boot.core.bean.SortedBean;
import com.arthur.boot.utils.BeanUtils;
import com.arthur.common.lifecycle.ServiceShutdownEvent;
import com.arthur.common.lifecycle.ShutdownHook;
import com.arthur.common.notify.publisher.EventPublisher;
import com.arthur.common.utils.CollectionUtils;
import com.arthur.common.utils.ConcurrentHashSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 服务优雅关闭回调
 *
 * @author DearYang
 * @date 2022-08-01
 * @since 1.0
 */
@SuppressWarnings("unused")
public abstract class AbstractShutdownHandler implements ShutdownHandler, ApplicationContextAware {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractShutdownHandler.class);
    private final Set<SortedLifecycleBean<ShutdownHook>> shutdownListeners = new ConcurrentHashSet<>();
    private EventPublisher eventPublisher;

    public AbstractShutdownHandler(ObjectProvider<List<ShutdownHook>> provider) {
        List<ShutdownHook> listeners = provider.getIfAvailable();
        if (listeners != null) {
            listeners.forEach(this::addShutdownListener);
        }
    }

    @Override
    public void addShutdownListener(ShutdownHook shutdownHook) {
        shutdownListeners.add(new SortedLifecycleBean<>(shutdownHook));
    }

    /**
     * 遍历关闭{@link ShutdownHook}
     */
    @Override
    public void invokeShutdown() {
        if (eventPublisher != null) {
            eventPublisher.publish(new ServiceShutdownEvent(null));
        }

        if (CollectionUtils.isEmpty(shutdownListeners)) {
            return;
        }

        List<SortedLifecycleBean<ShutdownHook>> sortedShutdownListeners = BeanUtils.sort(shutdownListeners);
        for (SortedLifecycleBean<ShutdownHook> shutdownListener : sortedShutdownListeners) {
            try {
                shutdownListener.getBean().shutdown();
            } catch (Exception e) {
                LOG.warn("ShutdownListener invoke shutdown method has error", e);
            }
        }
    }

    @Override
    public List<ShutdownHook> getShutdownListeners() {
        return BeanUtils.sort(shutdownListeners).stream().map(SortedBean::getBean)
                .collect(Collectors.toList());
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        eventPublisher = applicationContext.getBeanProvider(EventPublisher.class).getIfAvailable();
    }

}
