package com.arthur.plugin.circuitbreaker.key;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * 设置前缀名的{@link KeyResolver}
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-20
 * @since 1.0
 */
public class KeyResolverAdapter implements KeyResolver {

	private final String name;
	private final KeyResolver delegate;

	public KeyResolverAdapter(String name, KeyResolver delegate) {
		Assert.isTrue(StringUtils.hasText(name), "name cannot be empty");
		this.name = name;
		this.delegate = Objects.requireNonNull(delegate, "delegate");
	}

	@Override
	public Mono<String> resolve(ServerWebExchange exchange) {
		return delegate.resolve(exchange).map(value -> name + "." + value);
	}

}
