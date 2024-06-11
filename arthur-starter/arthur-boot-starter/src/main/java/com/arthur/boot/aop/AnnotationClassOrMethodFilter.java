package com.arthur.boot.aop;

import org.springframework.aop.support.annotation.AnnotationClassFilter;
import org.springframework.aop.support.annotation.AnnotationMethodMatcher;
import org.springframework.lang.NonNull;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.MethodFilter;

import java.lang.annotation.Annotation;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 基于类和方法注解匹配器
 * <p>
 * 遍历当前类的所有方法，以及父类方法
 *
 * @author DearYang
 * @date 2022-09-27
 * @see org.springframework.aop.ClassFilter
 * @see AnnotationClassFilter
 * @see AnnotationMethodMatcher
 * @since 1.0
 */
public class AnnotationClassOrMethodFilter extends AnnotationClassFilter {

	private final boolean checkSuper;
	private final AnnotationMethodMatcher methodMatcher;
	private MethodFilter methodFilter;

	public AnnotationClassOrMethodFilter(Class<? extends Annotation> annotationType) {
		this(annotationType, false);
	}

	public AnnotationClassOrMethodFilter(Class<? extends Annotation> annotationType, boolean checkInherited) {
		this(annotationType, checkInherited, false);
	}

	public AnnotationClassOrMethodFilter(Class<? extends Annotation> annotationType, boolean checkInherited, boolean checkSuper) {
		super(annotationType, checkInherited);
		this.checkSuper = checkSuper;
		this.methodMatcher = new AnnotationMethodMatcher(annotationType, checkInherited);
	}

	@Override
	public boolean matches(@NonNull Class<?> clazz) {
		return super.matches(clazz) || hasAnnotatedMethods(clazz);
	}

	private boolean hasAnnotatedMethods(Class<?> clazz) {
		final AtomicBoolean found = new AtomicBoolean(false);
		if (this.checkSuper) {
			ReflectionUtils.doWithMethods(clazz, method -> {
				if (found.get()) {
					return;
				}
				if (this.methodMatcher.matches(method, clazz)) {
					found.set(true);
				}
			}, this.methodFilter);
		} else {
			ReflectionUtils.doWithLocalMethods(clazz, method -> {
				if (found.get()) {
					return;
				}
				if (this.methodFilter != null && !this.methodFilter.matches(method)) {
					return;
				}
				if (this.methodMatcher.matches(method, clazz)) {
					found.set(true);
				}
			});
		}
		return found.get();
	}

	public void setMethodFilter(MethodFilter methodFilter) {
		this.methodFilter = methodFilter;
	}

}
