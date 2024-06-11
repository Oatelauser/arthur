package com.arthur.web.reactive.utils.matcher;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link ServerWebExchangeMatcher}工厂类
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-07
 * @since 1.0
 */
@SuppressWarnings("unused")
public abstract class ServerWebExchangeMatcherFactory {

	public static ServerWebExchangeMatcher and(ServerWebExchangeMatcher... andMatchers) {
		return and(List.of(andMatchers));
	}

	public static ServerWebExchangeMatcher and(List<ServerWebExchangeMatcher> andMatchers) {
		return new AndServerWebExchangeMatcher(andMatchers);
	}

	public static ServerWebExchangeMatcher or(ServerWebExchangeMatcher... orMatchers) {
		return or(List.of(orMatchers));
	}

	public static ServerWebExchangeMatcher or(List<ServerWebExchangeMatcher> orMatchers) {
		return new OrServerWebExchangeMatcher(orMatchers);
	}

	public static ServerWebExchangeMatcher not(ServerWebExchangeMatcher notMatcher) {
		return new NegatedServerWebExchangeMatcher(notMatcher);
	}

	public static ServerWebExchangeMatcher path(String urlPattern) {
		return new PathPatternParserServerWebExchangeMatcher(urlPattern, null);
	}

	public static ServerWebExchangeMatcher path(String urlPattern, HttpMethod httpMethod) {
		return new PathPatternParserServerWebExchangeMatcher(urlPattern, httpMethod);
	}

	public static ServerWebExchangeMatcher paths(HttpMethod httpMethod, List<String> urlPatterns) {
		Assert.notEmpty(urlPatterns, "urlPatterns cannot be empty");
		if (urlPatterns.size() == 1) {
			return path(urlPatterns.get(0), httpMethod);
		}

		List<ServerWebExchangeMatcher> pathMatchers = new ArrayList<>(urlPatterns.size());
		for (String urlPattern : urlPatterns) {
			pathMatchers.add(path(urlPattern, httpMethod));
		}
		return new OrServerWebExchangeMatcher(pathMatchers);
	}

	public static ServerWebExchangeMatcher paths(HttpMethod httpMethod, String... urlPatterns) {
		return paths(httpMethod, List.of(urlPatterns));
	}

	public static <T> ServerWebExchangeMatcher medias(List<MediaType> matchingMediaTypes) {
		return new MediaTypeServerWebExchangeMatcher(matchingMediaTypes);
	}

	public static <T> ServerWebExchangeMatcherEntry<T> entry(T entry, ServerWebExchangeMatcher matcher) {
		return new ServerWebExchangeMatcherEntry<>(matcher, entry);
	}

	/**
	 * 匹配任何的{@link org.springframework.web.server.ServerWebExchange}
	 */
	public static ServerWebExchangeMatcher any() {
		return exchange -> MatchResult.match();
	}

	/**
	 * 不匹配任何的{@link org.springframework.web.server.ServerWebExchange}
	 */
	public static ServerWebExchangeMatcher anyNot() {
		return exchange -> MatchResult.notMatch();
	}

}
