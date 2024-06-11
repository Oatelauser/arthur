package com.arthur.web.reactive.utils.matcher;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 拓展{@link ServerWebExchangeMatcher}新增数据保存能力
 * <p>
 * 参考实现{@code org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcherEntry}
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-08
 * @since 1.0
 */
public record ServerWebExchangeMatcherEntry<T>(ServerWebExchangeMatcher matcher, T entry)
	implements ServerWebExchangeMatcher {

	@Override
	public Mono<MatchResult> matches(ServerWebExchange exchange) {
		return matcher.matches(exchange).flatMap(matchResult ->
			MatchResultExpand.newInstance(matchResult, entry));
	}

}
