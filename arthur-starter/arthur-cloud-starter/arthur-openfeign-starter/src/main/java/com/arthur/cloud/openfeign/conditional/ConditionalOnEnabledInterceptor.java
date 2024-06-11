package com.arthur.cloud.openfeign.conditional;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

/**
 * 是否开启{@link RequestInterceptor}的Bean对象
 *
 * @author DearYang
 * @date 2022-09-06
 * @see OnEnabledInterceptor
 * @since 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Conditional(OnEnabledInterceptor.class)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface ConditionalOnEnabledInterceptor {

	/**
	 * The class component to check for.
	 *
	 * @return the class that must be enabled
	 */
	Class<? extends RequestInterceptor> value() default OnEnabledInterceptor.DefaultValue.class;

}
