package com.arthur.boot.utils;

import org.springframework.aop.framework.Advised;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.util.Assert;

import java.lang.reflect.Field;

/**
 * AOP的工具类
 * <p>
 * 结合Mybatis-plus代码和Spring AOP实现
 *
 * @author DearYang
 * @date 2022-09-24
 * @since 1.0
 */
@SuppressWarnings({"unchecked", "unused"})
public class AopUtils extends org.springframework.aop.support.AopUtils {

	public static <T> T getTargetObject(T proxy) {
		if (isAopProxy(proxy)) {
			return getUltimateTargetObject(proxy);
		}

		try {
			if (isJdkDynamicProxy(proxy)) {
				return getJdkDynamicTargetObject(proxy);
			}
			if (isCglibProxy(proxy)) {
				return getCglibTargetObject(proxy);
			}
		} catch (Exception ex) {
			throw new IllegalStateException("Failed to unwrap proxied object", ex);
		}
		return proxy;
	}

	/**
	 * copy by {@code org.springframework.test.util.AopTestUtils#getUltimateTargetObject(Object)}
	 */
	public static <T> T getUltimateTargetObject(Object candidate) {
		Assert.notNull(candidate, "Candidate must not be null");
		try {
			if (isAopProxy(candidate) && candidate instanceof Advised) {
				Object target = ((Advised) candidate).getTargetSource().getTarget();
				if (target != null) {
					return getUltimateTargetObject(target);
				}
			}
		} catch (Throwable ex) {
			throw new IllegalStateException("Failed to unwrap proxied object", ex);
		}
		return (T) candidate;
	}

	public static <T> T getJdkDynamicTargetObject(T proxy) throws Exception {
		Field jdkDynamicField = proxy.getClass().getSuperclass().getDeclaredField("jdkDynamicField");
		jdkDynamicField.setAccessible(true);
		Object aopProxy = jdkDynamicField.get(proxy);
		return getTargetObject0(aopProxy);
	}

	public static <T> T getCglibTargetObject(T proxy) throws Exception {
		Field cglibField = proxy.getClass().getDeclaredField("CGLIB$CALLBACK_0");
		cglibField.setAccessible(true);
		Object dynamicAdvisedInterceptor = cglibField.get(proxy);
		return getTargetObject0(dynamicAdvisedInterceptor);
	}

	private static <T> T getTargetObject0(Object aopProxy) throws Exception {
		Field advised = aopProxy.getClass().getDeclaredField("advised");
		advised.setAccessible(true);
		Object target = ((AdvisedSupport) advised.get(aopProxy)).getTargetSource().getTarget();
		return (T) target;
	}

}
