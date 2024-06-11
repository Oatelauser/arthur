package com.arthur.boot.aop;

import org.springframework.aop.ClassFilter;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldFilter;

import java.lang.annotation.Annotation;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 基于类中的字段注解过滤器
 *
 * @author DearYang
 * @date 2022-09-27
 * @see ClassFilter
 * @since 1.0
 */
@SuppressWarnings("unused")
public class AnnotationClassFieldFilter implements ClassFilter {

	private final Class<? extends Annotation> annotationType;

	/**
	 * 是否在父类查找字段
	 */
	private final boolean checkSuper;

	/**
	 * 是否继承注解
	 */
	private final boolean checkInherited;

	/**
	 * 字段过滤器
	 */
	private FieldFilter fieldFilter = new PrivateFieldFilter();

	public AnnotationClassFieldFilter(Class<? extends Annotation> annotationType, boolean checkSuper, boolean checkInherited) {
		Assert.notNull(annotationType, "Annotation type must not be null");
		this.checkSuper = checkSuper;
		this.checkInherited = checkInherited;
		this.annotationType = annotationType;
	}

	public AnnotationClassFieldFilter(Class<? extends Annotation> annotationType, boolean checkSuper) {
		this(annotationType, checkSuper, false);
	}

	public AnnotationClassFieldFilter(Class<? extends Annotation> annotationType) {
		this(annotationType, false);
	}

	@Override
	public boolean matches(@NonNull Class<?> clazz) {
		final AtomicBoolean found = new AtomicBoolean(false);
		if (checkSuper) {
			ReflectionUtils.doWithFields(clazz, field -> {
				if (found.get()) {
					return;
				}

				if (AnnotationMatchers.matches(checkInherited, annotationType, field)) {
					found.set(true);
				}
			}, this.fieldFilter);
		} else {
			ReflectionUtils.doWithLocalFields(clazz, field -> {
				if (found.get()) {
					return;
				}
				if (this.fieldFilter != null && !this.fieldFilter.matches(field)) {
					return;
				}
				if (AnnotationMatchers.matches(checkInherited, annotationType, field)) {
					found.set(true);
				}
			});
		}
		return found.get();
	}

	public void setFieldFilter(FieldFilter fieldFilter) {
		this.fieldFilter = fieldFilter;
	}

}
