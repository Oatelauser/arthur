package com.arthur.web.antisamy.autoconfigure;

import com.arthur.boot.conditional.ConditionalOnMissingGateway;
import com.arthur.boot.core.url.UrlPathMather;
import com.arthur.web.antisamy.context.AntiSamyPolicyService;
import com.arthur.web.antisamy.support.AnnotationHandler;
import com.arthur.web.antisamy.support.AntiSamyAnnotationHandler;
import com.arthur.web.antisamy.support.AntiSamyHandleMethodReactiveProcessor;
import com.arthur.web.antisamy.support.AntiSamyHandlerMethodProcessor;
import com.arthur.web.antisamy.web.AntiSamyHttpOutputMessageHandler;
import com.arthur.web.antisamy.web.AntiSamyWebFilter;
import com.arthur.web.antisamy.web.HttpServletRequestAntiSamyFilter;
import com.arthur.web.reactive.filter.HttpOutputMessageHandler;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.servlet.ConditionalOnMissingFilterBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.web.reactive.DispatcherHandler;
import org.springframework.web.server.WebFilter;
import org.springframework.web.servlet.DispatcherServlet;

import java.util.List;

import static com.arthur.web.antisamy.constant.AntiSamyConstants.FILTER_BEAN_NAME;
import static com.arthur.web.antisamy.constant.AntiSamyConstants.ORDER;
import static org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type.REACTIVE;
import static org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type.SERVLET;

/**
 * AntiSamy XSS自动配置类
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-06
 * @since 1.0
 */
@AutoConfiguration
@Import(AntiSamyPolicyServiceConfiguration.class)
@EnableConfigurationProperties(AntiSamyProperties.class)
@ConditionalOnProperty(value = "arthur.antisamy.enabled", havingValue = "true", matchIfMissing = true)
public class ArthurWebAntiSamyAutoConfiguration {

	@ConditionalOnMissingGateway
	@Configuration(proxyBeanMethods = false)
	static class AntiSamyWebMvcConfiguration {

		@Bean
		@ConditionalOnMissingFilterBean
		@ConditionalOnWebApplication(type = SERVLET)
		FilterRegistrationBean<HttpServletRequestAntiSamyFilter> antiSamyWebFilter(AntiSamyProperties properties,
				UrlPathMather urlPathMather,
				AntiSamyPolicyService antiSamyService) {
			FilterRegistrationBean<HttpServletRequestAntiSamyFilter> filterBean = new FilterRegistrationBean<>();
			filterBean.setOrder(ORDER);
			filterBean.setFilter(new HttpServletRequestAntiSamyFilter(properties, urlPathMather, antiSamyService));
			filterBean.addInitParameter("isIncludeRichText", "true");
			filterBean.addInitParameter("excludes", "/favicon.ico,/img/*,/js/*,/css/*");
			return filterBean;
		}

	}

	@ConditionalOnMissingGateway
	@Configuration(proxyBeanMethods = false)
	static class AntiSamyWebfluxConfiguration {

		@Bean
		@ConditionalOnMissingBean
		HttpOutputMessageHandler<String> httpOutputMessageHandler() {
			return new AntiSamyHttpOutputMessageHandler();
		}

		@Bean(name = FILTER_BEAN_NAME)
		@ConditionalOnClass(DispatcherHandler.class)
		@ConditionalOnWebApplication(type = REACTIVE)
		@ConditionalOnMissingBean(name = FILTER_BEAN_NAME)
		WebFilter antiSamyWebfluxFilter(AntiSamyProperties properties,
				AntiSamyPolicyService antiSamyService,
				HttpOutputMessageHandler<String> outputMessageHandler,
				ObjectProvider<List<HttpMessageReader<?>>> provider) {
			return new AntiSamyWebFilter(properties, antiSamyService, outputMessageHandler, provider);
		}

	}

	@ConditionalOnMissingGateway
	@Configuration(proxyBeanMethods = false)
	static class AnnotationProcessorConfiguration {

		@Bean
		@ConditionalOnMissingBean
		AnnotationHandler antiSamyAnnotationHandler(AntiSamyProperties properties) {
			return new AntiSamyAnnotationHandler(properties);
		}

		@Bean
		@ConditionalOnWebApplication(type = SERVLET)
		@ConditionalOnClass({ DispatcherServlet.class})
		AntiSamyHandlerMethodProcessor antiSamyHandleMethodProcessor(
				AnnotationHandler annotationHandler) {
			return new AntiSamyHandlerMethodProcessor(annotationHandler);
		}

		@Bean
		@ConditionalOnWebApplication(type = REACTIVE)
		@ConditionalOnClass({ DispatcherHandler.class})
		AntiSamyHandleMethodReactiveProcessor antiSamyHandleMethodReactiveProcessor(
				AnnotationHandler annotationHandler) {
			return new AntiSamyHandleMethodReactiveProcessor(annotationHandler);
		}

	}

}
