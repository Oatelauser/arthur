package com.arthur.lang.collection;

/**
 * Java元组
 *
 * @author DearYang
 * @date 2022-07-29
 * @since 1.0
 */
@SuppressWarnings("unused")
public record Tuple<A, B>(A first, B second) {

	public static <A, B> Tuple<A, B> of(A first, B second) {
		return new Tuple<>(first, second);
	}

}
