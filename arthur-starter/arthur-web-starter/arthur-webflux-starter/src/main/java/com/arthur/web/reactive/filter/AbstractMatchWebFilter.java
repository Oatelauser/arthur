package com.arthur.web.reactive.filter;

import com.arthur.web.reactive.utils.matcher.MatchResult;
import com.arthur.web.reactive.utils.matcher.ServerWebExchangeMatcher;
import org.springframework.lang.NonNull;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * 抽象匹配过滤器
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-09
 * @since 1.0
 */
public abstract class AbstractMatchWebFilter implements WebFilter {

	private final ServerWebExchangeMatcher matcher;

	protected AbstractMatchWebFilter(ServerWebExchangeMatcher matcher) {
		this.matcher = matcher;
	}

	@NonNull
	@Override
	public Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
		return matcher.matches(exchange).filter(MatchResult::isMatch)
			.switchIfEmpty(Mono.defer(() -> chain.filter(exchange).then(Mono.empty())))
			.flatMap(matchResult -> this.doFilter(matchResult, exchange, chain));
	}

	protected abstract Mono<Void> doFilter(MatchResult matchResult, ServerWebExchange exchange, WebFilterChain chain);

}
