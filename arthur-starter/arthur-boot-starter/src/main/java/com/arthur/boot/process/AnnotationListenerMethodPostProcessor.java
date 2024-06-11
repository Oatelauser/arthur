package com.arthur.boot.process;

import com.arthur.boot.utils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.AutoProxyUtils;
import org.springframework.aop.scope.ScopedObject;
import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 自定义拓展实现Bean方法注解监听器
 * <p>
 * 参考{@code EventListenerMethodProcessor}
 *
 * @author DearYang
 * @date 2022-08-28
 * @since 1.0
 */
@SuppressWarnings("rawtypes")
public class AnnotationListenerMethodPostProcessor implements BeanFactoryPostProcessor, SmartInitializingSingleton, ApplicationContextAware {

	private static final Logger LOG = LoggerFactory.getLogger(AnnotationListenerMethodPostProcessor.class);
	private final Map<Class<?>, Set<Class<?>>> ignoreClasses = new ConcurrentHashMap<>();
	private ConfigurableListableBeanFactory beanFactory;
	private ConfigurableApplicationContext applicationContext;

	private static boolean isSpringContainerClass(Class<?> clazz) {
		return (clazz.getName().startsWith("org.springframework.") &&
			!AnnotatedElementUtils.isAnnotated(ClassUtils.getUserClass(clazz), Component.class));
	}

	@Override
	public void postProcessBeanFactory(@NonNull ConfigurableListableBeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	@Override
	public void afterSingletonsInstantiated() {
		ConfigurableListableBeanFactory beanFactory = this.beanFactory;
		Assert.state(this.beanFactory != null, "No ConfigurableListableBeanFactory set");
		Map<Class<? extends Annotation>, AnnotationMethodProcessor> methodProcessors = getAnnotationMethodProcessors();
		if (CollectionUtils.isEmpty(methodProcessors)) {
			return;
		}

		for (String beanName : beanFactory.getBeanDefinitionNames()) {
			if (ScopedProxyUtils.isScopedTarget(beanName)) {
				continue;
			}

			Class<?> type = null;
			try {
				type = AutoProxyUtils.determineTargetClass(beanFactory, beanName);
			} catch (Throwable ex) {
				if (LOG.isDebugEnabled()) {
					LOG.debug("Could not resolve target class for bean with name '" + beanName + "'", ex);
				}
			}

			if (type != null && ScopedObject.class.isAssignableFrom(type)) {
				try {
					type = AutoProxyUtils.determineTargetClass(beanFactory, ScopedProxyUtils.getTargetBeanName(beanName));
				} catch (Throwable ex) {
					if (LOG.isDebugEnabled()) {
						LOG.debug("Could not resolve target bean for scoped proxy '" + beanName + "'", ex);
					}
				}
			}

			for (Map.Entry<Class<? extends Annotation>, AnnotationMethodProcessor> entry : methodProcessors.entrySet()) {
				try {
					Class<? extends Annotation> annotationClass = entry.getKey();
					processBean(beanName, type, annotationClass, ignoreClasses.computeIfAbsent(annotationClass,
							k -> Collections.newSetFromMap(new ConcurrentHashMap<>(64))),
						entry.getValue());
				} catch (Throwable ex) {
					throw new BeanInitializationException("Failed to process annotation on bean with name '" + beanName + "'", ex);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void processBean(String beanName, Class<?> targetType, Class<? extends Annotation> annotationClass,
			Set<Class<?>> ignoreClasses, AnnotationMethodProcessor methodProcessor) {
		if (!ignoreClasses.contains(targetType) && !isSpringContainerClass(targetType) &&
			AnnotationUtils.isCandidateClass(targetType, annotationClass)) {
			Map<Method, ? extends Annotation> annotatedMethods = null;
			try {
				annotatedMethods = MethodIntrospector.selectMethods(targetType,
					(MethodIntrospector.MetadataLookup<? extends Annotation>) method ->
						AnnotatedElementUtils.findMergedAnnotation(method, annotationClass));
			} catch (Throwable ex) {
				if (LOG.isDebugEnabled()) {
					LOG.debug("Could not resolve methods for bean with name '" + beanName + "'", ex);
				}
			}

			if (CollectionUtils.isEmpty(annotatedMethods)) {
				ignoreClasses.add(targetType);
				if (LOG.isTraceEnabled()) {
					LOG.trace("No annotations found on bean class: " + targetType.getName());
				}
				return;
			}

			ConfigurableApplicationContext context = this.applicationContext;
			Assert.state(context != null, "No ApplicationContext set");
			for (Map.Entry<Method, ? extends Annotation> entry : annotatedMethods.entrySet()) {
				Method annotatedMethod = AopUtils.selectInvocableMethod(entry.getKey(), context.getType(beanName));
				if (methodProcessor.supports(annotatedMethod)) {
					methodProcessor.processMethod(beanName, targetType, annotatedMethod);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private Map<Class<? extends Annotation>, AnnotationMethodProcessor> getAnnotationMethodProcessors() {
		List<AnnotationMethodProcessor> processors = BeanUtils.getBeanOfList(beanFactory, AnnotationMethodProcessor.class);
		if (CollectionUtils.isEmpty(processors)) {
			return null;
		}

		Map<Class<? extends Annotation>, AnnotationMethodProcessor> methodProcessors = new HashMap<>(processors.size());
		for (AnnotationMethodProcessor processor : processors) {
			methodProcessors.put((Class<? extends Annotation>) processor.getAnnotationType(), processor);
		}
		return methodProcessors;
	}

	@Override
	public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
		Assert.isTrue(applicationContext instanceof ConfigurableApplicationContext,
			"ApplicationContext does not implement ConfigurableApplicationContext");
		this.applicationContext = (ConfigurableApplicationContext) applicationContext;
	}

}
