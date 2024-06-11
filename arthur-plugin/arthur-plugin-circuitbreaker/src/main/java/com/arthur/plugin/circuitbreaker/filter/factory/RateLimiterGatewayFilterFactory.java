package com.arthur.plugin.circuitbreaker.filter.factory;

import com.arthur.plugin.circuitbreaker.key.KeyResolverAdapter;
import com.arthur.plugin.circuitbreaker.key.KeyResolverFactory;
import com.arthur.plugin.circuitbreaker.ratelimit.RedisLuaRateLimiter;
import com.arthur.plugin.result.ArthurResult;
import com.arthur.plugin.result.ArthurStatusEnum;
import com.arthur.web.reactive.server.WebServerResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.RequestRateLimiterGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RateLimiter;
import org.springframework.cloud.gateway.support.HttpStatusHolder;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

import static com.arthur.plugin.util.GatewayFilterFormatter.obtainsGatewayFilterFactoryEnabledKey;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.setResponseStatus;

/**
 * 拓展网关的请求限流过滤器
 *
 * @author DearYang
 * @date 2022-08-11
 * @see org.springframework.cloud.gateway.filter.factory.RequestRateLimiterGatewayFilterFactory
 * @since 1.0
 */
@SuppressWarnings({ "rawtypes", "unused" })
@ConfigurationProperties("spring.cloud.gateway.filter.request-mapping-rate-limiter")
public class RateLimiterGatewayFilterFactory extends AbstractRedisGatewayFilterFactory<RateLimiterGatewayFilterFactory.Config>
		implements EnvironmentPostProcessor {

	private final RateLimiter defaultRateLimiter;
	private final List<RateLimiter> rateLimiters;
	private final WebServerResponse webServerResponse;

	public RateLimiterGatewayFilterFactory(KeyResolver defaultKeyResolver,
			KeyResolverFactory keyResolverFactory, RateLimiter defaultRateLimiter,
			List<RateLimiter> rateLimiters, WebServerResponse webServerResponse) {
		super("RateLimit", defaultKeyResolver, keyResolverFactory);
		this.rateLimiters = rateLimiters;
		this.webServerResponse = webServerResponse;
		this.defaultRateLimiter = defaultRateLimiter;
	}

	@Override
	public GatewayFilter apply(Config config) {
		KeyResolver keyResolver = this.obtainKeyResolver(config);
		KeyResolver resolver = new KeyResolverAdapter(prefixName, keyResolver);
		int hashCode = resolver.hashCode();
		HttpStatusHolder emptyKeyStatus = this.resolveEmptyKeyStatus(config);
		boolean denyEmptyKey = this.isDenyEmptyKey(config);
		RateLimiter<Object> limiter = this.getRateLimiter(config);

		return (exchange, chain) -> resolver.resolve(exchange)
			.flatMap(key -> {
				String routeId = this.resolveRouteId(config, exchange);
				routeId += hashCode;

				return limiter.isAllowed(routeId, key).flatMap(response -> {
					for (Map.Entry<String, String> header : response.getHeaders().entrySet()) {
						exchange.getResponse().getHeaders().add(header.getKey(), header.getValue());
					}

					return response.isAllowed() ? chain.filter(exchange) : fallback(config, exchange);
				});
			})
			.switchIfEmpty(Mono.defer(() -> {
				if (denyEmptyKey) {
					setResponseStatus(exchange, emptyKeyStatus);
					return denyEmptyKey(exchange);
				}
				return chain.filter(exchange);
			}));
	}

	protected Mono<Void> fallback(Config config, ServerWebExchange exchange) {
		ServerWebExchangeUtils.setResponseStatus(exchange, config.getStatusCode());
		Object error = ArthurResult.ofError(exchange, ArthurStatusEnum.FALLBACK_DEFAULT, null);
		return webServerResponse.writeTo(error, exchange);
		//return WebFluxResultUtils.result(exchange, error);
	}

	@Override
	protected Mono<Void> denyEmptyKey(ServerWebExchange exchange) {
		Object error = ArthurResult.ofError(exchange, ArthurStatusEnum.RATE_LIMIT_DENY_EMPTY, null);
		return webServerResponse.writeTo(error, exchange);
		//return WebFluxResultUtils.result(exchange, error);
	}

	@Override
	public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
		String propertyName = obtainsGatewayFilterFactoryEnabledKey(RequestRateLimiterGatewayFilterFactory.class);
		environment.getPropertySources().addFirst(new MapPropertySource(this.getClass().getSimpleName(),
			Map.of(propertyName, Boolean.FALSE.toString())
		));
	}

	@SuppressWarnings("unchecked")
	private RateLimiter<Object> getRateLimiter(Config config) {
		String rateLimitAlgorithm = config.getRateLimitAlgorithm();
		if (config.getRateLimitAlgorithm() != null && !CollectionUtils.isEmpty(rateLimiters)) {
			for (RateLimiter rateLimiter : rateLimiters) {
				if (rateLimiter instanceof RedisLuaRateLimiter limiter
					&& limiter.supports(rateLimitAlgorithm)) {
					return rateLimiter;
				}
			}
		}

		return getOrDefault(config.getRateLimiter(), defaultRateLimiter);
	}

	private <T> T getOrDefault(T configValue, T defaultValue) {
		return (configValue != null) ? configValue : defaultValue;
	}

	public RateLimiter getDefaultRateLimiter() {
		return defaultRateLimiter;
	}

	public List<RateLimiter> getRateLimiters() {
		return rateLimiters;
	}

	public static class Config extends AbstractRedisGatewayFilterFactory.Config {

		private String rateLimitAlgorithm;
		private RateLimiter rateLimiter;

		public String getRateLimitAlgorithm() {
			return rateLimitAlgorithm;
		}

		public void setRateLimitAlgorithm(String rateLimitAlgorithm) {
			this.rateLimitAlgorithm = rateLimitAlgorithm;
		}

		public RateLimiter getRateLimiter() {
			return rateLimiter;
		}

		public void setRateLimiter(RateLimiter rateLimiter) {
			this.rateLimiter = rateLimiter;
		}
	}

}
