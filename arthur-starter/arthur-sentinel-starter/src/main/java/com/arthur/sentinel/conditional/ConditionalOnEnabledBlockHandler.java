package com.arthur.sentinel.conditional;

import com.arthur.sentinel.handler.BlockHandler;
import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

/**
 * 是否开启{@link BlockHandler}的Bean对象
 *
 * @author DearYang
 * @date 2022-09-06
 * @see OnEnabledBlockHandler
 * @since 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Conditional(OnEnabledBlockHandler.class)
public @interface ConditionalOnEnabledBlockHandler {

	/**
	 * The class component to check for.
	 *
	 * @return the class that must be enabled
	 */
	Class<? extends BlockHandler> value() default OnEnabledBlockHandler.DefaultValue.class;

}
