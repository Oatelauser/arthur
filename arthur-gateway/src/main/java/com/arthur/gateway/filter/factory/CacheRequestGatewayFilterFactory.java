package com.arthur.gateway.filter.factory;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.HttpMethod;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;

/**
 * Request Body 缓存过滤器
 * <p>
 * 解决流不能重复读取问题
 *
 * @author DearYang
 * @date 2022-09-12
 * @since 1.0
 */
public class CacheRequestGatewayFilterFactory extends AbstractGatewayFilterFactory<CacheRequestGatewayFilterFactory.Config> {

	@Override
	public GatewayFilter apply(Config config) {
		GatewayFilter filter = new CacheRequestGatewayFilter();
		if (config.order != null) {
			filter = new OrderedGatewayFilter(filter, config.order);
		}

		return filter;
	}

	public static class CacheRequestGatewayFilter implements GatewayFilter {

		@Override
		public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
			HttpMethod httpMethod = exchange.getRequest().getMethod();
			// 排除GET、DELETE请求
			if (httpMethod == null || GET.equals(httpMethod) || DELETE.equals(httpMethod)) {
				return chain.filter(exchange);
			}

			return ServerWebExchangeUtils.cacheRequestBodyAndRequest(exchange, request -> {
				if (request == exchange.getRequest()) {
					return chain.filter(exchange);
				}

				return chain.filter(exchange.mutate().request(request).build());
			});
		}

		@Override
		public List<String> shortcutFieldOrder() {
			return List.of("order");
		}

	}

	@SuppressWarnings("unused")
	public static class Config {

		private Integer order;

		public Integer getOrder() {
			return order;
		}

		public void setOrder(Integer order) {
			this.order = order;
		}
	}

}
