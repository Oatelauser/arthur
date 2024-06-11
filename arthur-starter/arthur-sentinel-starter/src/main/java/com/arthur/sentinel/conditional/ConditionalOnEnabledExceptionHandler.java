package com.arthur.sentinel.conditional;

import com.arthur.sentinel.handler.BlockExceptionHandler;
import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

/**
 * 是否开启{@link BlockExceptionHandler}的Bean对象
 *
 * @author DearYang
 * @date 2022-09-06
 * @see OnEnabledExceptionHandler
 * @since 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Conditional(OnEnabledExceptionHandler.class)
public @interface ConditionalOnEnabledExceptionHandler {

	/**
	 * The class component to check for.
	 *
	 * @return the class that must be enabled
	 */
	Class<? extends BlockExceptionHandler> value() default OnEnabledExceptionHandler.DefaultValue.class;

}
