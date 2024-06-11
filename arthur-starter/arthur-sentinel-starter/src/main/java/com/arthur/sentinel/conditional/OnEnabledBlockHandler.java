package com.arthur.sentinel.conditional;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.arthur.boot.conditional.OnEnabledComponent;
import com.arthur.boot.utils.NameUtils;
import com.arthur.sentinel.handler.BlockHandler;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.lang.annotation.Annotation;

/**
 * {@link BlockHandler} Bean对象是否开启的Conditional
 *
 * @author DearYang
 * @date 2022-09-06
 * @see OnEnabledComponent
 * @since 1.0
 */
public class OnEnabledBlockHandler extends OnEnabledComponent<BlockHandler> {


	public static final String PREFIX = "sentinel.block-request-handler";

	@Override
	protected Class<? extends Annotation> annotationClass() {
		return ConditionalOnEnabledExceptionHandler.class;
	}

	@Override
	protected Class<? extends BlockHandler> defaultValueClass() {
		return DefaultValue.class;
	}

	@Override
	protected String normalizeComponentName(Class<? extends BlockHandler> componentClass) {
		return PREFIX + NameUtils.normalizeClassNameAsProperty(BlockHandler.class, componentClass) + SUFFIX;
	}

	static class DefaultValue implements BlockHandler {

		@Override
		public boolean support(BlockException ex) {
			throw new UnsupportedOperationException("class DefaultValue is never meant to be instantiated");
		}


		@Override
		public Mono<ServerResponse> handleException(ServerWebExchange exchange, BlockException ex) {
			throw new UnsupportedOperationException("class DefaultValue is never meant to be instantiated");
		}

	}

}
