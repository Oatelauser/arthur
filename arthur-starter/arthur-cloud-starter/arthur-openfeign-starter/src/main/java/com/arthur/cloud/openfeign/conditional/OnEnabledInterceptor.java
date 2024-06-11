package com.arthur.cloud.openfeign.conditional;

import com.arthur.boot.conditional.OnEnabledComponent;
import com.arthur.cloud.openfeign.utils.NameUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;

import java.lang.annotation.Annotation;

/**
 * {@link RequestInterceptor} Bean对象是否开启的Conditional
 *
 * @author DearYang
 * @date 2022-09-06
 * @see OnEnabledComponent
 * @since 1.0
 */
public class OnEnabledInterceptor extends OnEnabledComponent<RequestInterceptor> {

	public static final String PREFIX = "feign.interceptor.";

	@Override
	protected Class<? extends Annotation> annotationClass() {
		return ConditionalOnEnabledInterceptor.class;
	}

	@Override
	protected Class<? extends RequestInterceptor> defaultValueClass() {
		return DefaultValue.class;
	}

	@Override
	protected String normalizeComponentName(Class<? extends RequestInterceptor> componentClass) {
		return PREFIX + NameUtils.normalizeClassNameAsProperty(RequestInterceptor.class, componentClass) + SUFFIX;
	}

	static class DefaultValue implements RequestInterceptor {
		@Override
		public void apply(RequestTemplate template) {
			throw new UnsupportedOperationException("class DefaultValue is never meant to be instantiated");
		}
	}

}
