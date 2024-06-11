package com.arthur.web.antisamy.web;

import com.arthur.web.antisamy.autoconfigure.AntiSamyProperties;
import com.arthur.web.antisamy.autoconfigure.AntiSamyProperties.AntiSamyRouteConfig;
import com.arthur.web.antisamy.context.AntiSamyPolicyService;
import com.arthur.web.antisamy.context.AntiSamyService;
import com.arthur.web.reactive.filter.HttpOutputMessageHandler;
import com.arthur.web.reactive.filter.ModifyRequestBodyWebFilter;
import com.arthur.web.reactive.support.CachedBodyHttpOutputMessage;
import com.arthur.web.reactive.utils.matcher.*;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.arthur.web.antisamy.constant.AntiSamyConstants.ANTISAMY_SERVICE_PARAMETER;
import static com.arthur.web.antisamy.constant.AntiSamyConstants.ORDER;

/**
 * AntiSamy Webflux Filter
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-06
 * @since 1.0
 */
@SuppressWarnings("unused")
public class AntiSamyWebFilter extends ModifyRequestBodyWebFilter<String> implements AntiSamyFilterSupport, Ordered {

	/**
	 * JSON请求匹配器
	 */
	private static final ServerWebExchangeMatcher JSON_REQUEST_MATCHER =
		new MediaTypeMethodServerWebExchangeMatcher(List.of(MediaType.APPLICATION_JSON));

    private ServerWebExchangeMatcher matcher;
    private final AntiSamyPolicyService antiSamyService;

    private List<HttpMethod> allowHttpMethods;
    private List<MediaType> includeMediaTypes;
    private List<MediaType> excludeMediaTypes;

    public AntiSamyWebFilter(AntiSamyProperties properties, AntiSamyPolicyService antiSamyService,
            HttpOutputMessageHandler<String> outputMessageHandler,
            ObjectProvider<List<HttpMessageReader<?>>> provider) {
        super(String.class, String.class, outputMessageHandler, provider);
        this.antiSamyService = antiSamyService;
        this.combineMatcher(properties);
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        return matcher.matches(exchange).filter(MatchResult::isMatch)
                .map(matchResult -> (MatchResultExpand<AntiSamyRouteConfig>) matchResult)
                .filter(matchResult -> this.shouldIgnoreRequest(matchResult.getEntry(), exchange.getRequest()))
                .switchIfEmpty(Mono.defer(() -> chain.filter(exchange).then(Mono.empty())))
                .flatMap(matchResult -> this.doFilter(matchResult.getEntry(), exchange, chain));
    }

	protected Mono<Void> doFilter(AntiSamyRouteConfig config, ServerWebExchange exchange, WebFilterChain chain) {
		AntiSamyService antiSamyService = this.getAntiSamyService(config, this.antiSamyService);
		return JSON_REQUEST_MATCHER.matches(exchange).filter(MatchResult::isMatch)
			.flatMap(ignore -> chain.filter(new AntiSamyServerWebExchangeDecorator(exchange, antiSamyService)))
			.switchIfEmpty(Mono.defer(() -> this.doFilter(antiSamyService, exchange, chain)));
	}

    private Mono<Void> doFilter(AntiSamyService antiSamyService, ServerWebExchange exchange, WebFilterChain chain) {
		exchange.getAttributes().put(ANTISAMY_SERVICE_PARAMETER, antiSamyService);
        return super.filter(exchange, chain)
                .doFinally(ignore -> exchange.getAttributes().remove(ANTISAMY_SERVICE_PARAMETER));
    }

    @Override
    protected Mono<Void> doFilter(HttpHeaders headers, CachedBodyHttpOutputMessage outputMessage,
            ServerWebExchange exchange, WebFilterChain chain) {
        AntiSamyService antiSamyService = exchange.getAttribute(ANTISAMY_SERVICE_PARAMETER);
        ServerHttpRequest request = new AntiSamyServerHttpRequest(headers, outputMessage,
                exchange.getRequest(), antiSamyService);
		return chain.filter(exchange.mutate().request(request).build());
    }

