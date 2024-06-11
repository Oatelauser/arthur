package com.arthur.web.reactive.utils.matcher;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 参考实现{@code org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher}
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-07
 * @since 1.0
 */
public interface ServerWebExchangeMatcher {

	/**
	 * 匹配{@link ServerWebExchange}
	 *
	 * @param exchange {@link ServerWebExchange}
	 * @return yes or no
	 */
	Mono<MatchResult> matches(ServerWebExchange exchange);

}
