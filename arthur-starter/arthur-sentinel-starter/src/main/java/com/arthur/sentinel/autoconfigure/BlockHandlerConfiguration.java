package com.arthur.sentinel.autoconfigure;

import com.arthur.sentinel.conditional.ConditionalOnEnabledBlockHandler;
import com.arthur.sentinel.handler.BlockHandler;
import com.arthur.sentinel.handler.BlockHandlers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Webflux 阻塞请求处理器配置类
 *
 * @author DearYang
 * @date 2022-09-14
 * @since 1.0
 */
@Configuration(proxyBeanMethods = false)
class BlockHandlerConfiguration {

	@Bean
	@ConditionalOnEnabledBlockHandler
    BlockHandler flowBlockHandler() {
		return new BlockHandlers.FlowBlockHandler();
	}

	@Bean
	@ConditionalOnEnabledBlockHandler
	BlockHandler systemBlockHandler() {
		return new BlockHandlers.SystemBlockHandler();
	}

	@Bean
	@ConditionalOnEnabledBlockHandler
	BlockHandler degradeBlockHandler() {
		return new BlockHandlers.DegradeBlockHandler();
	}

	@Bean
	@ConditionalOnEnabledBlockHandler
	BlockHandler authorityBlockHandler() {
		return new BlockHandlers.AuthorityBlockHandler();
	}

	@Bean
	@ConditionalOnEnabledBlockHandler
	BlockHandler paramFlowBlockHandler() {
		return new BlockHandlers.ParamFlowBlockHandler();
	}

}
