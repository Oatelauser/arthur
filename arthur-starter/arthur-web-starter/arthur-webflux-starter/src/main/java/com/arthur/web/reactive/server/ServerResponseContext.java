package com.arthur.web.reactive.server;

import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.lang.NonNull;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;

import java.util.List;

/**
 * Webflux响应上下文
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-20
 * @since 1.0
 */
public class ServerResponseContext implements ServerResponse.Context {

	private final List<ViewResolver> viewResolvers;
	private final ExchangeStrategies exchangeStrategies;

	public ServerResponseContext(ExchangeStrategies exchangeStrategies,
			List<ViewResolver> viewResolvers) {
		this.viewResolvers = viewResolvers;
		this.exchangeStrategies = exchangeStrategies;
	}

	@NonNull
	@Override
	public List<HttpMessageWriter<?>> messageWriters() {
		return exchangeStrategies.messageWriters();
	}

	@NonNull
	@Override
	public List<ViewResolver> viewResolvers() {
		return viewResolvers;
	}

}
