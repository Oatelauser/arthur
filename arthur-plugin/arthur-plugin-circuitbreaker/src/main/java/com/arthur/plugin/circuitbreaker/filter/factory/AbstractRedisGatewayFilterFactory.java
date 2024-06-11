package com.arthur.plugin.circuitbreaker.filter.factory;

import com.arthur.cloud.gateway.AbstractSmartGatewayFilterFactory;
import com.arthur.plugin.circuitbreaker.key.KeyResolverAdapter;
import com.arthur.plugin.circuitbreaker.key.KeyResolverFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.HasRouteId;
import org.springframework.cloud.gateway.support.HttpStatusHolder;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static com.arthur.plugin.circuitbreaker.constant.CircuitBreakerConstant.KEY_RESOLVER_ROUTE_KEY;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.setResponseStatus;

/**
 * 抽象Redis的网关过滤器
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-20
 * @since 1.0
 */
@SuppressWarnings("unused")
public abstract class AbstractRedisGatewayFilterFactory<C extends AbstractRedisGatewayFilterFactory.Config> extends AbstractSmartGatewayFilterFactory<C> {

	/**
	 * Redis Key 前缀名
	 */
	protected final String prefixName;

	/**
	 * 请求拒绝空key
	 */
	private boolean denyEmptyKey = true;

	/**
	 * 默认的key解析器
	 */
	private final KeyResolver defaultKeyResolver;

	private final KeyResolverFactory keyResolverFactory;

	/**
	 * HttpStatus to return when denyEmptyKey is true, defaults to FORBIDDEN.
	 */
	private String emptyKeyStatusCode = HttpStatus.FORBIDDEN.name();

	protected AbstractRedisGatewayFilterFactory(String prefixName,
		KeyResolver defaultKeyResolver, KeyResolverFactory keyResolverFactory) {
		this.prefixName = prefixName;
		this.defaultKeyResolver = defaultKeyResolver;
		this.keyResolverFactory = keyResolverFactory;
	}

	@Override
	public GatewayFilter apply(C config) {
		KeyResolver keyResolver = this.obtainKeyResolver(config);
		KeyResolver resolver = new KeyResolverAdapter(prefixName, keyResolver);
		HttpStatusHolder emptyKeyStatus = this.resolveEmptyKeyStatus(config);
		boolean denyEmptyKey = this.isDenyEmptyKey(config);

		return (exchange, chain) -> resolver.resolve(exchange)
			.flatMap(key -> doFilter(key, config, exchange, chain))
			.switchIfEmpty(Mono.defer(() -> {
				if (denyEmptyKey) {
					setResponseStatus(exchange, emptyKeyStatus);
					return denyEmptyKey(exchange);
				}
				return chain.filter(exchange);
			}));
	}

	/**
	 * 执行Redis业务逻辑过滤器
	 *
	 * @param key      redis key
	 * @param config   配置信息
	 * @param exchange {@link ServerWebExchange}
	 * @param chain    {@link GatewayFilterChain}
	 */
	protected Mono<Void> doFilter(String key, C config, ServerWebExchange exchange, GatewayFilterChain chain) {
		return chain.filter(exchange);
	}

	/**
	 * 处理拒绝空key的请求，直接返回
	 *
	 * @param exchange {@link ServerWebExchange}
	 */
	protected Mono<Void> denyEmptyKey(ServerWebExchange exchange) {
		return exchange.getResponse().setComplete();
	}

	protected KeyResolver obtainKeyResolver(C config) {
		String keyResolverName = config.getKeyResolver();
		if (StringUtils.hasText(keyResolverName)) {
			return defaultKeyResolver;
		}
		if (KEY_RESOLVER_ROUTE_KEY.equals(keyResolverName)) {
			return exchange -> Mono.just(config.routeId);
		}
		KeyResolver keyResolver = keyResolverFactory.get(keyResolverName);
		if (keyResolver == null) {
			keyResolver = defaultKeyResolver;
		}
		return keyResolver;
	}

	/**
	 * 解析路由ID
	 *
	 * @param config   配置获取
	 * @param exchange {@link ServerWebExchange} 获取
	 * @return 路由ID
	 */
	@SuppressWarnings("ConstantConditions")
	public String resolveRouteId(C config, ServerWebExchange exchange) {
		String routeId = config.getRouteId();
		if (routeId == null) {
			Route route = exchange.getAttribute(GATEWAY_ROUTE_ATTR);
			routeId = route.getId();
		}
		return routeId;
	}

	public boolean isDenyEmptyKey(C config) {
		Boolean denyEmptyKey = config.getDenyEmptyKey();
		return denyEmptyKey != null ? denyEmptyKey : this.denyEmptyKey;
	}

	public HttpStatusHolder resolveEmptyKeyStatus(C config) {
		String emptyKeyStatus = config.getEmptyKeyStatus();
		emptyKeyStatus = emptyKeyStatus == null ? this.emptyKeyStatusCode : emptyKeyStatus;
		return HttpStatusHolder.parse(emptyKeyStatus);
	}

	public boolean isDenyEmptyKey() {
		return denyEmptyKey;
	}

	public void setDenyEmptyKey(boolean denyEmptyKey) {
		this.denyEmptyKey = denyEmptyKey;
	}

	public KeyResolver getDefaultKeyResolver() {
		return defaultKeyResolver;
	}

	public String getEmptyKeyStatusCode() {
		return emptyKeyStatusCode;
	}

	public void setEmptyKeyStatusCode(String emptyKeyStatusCode) {
		this.emptyKeyStatusCode = emptyKeyStatusCode;
	}

	public static class Config implements HasRouteId {

		/**
		 * 路由ID
		 */
		protected String routeId;

		/**
		 * {@link KeyResolver}标签ID
		 */
		private String keyResolver;

		/**
		 * {@link AbstractRedisGatewayFilterFactory#denyEmptyKey}
		 */
		private Boolean denyEmptyKey;

		/**
		 * {@link AbstractRedisGatewayFilterFactory#emptyKeyStatusCode}
		 */
		private String emptyKeyStatus;

		/**
		 * 拒绝后的响应码
		 */
		private HttpStatus statusCode = HttpStatus.TOO_MANY_REQUESTS;

		@Override
		public void setRouteId(String routeId) {
			this.routeId = routeId;
		}

		@Override
		public String getRouteId() {
			return routeId;
		}

		public Boolean getDenyEmptyKey() {
			return denyEmptyKey;
		}

		public void setDenyEmptyKey(Boolean denyEmptyKey) {
			this.denyEmptyKey = denyEmptyKey;
		}

		public String getEmptyKeyStatus() {
			return emptyKeyStatus;
		}

		public void setEmptyKeyStatus(String emptyKeyStatus) {
			this.emptyKeyStatus = emptyKeyStatus;
		}

		public String getKeyResolver() {
			return keyResolver;
		}

		public void setKeyResolver(String keyResolver) {
			this.keyResolver = keyResolver;
		}

		public HttpStatus getStatusCode() {
			return statusCode;
		}

		public void setStatusCode(HttpStatus statusCode) {
			this.statusCode = statusCode;
		}

	}

}
