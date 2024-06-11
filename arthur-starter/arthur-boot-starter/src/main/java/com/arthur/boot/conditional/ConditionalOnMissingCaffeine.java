package com.arthur.boot.conditional;

import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

/**
 * 条件判断是否存在如下类：
 * 1.Caffeine
 * 2.CaffeineCacheManager
 *
 * @author DearYang
 * @date 2022-09-08
 * @see OnCaffeineCacheMissingCondition
 * @since 1.0
 */
@Documented
@SuppressWarnings("unused")
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Conditional(ConditionalOnMissingCaffeine.OnCaffeineCacheMissingCondition.class)
public @interface ConditionalOnMissingCaffeine {

	class OnCaffeineCacheMissingCondition extends AnyNestedCondition {

		private OnCaffeineCacheMissingCondition() {
			super(ConfigurationPhase.REGISTER_BEAN);
		}

		@ConditionalOnMissingClass("com.github.benmanes.caffeine.cache.Caffeine")
		static class CaffeineClassMissing {
		}

		@ConditionalOnMissingClass("org.springframework.cache.caffeine.CaffeineCacheManager")
		static class CaffeineCacheManagerClassMissing {
		}

	}

}
