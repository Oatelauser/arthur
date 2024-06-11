/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.arthur.gateway.autoconfigure;

import com.arthur.gateway.handler.GatewayErrorWebExceptionHandler;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.reactive.HiddenHttpMethodFilter;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

/**
 * The type Error handler configuration.
 *
 * @see org.springframework.boot.autoconfigure.web.reactive.error.ErrorWebFluxAutoConfiguration
 */
@ConditionalOnClass(WebFluxConfigurer.class)
@AutoConfiguration(before = WebFluxAutoConfiguration.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@EnableConfigurationProperties({ServerProperties.class, WebProperties.class})
public class WebfluxErrorHandlerConfiguration {


	private final ServerProperties serverProperties;

	public WebfluxErrorHandlerConfiguration(ServerProperties serverProperties) {
		this.serverProperties = serverProperties;
	}

	@Bean
	@Order(-1)
	@ConditionalOnMissingBean(value = ErrorWebExceptionHandler.class, search = SearchStrategy.CURRENT)
	public ErrorWebExceptionHandler webfluxExceptionHandler(ErrorAttributes errorAttributes,
			WebProperties webProperties, ObjectProvider<ViewResolver> viewResolvers,
			ServerCodecConfigurer serverCodecConfigurer, ApplicationContext applicationContext) {
		DefaultErrorWebExceptionHandler exceptionHandler = new GatewayErrorWebExceptionHandler(errorAttributes,
			webProperties.getResources(), this.serverProperties.getError(), applicationContext);
		exceptionHandler.setViewResolvers(viewResolvers.orderedStream().collect(Collectors.toList()));
		exceptionHandler.setMessageWriters(serverCodecConfigurer.getWriters());
		exceptionHandler.setMessageReaders(serverCodecConfigurer.getReaders());
		return exceptionHandler;
	}

	/**
	 * Hidden http method filter hidden http method filter.
	 *
	 * @return the hidden http method filter
	 * @see <a href="https://github.com/spring-cloud/spring-cloud-gateway/issues/541">issues-541</a>
	 */
	@Bean
	public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
		return new HiddenHttpMethodFilter() {

			@Override
			@NonNull
			public Mono<Void> filter(@NonNull final ServerWebExchange exchange, @NonNull final WebFilterChain chain) {
				return chain.filter(exchange);
			}
		};
	}

}
