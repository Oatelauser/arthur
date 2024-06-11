package com.arthur.sentinel.autoconfigure;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.RequestOriginParser;
import com.arthur.sentinel.callback.WebRequestOriginParser;
import com.arthur.sentinel.handler.BlockExceptionHandler;
import com.arthur.sentinel.handler.DefaultBlockExceptionHandler;
import com.arthur.sentinel.handler.DelegatingBlockExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type.SERVLET;

/**
 * Sentinel WebMVC 自动配置类
 *
 * @author DearYang
 * @date 2022-09-12
 * @since 1.0
 */
@AutoConfiguration
@ConditionalOnWebApplication(type = SERVLET)
@ConditionalOnClass(RequestOriginParser.class)
@Import(BlockExceptionHandlerConfiguration.class)
@EnableConfigurationProperties(SentinelProperties.class)
public class SentinelWebAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public RequestOriginParser defaultRequestOriginParser(SentinelProperties sentinelProperties) {
		return new WebRequestOriginParser(sentinelProperties);
	}

	@Bean
	@ConditionalOnMissingBean
	public com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler webBlockExceptionHandler(ObjectMapper objectMapper,
																												  ObjectProvider<List<BlockExceptionHandler>> provider) {
		return new DelegatingBlockExceptionHandler(new DefaultBlockExceptionHandler(objectMapper), provider);
	}

}
