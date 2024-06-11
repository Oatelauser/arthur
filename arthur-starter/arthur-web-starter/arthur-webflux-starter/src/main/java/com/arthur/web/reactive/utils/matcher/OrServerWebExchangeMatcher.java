package com.arthur.web.reactive.utils.matcher;

import org.springframework.util.Assert;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * OR关系匹配器
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-07
 * @since 1.0
 */
public class OrServerWebExchangeMatcher implements ServerWebExchangeMatcher {

	private final Flux<ServerWebExchangeMatcher> matchers;

	public OrServerWebExchangeMatcher(List<ServerWebExchangeMatcher> matchers) {
		Assert.notEmpty(matchers, "matchers cannot be empty");
		this.matchers = Flux.fromIterable(matchers);
	}

	@Override
	public Mono<MatchResult> matches(ServerWebExchange exchange) {
		return matchers.flatMap(matcher -> matcher.matches(exchange))
			.any(MatchResult::isMatch)
			.flatMap(anyMatch -> anyMatch ? MatchResult.match() : MatchResult.notMatch());
	}

}
