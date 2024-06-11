package com.arthur.web.servlet.process;

import com.arthur.common.invoke.Lookups;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration.EnableWebMvcConfiguration;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.resource.ResourceUrlProvider;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;

import static java.lang.invoke.MethodType.methodType;

/**
 * {@link WebMvcConfigurationSupport}操作类
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-04-25
 * @see SpringMappingHandlerRegistar
 * @since 1.0
 */
class WebmvcConfigurationOperator {

	private static final MethodHandle PATH_MATCH_METHOD;

	private final EnableWebMvcConfiguration webMvcConfiguration;

	static {
		MethodHandles.Lookup lookup = Lookups.trustedLookup(EnableWebMvcConfiguration.class);
		try {
			PATH_MATCH_METHOD = lookup.findVirtual(EnableWebMvcConfiguration.class,
				"getPathMatchConfigurer", methodType(PathMatchConfigurer.class));
		} catch (NoSuchMethodException | IllegalAccessException e) {
			throw new IllegalStateException(e);
		}
	}

	public WebmvcConfigurationOperator(EnableWebMvcConfiguration webMvcConfiguration) {
		this.webMvcConfiguration = webMvcConfiguration;
	}

	public PathMatchConfigurer getPathMatchConfigurer() {
		try {
			return (PathMatchConfigurer) PATH_MATCH_METHOD.invoke(webMvcConfiguration);
		} catch (Throwable e) {
			throw new IllegalStateException(e);
		}
	}

	public SpringMappingHandlerMapping requestMappingHandlerMapping(RequestMappingHandlerMapping delegate) {
		SpringMvcConfiguration configuration = new SpringMvcConfiguration(delegate, this);
		return (SpringMappingHandlerMapping) configuration.requestMappingHandlerMapping(new ContentNegotiationManager(),
			new FormattingConversionService(), new ResourceUrlProvider());
	}

	@SuppressWarnings("all")
	private static class SpringMvcConfiguration extends WebMvcConfigurationSupport {

		private final RequestMappingHandlerMapping delegate;
		private final WebmvcConfigurationOperator operator;

		private SpringMvcConfiguration(RequestMappingHandlerMapping delegate,
			WebmvcConfigurationOperator operator) {
			this.delegate = delegate;
			this.operator = operator;
		}

		@Override
		protected RequestMappingHandlerMapping createRequestMappingHandlerMapping() {
			return new SpringMappingHandlerMapping(delegate);
		}

		@Override
		protected PathMatchConfigurer getPathMatchConfigurer() {
			return operator.getPathMatchConfigurer();
		}

	}
}
