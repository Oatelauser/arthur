package com.arthur.web.reactive.utils.matcher;

import reactor.core.publisher.Mono;

/**
 * 记录{@link ServerWebExchangeMatcher}匹配结果
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-07
 * @since 1.0
 */
public class MatchResult {

	private final boolean match;

	public MatchResult(boolean match) {
		this.match = match;
	}

	public boolean isMatch() {
		return match;
	}

	public static Mono<MatchResult> negate(MatchResult matchResult) {
		return matchResult.match ? Holder.NOT_MATCH : Holder.MATCH;
	}

	public static Mono<MatchResult> writeTo(MatchResult matchResult) {
		return matchResult.match ? Holder.MATCH : Holder.NOT_MATCH;
	}

	public static Mono<MatchResult> match() {
		return Holder.MATCH;
	}

	public static Mono<MatchResult> notMatch() {
		return Holder.NOT_MATCH;
	}

	private static class Holder {
		private static final Mono<MatchResult> MATCH = Mono.just(new MatchResult(true));
		private static final Mono<MatchResult> NOT_MATCH = Mono.just(new MatchResult(false));
	}

}
