package com.arthur.web.reactive.support;

import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.lang.NonNull;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.client.ExchangeStrategies;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 内容插入工具
 * <p>
 * 参考{@code org.springframework.cloud.gateway.support.BodyInserterContext}
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-09
 * @see BodyInserter.Context
 * @since 1.0
 */
public class BodyInserterContext implements BodyInserter.Context {

	private final ExchangeStrategies exchangeStrategies;

	public BodyInserterContext() {
		this.exchangeStrategies = ExchangeStrategies.withDefaults();
	}

	public BodyInserterContext(ExchangeStrategies exchangeStrategies) {
		this.exchangeStrategies = exchangeStrategies;
	}

	@NonNull
	@Override
	public List<HttpMessageWriter<?>> messageWriters() {
		return this.exchangeStrategies.messageWriters();
	}

	@NonNull
	@Override
	public Optional<ServerHttpRequest> serverRequest() {
		return Optional.empty();
	}

	@NonNull
	@Override
	public Map<String, Object> hints() {
		return Collections.emptyMap();
	}

}
