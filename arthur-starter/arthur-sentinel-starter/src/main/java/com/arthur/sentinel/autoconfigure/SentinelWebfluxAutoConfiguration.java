package com.arthur.sentinel.autoconfigure;

import com.alibaba.csp.sentinel.adapter.spring.webflux.callback.BlockRequestHandler;
import com.alibaba.csp.sentinel.adapter.spring.webflux.callback.WebFluxCallbackManager;
import com.arthur.sentinel.callback.WebfluxOriginParser;
import com.arthur.sentinel.callback.WebfluxUrlCleaner;
import com.arthur.sentinel.handler.BlockHandler;
import com.arthur.sentinel.handler.DefaultBlockRequestHandler;
import com.arthur.sentinel.handler.DelegatingBlockRequestHandler;
import com.arthur.sentinel.callback.WebfluxRequestOriginParser;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.lang.NonNull;

import java.util.List;

import static org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type.REACTIVE;

/**
 * Sentinel Webflux 自动配置类
 *
 * @author DearYang
 * @date 2022-09-12
 * @since 1.0
 */
@AutoConfiguration
@Import(BlockHandlerConfiguration.class)
@ConditionalOnWebApplication(type = REACTIVE)
@ConditionalOnClass(WebFluxCallbackManager.class)
@EnableConfigurationProperties(SentinelProperties.class)
@ConditionalOnMissingBean(SentinelGatewayAutoConfiguration.class)
public class SentinelWebfluxAutoConfiguration implements SmartInitializingSingleton, ApplicationContextAware {

	private ApplicationContext applicationContext;

	@Bean
	@ConditionalOnMissingBean
	public WebfluxOriginParser webfluxOriginParser(SentinelProperties sentinelProperties) {
		return new WebfluxRequestOriginParser((sentinelProperties));
	}

	@Bean
	@ConditionalOnMissingBean
	public BlockRequestHandler webfluxBlockRequestHandler(ObjectProvider<List<BlockHandler>> provider) {
		return new DelegatingBlockRequestHandler(new DefaultBlockRequestHandler(), provider);
	}

	@Override
	public void afterSingletonsInstantiated() {
		applicationContext.getBeanProvider(BlockRequestHandler.class)
			.ifAvailable(WebFluxCallbackManager::setBlockHandler);
		applicationContext.getBeanProvider(WebfluxOriginParser.class)
			.ifAvailable(parser -> WebFluxCallbackManager.setRequestOriginParser(parser::parseOrigin));
		applicationContext.getBeanProvider(WebfluxUrlCleaner.class)
			.ifAvailable(cleaner -> WebFluxCallbackManager.setUrlCleaner(
				(exchange, url) -> cleaner.clean(url, exchange)));
	}

	@Override
	public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}
