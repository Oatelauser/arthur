package com.arthur.boot.lifecycle;

import com.arthur.boot.constant.BootConstants;
import com.arthur.boot.core.bean.SortedBean;
import com.arthur.boot.utils.BeanUtils;
import com.arthur.common.lifecycle.InitializeListener;
import com.arthur.common.lifecycle.ServiceStartupEvent;
import com.arthur.common.notify.publisher.EventPublisher;
import com.arthur.common.utils.CollectionUtils;
import com.arthur.common.utils.ConcurrentHashSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 抽象服务初始化预热
 *
 * @author DearYang
 * @date 2022-08-01
 * @since 1.0
 */
public abstract class AbstractServiceInitializer implements ServiceInitializer, ApplicationContextAware, ApplicationListener<ApplicationStartedEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractServiceInitializer.class);
    private static volatile boolean INITIALIZED = false;
    private final Set<SortedLifecycleBean<InitializeListener>> initializeListeners = new ConcurrentHashSet<>();
    private EventPublisher eventPublisher;

    public AbstractServiceInitializer(ObjectProvider<List<InitializeListener>> provider) {
        List<InitializeListener> listeners = provider.getIfAvailable();
        if (listeners != null) {
            listeners.forEach(this::addInitializeListener);
        }
    }

    static boolean isBootstrapContext(ApplicationStartedEvent applicationEvent) {
        return applicationEvent.getApplicationContext().getEnvironment().getPropertySources().contains(BootConstants.BOOTSTRAP_PROPERTY_SOURCE_NAME);
    }

    @Override
    public void onApplicationEvent(@NonNull ApplicationStartedEvent event) {
        if (INITIALIZED || isBootstrapContext(event)) {
            return;
        }

        synchronized (AbstractServiceInitializer.class) {
            if (INITIALIZED) {
                return;
            }

            if (eventPublisher != null) {
                eventPublisher.publish(new ServiceStartupEvent(null));
            }
            invokeStart();
            this.initialize();
            INITIALIZED = true;
        }
    }

    @Override
    public void invokeStart() {
        if (CollectionUtils.isEmpty(initializeListeners)) {
            return;
        }

        List<SortedLifecycleBean<InitializeListener>> sortedInitializeListeners = BeanUtils.sort(this.initializeListeners);
        for (SortedLifecycleBean<InitializeListener> sortedBean : sortedInitializeListeners) {
            try {
                sortedBean.getBean().start();
            } catch (Exception e) {
				if (LOG.isWarnEnabled()) {
					LOG.warn("InitializeListener invoke start method has error", e);
				}
            }
        }
    }

    @Override
    public void addInitializeListener(InitializeListener listener) {
        initializeListeners.add(new SortedLifecycleBean<>(listener));
    }

    @Override
    public List<InitializeListener> getInitializeListener() {
        return BeanUtils.sort(initializeListeners).stream().map(SortedBean::getBean)
                .collect(Collectors.toList());
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.eventPublisher = applicationContext.getBeanProvider(EventPublisher.class).getIfAvailable();
    }

    /**
     * 自定义初始化操作
     */
    protected abstract void initialize();

}
