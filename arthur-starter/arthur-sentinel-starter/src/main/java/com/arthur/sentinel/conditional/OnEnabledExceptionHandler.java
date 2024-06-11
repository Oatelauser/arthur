package com.arthur.sentinel.conditional;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.arthur.boot.conditional.OnEnabledComponent;
import com.arthur.boot.utils.NameUtils;
import com.arthur.sentinel.handler.BlockExceptionHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.lang.annotation.Annotation;

/**
 * {@link BlockExceptionHandler} Bean对象是否开启的Conditional
 *
 * @author DearYang
 * @date 2022-09-06
 * @see OnEnabledComponent
 * @since 1.0
 */
public class OnEnabledExceptionHandler extends OnEnabledComponent<BlockExceptionHandler> {

	public static final String PREFIX = "sentinel.block-exception-handler";

	@Override
	protected Class<? extends Annotation> annotationClass() {
		return ConditionalOnEnabledExceptionHandler.class;
	}

	@Override
	protected Class<? extends BlockExceptionHandler> defaultValueClass() {
		return DefaultValue.class;
	}

	@Override
	protected String normalizeComponentName(Class<? extends BlockExceptionHandler> componentClass) {
		return PREFIX + NameUtils.normalizeClassNameAsProperty(BlockExceptionHandler.class, componentClass) + SUFFIX;
	}

	static class DefaultValue implements BlockExceptionHandler {

		@Override
		public boolean support(BlockException ex) {
			throw new UnsupportedOperationException("class DefaultValue is never meant to be instantiated");
		}

		@Override
		public void handleException(HttpServletRequest request, HttpServletResponse response, BlockException ex) throws Exception {
			throw new UnsupportedOperationException("class DefaultValue is never meant to be instantiated");
		}

	}

}
