package com.arthur.boot.conditional;

import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.ConfigurationCondition;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.core.type.MethodMetadata;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.util.Map;

import static com.arthur.common.constant.BaseConstants.STRING_FALSE;

/**
 * 增强拓展 {@code org.springframework.cloud.gateway.config.conditional.OnEnabledComponent}
 * <p>
 * 该类的Conditional注解必须有以下隐式条件属性，可以不用设置：
 * 1.{@code value}属性，返回值是{@link Class}
 * 2.支持{@link org.springframework.core.annotation.AliasFor}
 * <p>
 * 实例注解，支持的注解范式如下：
 * 1.@ConditionalOnEnabledInterceptor
 * 2.@ConditionalOnEnabledInterceptor(value=XX.class)
 *
 * @author DearYang
 * @date 2022-09-06
 * @since 1.0
 */
@SuppressWarnings("unused")
public abstract class OnEnabledComponent<T> extends SpringBootCondition implements ConfigurationCondition {

	public static final String SUFFIX = ".enabled";

	@NonNull
	@Override
	public ConfigurationPhase getConfigurationPhase() {
		return ConfigurationPhase.REGISTER_BEAN;
	}

	@Override
	public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
		Map<String, Object> attributes = metadata.getAnnotationAttributes(annotationClass().getName());
		Class<? extends T> candidateClass = getComponentType(context, metadata, attributes);
		return determineOutcome(candidateClass, context.getEnvironment(), attributes);
	}

	@SuppressWarnings("unchecked")
	protected Class<? extends T> getComponentType(ConditionContext context, AnnotatedTypeMetadata metadata, Map<String, Object> attributes) {
		Class<?> target;
		if (attributes != null && (target = (Class<?>) attributes.get("value")) != null) {
			if (target != defaultValueClass()) {
				return (Class<? extends T>) target;
			}
		}

		Assert.state(metadata instanceof MethodMetadata && metadata.isAnnotated(Bean.class.getName()),
			getClass().getSimpleName() + " must be used on @Bean methods when the value is not specified");

		MethodMetadata methodMetadata = (MethodMetadata) metadata;
		try {
			return (Class<? extends T>) ClassUtils.forName(methodMetadata.getReturnTypeName(), context.getClassLoader());
		} catch (Throwable ex) {
			throw new IllegalStateException("Failed to extract component class for "
				+ methodMetadata.getDeclaringClassName() + "." + methodMetadata.getMethodName(), ex);
		}
	}

	/**
	 * 确定条件输出
	 *
	 * @param componentClass 组件class对象
	 * @param environment    Spring环境
	 * @param attributes     当前注解信息
	 * @return {@link ConditionOutcome}
	 */
	private ConditionOutcome determineOutcome(Class<? extends T> componentClass, Environment environment, Map<String, Object> attributes) {
		String key = normalizeComponentName(componentClass);
		ConditionMessage.Builder builder = ConditionMessage.forCondition(annotationClass().getName(), componentClass.getName());

		if (STRING_FALSE.equalsIgnoreCase(environment.getProperty(key))) {
			return ConditionOutcome.noMatch(builder.because("bean is not available"));
		}

		return ConditionOutcome.match();
	}

	protected boolean hasMatchIfMissing() {
		return false;
	}

	/**
	 * 获取注解Class对象
	 *
	 * @return 注解Class
	 */
	protected abstract Class<? extends Annotation> annotationClass();

	/**
	 * 默认类型
	 *
	 * @return 默认实现的class
	 */
	protected abstract Class<? extends T> defaultValueClass();

	/**
	 * 规范化名称
	 *
	 * @param componentClass 组件class
	 * @return 规范化的名称
	 */
	protected abstract String normalizeComponentName(Class<? extends T> componentClass);

}
