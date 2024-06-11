package com.arthur.boot.conditional;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.NoneNestedConditions;
import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

import static org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type.REACTIVE;
import static org.springframework.context.annotation.ConfigurationCondition.ConfigurationPhase.PARSE_CONFIGURATION;

/**
 * 条件匹配：应用环境不是{@link org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type#REACTIVE}
 *
 * @author DearYang
 * @date 2022-09-09
 * @see org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
 * @since 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Conditional(ConditionalOnMissingReactive.OnMissReactiveCondition.class)
public @interface ConditionalOnMissingReactive {

	class OnMissReactiveCondition extends NoneNestedConditions {

		private OnMissReactiveCondition() {
			super(PARSE_CONFIGURATION);
		}

		@SuppressWarnings("unused")
		@ConditionalOnWebApplication(type = REACTIVE)
		private static class OnReactiveCondition { }
	}

}
