package com.arthur.boot.aop;

import org.springframework.aop.ClassFilter;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.lang.NonNull;

import java.lang.annotation.Annotation;

/**
 * 拓展{@link AnnotationMatchingPointcut}，提供更为高级功能
 * <p>
 * 1.类注解
 * 2.方法注解
 * 3.类和方法`且`关系
 * 4.类和方法`或`关系
 *
 * @author DearYang
 * @date 2022-09-26
 * @since 1.0
 */
@SuppressWarnings("unused")
public class AnnotationExpandMatchingPointcut extends AnnotationMatchingPointcut {

	/**
	 * 特殊的类过滤器
	 */
	private static final String CLASS_FILTER_NAME = "org.springframework.aop.support.annotation.AnnotationMatchingPointcut.AnnotationCandidateClassFilter";

	private Class<? extends Annotation> classAnnotationType;

	private AnnotationExpandMatchingPointcut(Class<? extends Annotation> classAnnotationType, Class<? extends Annotation> methodAnnotationType) {
		super(classAnnotationType, methodAnnotationType);
	}

	private AnnotationExpandMatchingPointcut(Class<? extends Annotation> classAnnotationType, Class<? extends Annotation> methodAnnotationType, boolean checkInherited) {
		super(classAnnotationType, methodAnnotationType, checkInherited);
	}

	/**
	 * 提供基于方法注解的AOP
	 * <p>
	 * 匹配所有类，过滤方法
	 *
	 * @param methodAnnotationType 方法注解
	 * @param checkInherited       是否继承继承
	 * @return {@link AnnotationMatchingPointcut}
	 */
	public static AnnotationMatchingPointcut createMethodAnnotationPointcut(Class<? extends Annotation> methodAnnotationType, boolean checkInherited) {
		return new AnnotationExpandMatchingPointcut(null, methodAnnotationType, checkInherited);
	}

	/**
	 * @see AnnotationExpandMatchingPointcut#createMethodAnnotationPointcut(Class, boolean)
	 */
	public static Pointcut createMethodAnnotationPointcut(Class<? extends Annotation> methodAnnotationType) {
		return new AnnotationExpandMatchingPointcut(null, methodAnnotationType);
	}

	/**
	 * 提供基于类上注解的AOP
	 * <p>
	 * 过滤特定类，匹配所有方法
	 *
	 * @param classAnnotationType 类上注解
	 * @param checkInherited      是否继承注解
	 * @return {@link AnnotationMatchingPointcut}
	 */
	public static Pointcut createClassAnnotationPointcut(Class<? extends Annotation> classAnnotationType, boolean checkInherited) {
		return new AnnotationMatchingPointcut(classAnnotationType, checkInherited);
	}

	/**
	 * @see AnnotationExpandMatchingPointcut#createClassAnnotationPointcut(Class, boolean)
	 */
	public static Pointcut createClassAnnotationPointcut(Class<? extends Annotation> classAnnotationType) {
		return new AnnotationMatchingPointcut(classAnnotationType);
	}

	/**
	 * 匹配类和方法上的注解
	 * <p>
	 * 过滤特定类，过滤特定方法
	 *
	 * @param classAnnotationType  类上注解
	 * @param methodAnnotationType 方法注解
	 * @param checkInherited       是否继承注解
	 * @return {@link AnnotationExpandMatchingPointcut}
	 */
	public static Pointcut createClassMethodAnnotationPointcut(Class<? extends Annotation> classAnnotationType, Class<? extends Annotation> methodAnnotationType, boolean checkInherited) {
		return new AnnotationExpandMatchingPointcut(classAnnotationType, methodAnnotationType, checkInherited);
	}

	/**
	 * @see AnnotationExpandMatchingPointcut#createClassMethodAnnotationPointcut(Class, Class, boolean)
	 */
	public static Pointcut createClassMethodAnnotationPointcut(Class<? extends Annotation> classAnnotationType, Class<? extends Annotation> methodAnnotationType) {
		return new AnnotationExpandMatchingPointcut(classAnnotationType, methodAnnotationType);
	}

	/**
	 * 匹配类或者方法上的注解
	 * <p>
	 * 过滤特定类或者过滤特定方法
	 *
	 * @param classOrMethodFilter {@link AnnotationClassOrMethodFilter}
	 * @return {@link AnnotationClassOrMethodPointcut}
	 */
	public static Pointcut createClassOrMethodAnnotationPointcut(AnnotationClassOrMethodFilter classOrMethodFilter) {
		return new AnnotationClassOrMethodPointcut(classOrMethodFilter);
	}

	@NonNull
	@Override
	public ClassFilter getClassFilter() {
		ClassFilter classFilter = super.getClassFilter();
		if (CLASS_FILTER_NAME.equals(classFilter.getClass().getName())) {
			classFilter = ClassFilter.TRUE;
		}

		return classFilter;
	}

}
