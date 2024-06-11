package com.arthur.web.reactive.utils.matcher;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.PathContainer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.Assert;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;
import reactor.core.publisher.Mono;

/**
 * 请求路径匹配器
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-07
 * @since 1.0
 */
@SuppressWarnings("unused")
public class PathPatternParserServerWebExchangeMatcher implements ServerWebExchangeMatcher {

	private static final Log LOG = LogFactory.getLog(PathPatternParserServerWebExchangeMatcher.class);

	private static final PathPatternParser DEFAULT_PATTERN_PARSER = new PathPatternParser();

	private final HttpMethod method;
	private final PathPattern pattern;

	public PathPatternParserServerWebExchangeMatcher(PathPattern pattern) {
		this(pattern, null);
	}

	public PathPatternParserServerWebExchangeMatcher(PathPattern pattern, HttpMethod method) {
		Assert.notNull(pattern, "pattern cannot be null");
		this.pattern = pattern;
		this.method = method;
	}

	public PathPatternParserServerWebExchangeMatcher(String pattern, HttpMethod method) {
		Assert.notNull(pattern, "pattern cannot be null");
		this.pattern = DEFAULT_PATTERN_PARSER.parse(pattern);
		this.method = method;
	}

	public PathPatternParserServerWebExchangeMatcher(String pattern) {
		this(pattern, null);
	}

	@Override
	public Mono<MatchResult> matches(ServerWebExchange exchange) {
		ServerHttpRequest request = exchange.getRequest();
		PathContainer path = request.getPath().pathWithinApplication();
		if (this.method != null && !this.method.equals(request.getMethod())) {
			return MatchResult.notMatch().doOnNext(result -> {
				if (LOG.isDebugEnabled()) {
					LOG.debug("Request '" + request.getMethod() + " " + path + "' doesn't match '" + this.method
						+ " " + this.pattern.getPatternString() + "'");
				}
			});
		}

		if (!this.pattern.matches(path)) {
			return MatchResult.notMatch().doOnNext(result -> {
				if (LOG.isDebugEnabled()) {
					LOG.debug("Request '" + request.getMethod() + " " + path + "' doesn't match '" + this.method
						+ " " + this.pattern.getPatternString() + "'");
				}
			});
		}

		if (LOG.isDebugEnabled()) {
			LOG.debug("Checking match of request : '" + path + "'; against '" + this.pattern.getPatternString() + "'");
		}
		return MatchResult.match();
	}

	@Override
	public String toString() {
		return "PathMatcherServerWebExchangeMatcher{" + "pattern='" + this.pattern + '\'' + ", method=" + this.method + '}';
	}

}
