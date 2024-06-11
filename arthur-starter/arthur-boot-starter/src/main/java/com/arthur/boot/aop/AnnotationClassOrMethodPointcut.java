package com.arthur.boot.aop;

import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.lang.NonNull;

/**
 * 基于类或者当前类的方法上的注解Pointcut
 *
 * @author DearYang
 * @date 2022-09-27
 * @since 1.0
 */
public class AnnotationClassOrMethodPointcut implements Pointcut {

	private final AnnotationClassOrMethodFilter classOrMethodFilter;

	public AnnotationClassOrMethodPointcut(AnnotationClassOrMethodFilter classOrMethodFilter) {
		this.classOrMethodFilter = classOrMethodFilter;
	}

	@NonNull
	@Override
	public MethodMatcher getMethodMatcher() {
		return MethodMatcher.TRUE;
	}

	@NonNull
	@Override
	public ClassFilter getClassFilter() {
		return this.classOrMethodFilter;
	}

}
