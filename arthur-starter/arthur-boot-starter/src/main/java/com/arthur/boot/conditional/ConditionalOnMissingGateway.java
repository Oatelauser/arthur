package com.arthur.boot.conditional;

import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

import static org.springframework.context.annotation.ConfigurationCondition.ConfigurationPhase.REGISTER_BEAN;

/**
 * 条件判断缺少Spring Cloud Gateway运行环境
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-06
 * @since 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
@Conditional(ConditionalOnMissingGateway.OnMissGatewayCondition.class)
public @interface ConditionalOnMissingGateway {

	@SuppressWarnings("unused")
	class OnMissGatewayCondition extends AnyNestedCondition {

		public OnMissGatewayCondition() {
			super(REGISTER_BEAN);
		}

		@ConditionalOnMissingClass("org.springframework.cloud.gateway.filter.GatewayFilter")
		static class OnGatewayCondition {
		}

		@ConditionalOnProperty(name = "spring.cloud.gateway.enabled", havingValue = "false")
		static class OnGatewayPropertyCondition {
		}

	}

}
