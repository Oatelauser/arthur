package com.arthur.boot.aop;

import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

/**
 * 注解匹配器
 *
 * @author DearYang
 * @date 2022-09-28
 * @since 1.0
 */
public abstract class AnnotationMatchers {

	/**
	 * 判断指定{@link AnnotatedElement}是否存在注解，或者继承注解
	 *
	 * @param checkInherited 是否继承注解
	 * @param annotationType 注解类型
	 * @param element        {@link AnnotatedElement}
	 * @return yes or no
	 */
	public static boolean matches(boolean checkInherited, Class<? extends Annotation> annotationType, AnnotatedElement element) {
		return checkInherited ? AnnotatedElementUtils.hasAnnotation(element, annotationType) :
			element.isAnnotationPresent(annotationType);
	}

}
