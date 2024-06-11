package com.arthur.boot.mybatis.datasource;

import com.arthur.boot.mybatis.annotation.DS;
import com.arthur.boot.mybatis.constant.MybatisConstants;
import com.arthur.boot.utils.AnnotationUtils;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.BeanFactoryAnnotationUtils;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Objects;

import static org.springframework.core.annotation.AnnotationUtils.getAnnotationAttributes;

/**
 * 动态数据源Advice，在标识{@link DS}的方法执行前动态设置数据源
 *
 * @author DearYang
 * @date 2022-09-24
 * @see DS
 * @see DatabaseAnnotationAdvisor
 * @since 1.0
 */
public class AnnotationDatabaseInterceptor implements MethodInterceptor {

	private static final Logger LOG = LoggerFactory.getLogger(AnnotationDatabaseInterceptor.class);
	private final Cache routeKeyCache;
	private final RouteLookup defaultLookupKey;
	private BeanFactory beanFactory;
	private Class<? extends Annotation> annotationType = DS.class;

	public AnnotationDatabaseInterceptor(RouteLookup defaultLookupKey, CacheManager cacheManager) {
		this.defaultLookupKey = defaultLookupKey;
		this.routeKeyCache = Objects.requireNonNull(cacheManager.getCache(MybatisConstants.ROUTE_KEY_CACHE_NAME));
	}

	@Override
	public Object invoke(@NonNull MethodInvocation invocation) throws Throwable {
		RouteLookup routeLookup = null;
		try {
			routeLookup = this.processMethodReturnRouteLookup(invocation.getMethod(), invocation.getThis(), invocation);
		} catch (Exception ignored) {
		}
		try {
			return invocation.proceed();
		} finally {
			if (routeLookup != null) {
				routeLookup.clear();
			}
		}
	}

	private RouteLookup processMethodReturnRouteLookup(@NonNull Method method, @Nullable Object target, MethodInvocation invocation) {
		// 如果存在缓存，则直接走缓存
		Cache.ValueWrapper wrapper;
		if ((wrapper = routeKeyCache.get(invocation)) != null) {
			defaultLookupKey.choose((String) wrapper.get());
			return null;
		}

		Class<?> targetClass = target == null ? null : AopUtils.getTargetClass(target);
		Method specificMethod = ClassUtils.getMostSpecificMethod(method, targetClass);
		Method userDeclaredMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);

		Annotation annotation = AnnotationUtils.findMethodAnnotation(userDeclaredMethod, this.annotationType, targetClass);
		if (annotation == null || AnnotationUtils.isDefaultValue(annotation, MybatisConstants.ANNOTATION_NAME)) {
			return null;
		}

		AnnotationAttributes attributes = getAnnotationAttributes(annotation, false, false);
		RouteLookup routeLookup = this.determineRouteLookup(attributes);
		String name = attributes.getString(MybatisConstants.ANNOTATION_NAME);
		defaultLookupKey.choose(name);
		routeKeyCache.putIfAbsent(invocation, name);
		return routeLookup;
	}

	private RouteLookup determineRouteLookup(AnnotationAttributes attributes) {
		RouteLookup routeLookup = this.defaultLookupKey;
		String qualifier = attributes.getString(MybatisConstants.ANNOTATION_LOOK_UP_NAME);
		if (StringUtils.hasLength(qualifier)) {
			try {
				routeLookup = BeanFactoryAnnotationUtils.qualifiedBeanOfType(this.beanFactory, RouteLookup.class, qualifier);
			} catch (NoSuchBeanDefinitionException e) {
				if (LOG.isTraceEnabled()) {
					LOG.trace("Could not find unique RouteLookup bean - attempting to resolve by name [" +
						" + " + qualifier + "] : " + e.getMessage());

				}
			} catch (Exception ignored) {
			}
		}

		return routeLookup;
	}

	public void setAnnotationType(Class<? extends Annotation> annotationType) {
		this.annotationType = annotationType;
	}

	public void setBeanFactory(BeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}

}
