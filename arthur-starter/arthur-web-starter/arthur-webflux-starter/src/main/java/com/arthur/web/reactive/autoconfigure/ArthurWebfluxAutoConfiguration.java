package com.arthur.web.reactive.autoconfigure;

import com.arthur.boot.conditional.ConditionalOnMissingGateway;
import com.arthur.web.reactive.context.ReactorContextWebFilter;
import com.arthur.web.reactive.process.AnnotationHandlerMethodReactivePostProcessor;
import com.arthur.web.reactive.process.HandleMethodReactiveProcessor;
import com.arthur.web.reactive.process.WebfluxExceptionResponseProcessor;
import com.arthur.web.reactive.server.WebServerResponse;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.result.view.ViewResolver;

import java.util.List;

import static com.arthur.web.reactive.constant.WebfluxConstants.REACTOR_CONTEXT_BEAN;
import static org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type.REACTIVE;

/**
 * Webflux 自动配置类
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-01-07
 * @since 1.0
 */
@AutoConfiguration
@Import(WebClientConfiguration.class)
public class ArthurWebfluxAutoConfiguration {

	@Bean
	public WebServerResponse webServerResponseHandler(
			ObjectProvider<ExchangeStrategies> strategies,
			ObjectProvider<List<ViewResolver>> viewResolvers) {
		return new WebServerResponse(strategies.getIfAvailable(ExchangeStrategies::withDefaults),
			viewResolvers.getIfAvailable(List::of));
	}

	@Bean(name = REACTOR_CONTEXT_BEAN)
	@ConditionalOnMissingBean(name = REACTOR_CONTEXT_BEAN)
	public ReactorContextWebFilter reactorContextWebFilter() {
		return new ReactorContextWebFilter();
	}

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnMissingGateway
	public AnnotationHandlerMethodReactivePostProcessor handleMethodReactivePostProcessor(
			ObjectProvider<List<HandleMethodReactiveProcessor>> provider) {
		return new AnnotationHandlerMethodReactivePostProcessor(provider);
	}

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnWebApplication(type = REACTIVE)
	public WebfluxExceptionResponseProcessor webfluxExceptionResponseProcess() {
		return new WebfluxExceptionResponseProcessor();
	}

}
