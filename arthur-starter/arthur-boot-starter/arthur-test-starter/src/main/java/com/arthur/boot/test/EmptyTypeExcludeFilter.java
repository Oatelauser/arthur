package com.arthur.boot.test;

import org.springframework.boot.test.autoconfigure.filter.AnnotationCustomizableTypeExcludeFilter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.util.Collections;
import java.util.Set;

/**
 * 加载排除过滤器
 * <p>
 * 默认不会加载所有Bean对象
 *
 * @author DearYang
 * @date 2022-08-02
 * @since 1.0
 */
public class EmptyTypeExcludeFilter extends AnnotationCustomizableTypeExcludeFilter {

	private final TestEmptyEnvironment annotation;

	EmptyTypeExcludeFilter(Class<?> testClass) {
		this.annotation = AnnotatedElementUtils.getMergedAnnotation(testClass, TestEmptyEnvironment.class);
	}

	@Override
	protected boolean hasAnnotation() {
		return annotation != null;
	}

	@Override
	protected ComponentScan.Filter[] getFilters(FilterType type) {
		switch (type) {
			case INCLUDE:
				return annotation.includeFilters();
			case EXCLUDE:
				return annotation.excludeFilters();
			default:
				throw new UnsupportedOperationException("Unsupported filter type: " + type);
		}
	}

	@Override
	protected boolean isUseDefaultFilters() {
		return annotation.useDefaultFilters();
	}

	@Override
	protected Set<Class<?>> getDefaultIncludes() {
		return Collections.emptySet();
	}

	@Override
	protected Set<Class<?>> getComponentIncludes() {
		return Collections.emptySet();
	}

}
