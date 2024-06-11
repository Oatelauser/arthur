package com.arthur.web.reactive.utils.matcher;

import org.springframework.util.Assert;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 取反关系匹配器
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-07
 * @since 1.0
 */
public class NegatedServerWebExchangeMatcher implements ServerWebExchangeMatcher {

	private final Mono<ServerWebExchangeMatcher> matcher;

	public NegatedServerWebExchangeMatcher(ServerWebExchangeMatcher matcher) {
		Assert.notNull(matcher, "matcher cannot be empty");
		this.matcher = Mono.just(matcher);
	}

	@Override
	public Mono<MatchResult> matches(ServerWebExchange exchange) {
		return matcher.flatMap(matcher -> matcher.matches(exchange))
			.flatMap(this::negate);
	}

	private Mono<MatchResult> negate(MatchResult matchResult) {
		return matchResult.isMatch() ? MatchResult.notMatch() : MatchResult.match();
	}

}
