package com.arthur.boot.context.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.*;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.lang.NonNull;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;

import static com.arthur.boot.constant.BootConstants.BOOTSTRAP_PROPERTY_SOURCE_NAME;

/**
 * 用于 {@link ApplicationEvent} 的抽象类 {@link ApplicationListener} 保证仅执行一次，
 * 并防止事件在分层 {@link ApplicationContext ApplicationContexts} 中传播
 * <p>
 * 一般用于Spring启动预热
 */
public abstract class OnceApplicationContextEventListener<T extends ApplicationEvent> implements ApplicationListener<T>, ApplicationContextAware {

	private static final Logger LOG = LoggerFactory.getLogger(OnceApplicationContextEventListener.class);

	private ApplicationContext applicationContext;

	@Override
	public void onApplicationEvent(@NonNull T event) {
		if (isBootstrapContext(event)) {
			return;
		}

		ApplicationContext source = this.resolveApplicationEvent(event);
		if (ObjectUtils.nullSafeEquals(getApplicationContext(), source)) {
			this.onApplicationEvent(source);
			return;
		}

		if (LOG.isDebugEnabled()) {
			LOG.debug("The source of event[" + event.getSource() + "] is not original!");
		}
	}

	private boolean isBootstrapContext(T event) {
		ConfigurableApplicationContext configurableApplicationContext = null;
		if (event instanceof ApplicationContextEvent applicationEvent) {
			ApplicationContext context = applicationEvent.getApplicationContext();
			if (context instanceof ConfigurableApplicationContext applicationEventContext) {
				configurableApplicationContext = applicationEventContext;
			}
		}
		if (event instanceof ApplicationReadyEvent applicationEvent) {
			configurableApplicationContext = applicationEvent.getApplicationContext();
		}
		if (configurableApplicationContext != null) {
			return configurableApplicationContext.getEnvironment().getPropertySources()
				.contains(BOOTSTRAP_PROPERTY_SOURCE_NAME);
		}
		return false;
	}

	/**
	 * 解析事件对象获取{@link ApplicationContext}对象
	 *
	 * @param event 事件
	 * @return {@link ApplicationContext}
	 */
	protected ApplicationContext resolveApplicationEvent(T event) {
		return (ApplicationContext) event.getSource();
	}

	public ApplicationContext getApplicationContext() {
		if (applicationContext == null) {
			throw new NullPointerException("applicationContext must be not null, it has to invoke " +
				"setApplicationContext(ApplicationContext) method first if "
				+ ClassUtils.getShortName(getClass()) + " instance is not a Spring Bean");
		}
		return applicationContext;
	}

	@Override
	public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	/**
	 * process ApplicationContext
	 *
	 * @param applicationContext {@link  ApplicationContext}
	 */
	protected abstract void onApplicationEvent(ApplicationContext applicationContext);

}
