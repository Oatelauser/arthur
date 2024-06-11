package com.arthur.web.reactive.utils.matcher;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 结合请求类型和请求方式的组合匹配器
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-13
 * @since 1.0
 */
public class MediaTypeMethodServerWebExchangeMatcher extends MediaTypeServerWebExchangeMatcher {

	/**
	 * 取反
	 */
	private boolean negate = false;
	private List<HttpMethod> allowHttpMethods = List.of(HttpMethod.POST, HttpMethod.PUT);

	public MediaTypeMethodServerWebExchangeMatcher(List<MediaType> matchingMediaTypes) {
		super(matchingMediaTypes);
	}

	@Override
	public Mono<MatchResult> matches(ServerWebExchange exchange) {
		if (!allowHttpMethods.contains(exchange.getRequest().getMethod())) {
			return MatchResult.notMatch();
		}
		return super.matches(exchange).flatMap(matchResult -> negate ?
			MatchResult.negate(matchResult) : MatchResult.writeTo(matchResult));
	}

	public void setHttpMethods(List<HttpMethod> matchingHttpMethods) {
		this.allowHttpMethods = matchingHttpMethods;
	}

	public void setNegate(boolean negate) {
		this.negate = negate;
	}

}
