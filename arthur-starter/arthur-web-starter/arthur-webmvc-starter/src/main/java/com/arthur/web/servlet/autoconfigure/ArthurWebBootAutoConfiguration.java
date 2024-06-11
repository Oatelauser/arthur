package com.arthur.web.servlet.autoconfigure;

import com.arthur.boot.conditional.ConditionalOnMissingReactive;
import com.arthur.web.servlet.process.AnnotationHandlerMethodPostProcessor;
import com.arthur.web.servlet.process.SpringMappingHandlerRegistar;
import com.arthur.web.servlet.server.WebServiceResponse;
import io.undertow.Undertow;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration.EnableWebMvcConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * Web自动配置类
 *
 * @author DearYang
 * @date 2022-09-16
 * @since 1.0
 */
@AutoConfiguration
@Import(GracefulResponseConfiguration.class)
@EnableConfigurationProperties({ HttpClientPoolProperties.class })
public class ArthurWebBootAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public WebServiceResponse webServiceResponse(ObjectFactory<HttpMessageConverters> messageConverters) {
		return new WebServiceResponse(messageConverters);
	}

	@Bean
	@ConditionalOnMissingBean
	public AnnotationHandlerMethodPostProcessor handlerMethodPostProcessor() {
		return new AnnotationHandlerMethodPostProcessor();
	}

	@Bean
	@ConditionalOnMissingBean
	@SuppressWarnings("SpellCheckingInspection")
	public SpringMappingHandlerRegistar requestMappingHandlerRegistar(
			EnableWebMvcConfiguration webMvcConfiguration,
			@Qualifier("requestMappingHandlerMapping") RequestMappingHandlerMapping handlerMapping) {
		return new SpringMappingHandlerRegistar(handlerMapping, webMvcConfiguration);
	}

	@ConditionalOnMissingReactive
	@Configuration(proxyBeanMethods = false)
	@ConditionalOnBean(WebMvcConfigurer.class)
	@Import({ WebRestTemplateConfiguration.class,
		RequestMappingHandlerAdapterConfiguration.class })
	static class ArthurWebmvcConfiguration {
	}

	@ConditionalOnClass(Undertow.class)
	@Configuration(proxyBeanMethods = false)
	@Import({ UndertowStaticResourceConfiguration.class })
	static class ArthurUndertowConfiguration {
	}

}
