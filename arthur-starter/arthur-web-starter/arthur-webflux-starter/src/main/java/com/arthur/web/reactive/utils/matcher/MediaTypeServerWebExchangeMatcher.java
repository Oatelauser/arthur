package com.arthur.web.reactive.utils.matcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.reactive.accept.RequestedContentTypeResolver;
import org.springframework.web.reactive.accept.RequestedContentTypeResolverBuilder;
import org.springframework.web.server.NotAcceptableStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * 媒体类型匹配器
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-08
 * @since 1.0
 */
@SuppressWarnings("unused")
public class MediaTypeServerWebExchangeMatcher implements ServerWebExchangeMatcher {

	private static final Logger LOG = LoggerFactory.getLogger(MediaTypeServerWebExchangeMatcher.class);

	private final List<MediaType> matchingMediaTypes;
	private Set<MediaType> ignoredMediaTypes = Collections.emptySet();
	private Mono<RequestedContentTypeResolver> contentTypeResolver = Mono.just(
		new RequestedContentTypeResolverBuilder().build());

	public MediaTypeServerWebExchangeMatcher(List<MediaType> matchingMediaTypes) {
		Assert.notEmpty(matchingMediaTypes, "matchingMediaTypes cannot be empty");
		Assert.noNullElements(matchingMediaTypes,
			() -> "matchingMediaTypes cannot contain null element.Got " + matchingMediaTypes);
		this.matchingMediaTypes = matchingMediaTypes;
	}

	@Override
	public Mono<MatchResult> matches(ServerWebExchange exchange) {
		return contentTypeResolver.map(resolver -> resolver.resolveMediaTypes(exchange))
			.map(httpRequestMediaTypes -> {
				MimeTypeUtils.sortBySpecificity(httpRequestMediaTypes);
				return httpRequestMediaTypes;
			})
			.flatMap(this::doMatches)
			.doOnError(NotAcceptableStatusException.class, ex -> {
				if (LOG.isDebugEnabled()) {
					LOG.debug("Failed to parse MediaTypes, returning false", ex);
				}
			})
			.onErrorResume(ex -> MatchResult.notMatch());
	}

	private Mono<MatchResult> doMatches(List<MediaType> httpRequestMediaTypes) {
		return Flux.fromIterable(httpRequestMediaTypes).filter(this::shouldIgnoreMediaType)
			.flatMap(httpRequestMediaType -> Flux.fromIterable(this.matchingMediaTypes)
				.any(matchingMediaType -> matchingMediaType.isCompatibleWith(httpRequestMediaType))
			).any(anyMatch -> anyMatch)
			.flatMap(anyMatch -> anyMatch ? MatchResult.match() : MatchResult.notMatch());
	}

	private boolean shouldIgnoreMediaType(MediaType httpRequestMediaType) {
		for (MediaType ignoredMediaType : ignoredMediaTypes) {
			if (httpRequestMediaType.includes(ignoredMediaType)) {
				return true;
			}
		}
		return false;
	}

	public void setContentTypeResolver(RequestedContentTypeResolver contentTypeResolver) {
		this.contentTypeResolver = Mono.just(contentTypeResolver);
	}

	public Set<MediaType> getIgnoredMediaTypes() {
		return ignoredMediaTypes;
	}

	public void setIgnoredMediaTypes(Set<MediaType> ignoredMediaTypes) {
		this.ignoredMediaTypes = ignoredMediaTypes;
	}

}
