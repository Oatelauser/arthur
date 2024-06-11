package com.arthur.sentinel.autoconfigure;

import com.arthur.sentinel.conditional.ConditionalOnEnabledExceptionHandler;
import com.arthur.sentinel.handler.BlockExceptionHandler;
import com.arthur.sentinel.handler.BlockExceptionHandlers;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * WebMVC 阻塞异常处理器配置类
 *
 * @author DearYang
 * @date 2022-09-14
 * @since 1.0
 */
@Configuration(proxyBeanMethods = false)
class BlockExceptionHandlerConfiguration {

	private final ObjectMapper objectMapper;

	BlockExceptionHandlerConfiguration(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Bean
	@ConditionalOnEnabledExceptionHandler
    BlockExceptionHandler flowExceptionHandler() {
		return new BlockExceptionHandlers.FlowBlockExceptionHandler(objectMapper);
	}

	@Bean
	@ConditionalOnEnabledExceptionHandler
	BlockExceptionHandler systemExceptionHandler() {
		return new BlockExceptionHandlers.SystemBlockExceptionHandler(objectMapper);
	}

	@Bean
	@ConditionalOnEnabledExceptionHandler
	BlockExceptionHandler degradeExceptionHandler() {
		return new BlockExceptionHandlers.DegradeBlockExceptionHandler(objectMapper);
	}

	@Bean
	@ConditionalOnEnabledExceptionHandler
	BlockExceptionHandler paramFlowExceptionHandler() {
		return new BlockExceptionHandlers.ParamFlowBlockExceptionHandler(objectMapper);
	}

	@Bean
	@ConditionalOnEnabledExceptionHandler
	BlockExceptionHandler authorityExceptionHandler() {
		return new BlockExceptionHandlers.AuthorityBlockExceptionHandler(objectMapper);
	}

}
