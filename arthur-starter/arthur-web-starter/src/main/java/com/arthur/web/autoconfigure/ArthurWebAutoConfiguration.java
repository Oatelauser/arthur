package com.arthur.web.autoconfigure;

import com.arthur.boot.autoconfigure.ApplicationLifecycleAutoConfiguration;
import com.arthur.boot.lifecycle.ShutdownHandler;
import com.arthur.common.lifecycle.ShutdownHook;
import com.arthur.web.lifecycle.UndertowGracefulShutdownHandler;
import com.arthur.web.validation.SpringValidation;
import jakarta.validation.Valid;
import jakarta.validation.valueextraction.ValueExtractor;
import org.hibernate.validator.internal.engine.ConfigurationImpl;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * Spring Web自动配置类
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-01-08
 * @since 1.0
 */
@EnableConfigurationProperties(GlobalExceptionProperties.class)
@Import({ JacksonConfiguration.class, ExceptionHandlerExceptionProcessor.class })
@AutoConfiguration(before = ApplicationLifecycleAutoConfiguration.class)
public class ArthurWebAutoConfiguration {

	@Bean
	@ConditionalOnClass(name = "io.undertow.server.handlers.GracefulShutdownHandler")
	public ShutdownHandler undertowGracefulShutdownHandler(ObjectProvider<List<ShutdownHook>> shutdownListenerProvider) {
		return new UndertowGracefulShutdownHandler(shutdownListenerProvider);
	}

	/**
	 * Spring Validation 自动配置类
	 */
	@Configuration(proxyBeanMethods = false)
	@ConditionalOnClass({ Valid.class, Validated.class, ConfigurationImpl.class })
	static class JakartaValidationConfiguration {

		@Bean
		@ConditionalOnMissingBean
		SpringValidation springValidation(ObjectProvider<List<ValueExtractor<?>>> valueExtractors) {
			return new SpringValidation(valueExtractors);
		}

	}

}
