package com.arthur.boot.aop;

import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 基于方法参数注解匹配
 *
 * @author DearYang
 * @date 2022-09-27
 * @since 1.0
 */
@SuppressWarnings("unused")
public class AnnotationMethodParameterMatcher extends StaticMethodMatcherPointcut {

	private final Class<? extends Annotation> annotationType;

	/**
	 * 是否继承注解
	 */
	private final boolean checkInherited;

	public AnnotationMethodParameterMatcher(Class<? extends Annotation> annotationType, boolean checkInherited) {
		Assert.notNull(annotationType, "Annotation type must not be null");
		this.checkInherited = checkInherited;
		this.annotationType = annotationType;
	}

	public AnnotationMethodParameterMatcher(Class<? extends Annotation> annotationType) {
		this(annotationType, false);
	}

	@Override
	public boolean matches(@NonNull Method method, @NonNull Class<?> targetClass) {
		if (Proxy.isProxyClass(targetClass)) {
			return false;
		}

		for (Class<?> parameterType : method.getParameterTypes()) {
			if (AnnotationMatchers.matches(checkInherited, annotationType, parameterType)) {
				return true;
			}
		}

		return false;
	}

}
