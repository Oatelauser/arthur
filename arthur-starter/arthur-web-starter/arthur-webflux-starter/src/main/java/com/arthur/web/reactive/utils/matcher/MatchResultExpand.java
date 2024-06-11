package com.arthur.web.reactive.utils.matcher;

import reactor.core.publisher.Mono;

/**
 * 拓展{@link MatchResult}新增泛型对象T
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-08
 * @since 1.0
 */
public class MatchResultExpand<T> extends MatchResult {

	/**
	 * 保存的数据
	 */
	private final T entry;

	private MatchResultExpand(boolean matchResult, T entry) {
		super(matchResult);
		this.entry = entry;
	}

	public T getEntry() {
		return entry;
	}

	public static <T> Mono<MatchResultExpand<T>> newInstance(MatchResult matchResult, T entry) {
		return Mono.just(new MatchResultExpand<>(matchResult.isMatch(), entry));
	}

}