    private boolean shouldIgnoreRequest(AntiSamyRouteConfig config, ServerHttpRequest request) {
        return ObjectUtils.containsElement(config.getMethod(), request.getMethod().name());
    }

    /**
     * 加载{@link ServerWebExchangeMatcher}
     *
     * @param properties AntiSamy配置信息
     */
    public void combineMatcher(AntiSamyProperties properties) {
        Map<String, AntiSamyRouteConfig> routes = properties.getRoutes();
        if (CollectionUtils.isEmpty(routes)) {
            this.matcher = ServerWebExchangeMatcherFactory.anyNot();
            return;
        }

        List<ServerWebExchangeMatcher> matchers = new ArrayList<>(routes.size());
        for (Map.Entry<String, AntiSamyRouteConfig> entry : routes.entrySet()) {
            AntiSamyRouteConfig config = entry.getValue();
            ServerWebExchangeMatcher path = ServerWebExchangeMatcherFactory.path(entry.getKey());
            if (config.isDefenseQueryBody()) {
                path = this.combineMatcher(path);
            }
            matchers.add(ServerWebExchangeMatcherFactory.entry(config, path));
        }
        this.matcher = ServerWebExchangeMatcherFactory.or(matchers);
    }

    private ServerWebExchangeMatcher combineMatcher(ServerWebExchangeMatcher matcher) {
        List<ServerWebExchangeMatcher> matchers = new ArrayList<>(3);
        if (!CollectionUtils.isEmpty(includeMediaTypes)) {
            MediaTypeMethodServerWebExchangeMatcher media = new MediaTypeMethodServerWebExchangeMatcher(includeMediaTypes);
            Optional.ofNullable(allowHttpMethods).ifPresent(media::setHttpMethods);
            matchers.add(media);
        }
        if (!CollectionUtils.isEmpty(excludeMediaTypes)) {
            MediaTypeMethodServerWebExchangeMatcher media = new MediaTypeMethodServerWebExchangeMatcher(excludeMediaTypes);
            Optional.ofNullable(allowHttpMethods).ifPresent(media::setHttpMethods);
            media.setNegate(true);
            matchers.add(media);
        }
        if (!matchers.isEmpty()) {
            matchers.add(matcher);
            matcher = ServerWebExchangeMatcherFactory.and(matchers);
        }

        return matcher;
    }

    @Override
    public int getOrder() {
        return ORDER;
    }

    public List<MediaType> getExcludeMediaTypes() {
        return excludeMediaTypes;
    }

    public void setExcludeMediaTypes(List<MediaType> excludeMediaTypes) {
        Assert.notEmpty(excludeMediaTypes, "excludeMediaTypes cannot be empty");
        Assert.noNullElements(excludeMediaTypes, "excludeMediaTypes has null element");
        this.excludeMediaTypes = excludeMediaTypes;
    }

    public List<MediaType> getIncludeMediaTypes() {
        return includeMediaTypes;
    }

    public void setIncludeMediaTypes(List<MediaType> includeMediaTypes) {
        Assert.notEmpty(includeMediaTypes, "includeMediaTypes cannot be empty");
        Assert.noNullElements(includeMediaTypes, "includeMediaTypes has null element");
        this.includeMediaTypes = includeMediaTypes;
    }

    public List<HttpMethod> getAllowHttpMethods() {
        return allowHttpMethods;
    }

    public void setAllowHttpMethods(List<HttpMethod> allowHttpMethods) {
        Assert.notEmpty(allowHttpMethods, "allowHttpMethods cannot be empty");
        Assert.noNullElements(allowHttpMethods, "allowHttpMethods has null element");
        this.allowHttpMethods = allowHttpMethods;
    }

}
