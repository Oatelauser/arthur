package com.arthur.sentinel.autoconfigure;

import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;
import com.arthur.sentinel.callback.WebfluxOriginParser;
import com.arthur.sentinel.handler.BlockHandler;
import com.arthur.sentinel.handler.DefaultGatewayBlockRequestHandler;
import com.arthur.sentinel.handler.DelegatingGatewayBlockRequestHandler;
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
 * Sentinel Gateway 自动配置类
 *
 * @author DearYang
 * @date 2022-09-12
 * @since 1.0
 */
@AutoConfiguration
@Import(BlockHandlerConfiguration.class)
@ConditionalOnWebApplication(type = REACTIVE)
@ConditionalOnClass(GatewayCallbackManager.class)
@EnableConfigurationProperties(SentinelProperties.class)
@ConditionalOnMissingBean(SentinelWebfluxAutoConfiguration.class)
public class SentinelGatewayAutoConfiguration implements SmartInitializingSingleton, ApplicationContextAware {

	private ApplicationContext applicationContext;

	@Bean
	@ConditionalOnMissingBean
	public WebfluxOriginParser gatewayOriginParser(SentinelProperties sentinelProperties) {
		return new WebfluxRequestOriginParser(sentinelProperties);
	}

	@Bean
	@ConditionalOnMissingBean
	public BlockRequestHandler gatewayBlockRequestHandler(ObjectProvider<List<BlockHandler>> provider) {
		return new DelegatingGatewayBlockRequestHandler(new DefaultGatewayBlockRequestHandler(), provider);
	}

	@Override
	public void afterSingletonsInstantiated() {
		applicationContext.getBeanProvider(WebfluxOriginParser.class)
			.ifAvailable(parser -> GatewayCallbackManager.setRequestOriginParser(parser::parseOrigin));
		applicationContext.getBeanProvider(BlockRequestHandler.class)
			.ifAvailable(GatewayCallbackManager::setBlockHandler);
	}

	@Override
	public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}
