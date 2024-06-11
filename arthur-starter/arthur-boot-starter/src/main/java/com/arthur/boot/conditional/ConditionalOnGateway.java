package com.arthur.boot.conditional;

import org.springframework.boot.autoconfigure.condition.AllNestedConditions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

import static org.springframework.context.annotation.ConfigurationCondition.ConfigurationPhase.PARSE_CONFIGURATION;

/**
 * 条件判断Spring Cloud Gateway运行环境
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-06
 * @since 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
@Conditional(ConditionalOnGateway.OnGatewayCondition.class)
public @interface ConditionalOnGateway {

	class OnGatewayCondition extends AllNestedConditions {

		public OnGatewayCondition() {
			super(PARSE_CONFIGURATION);
		}

		@SuppressWarnings("unused")
		@ConditionalOnClass(name = { "org.springframework.cloud.gateway.filter.GatewayFilter",
			"org.springframework.web.reactive.DispatcherHandler" })
		@ConditionalOnProperty(name = "spring.cloud.gateway.enabled", matchIfMissing = true)
		private static class GatewayCondition {
		}

	}

}
