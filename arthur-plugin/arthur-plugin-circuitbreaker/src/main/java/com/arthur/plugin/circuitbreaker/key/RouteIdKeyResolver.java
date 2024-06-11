package com.arthur.plugin.circuitbreaker.key;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_PREDICATE_MATCHED_PATH_ROUTE_ID_ATTR;

/**
 * 路由ID解析器
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-20
 * @since 1.0
 */
public class RouteIdKeyResolver implements NamedKeyResolver {

	@Override
	public Mono<String> resolve(ServerWebExchange exchange) {
		String routeId = exchange.getAttribute(GATEWAY_PREDICATE_MATCHED_PATH_ROUTE_ID_ATTR);
		return Mono.justOrEmpty(routeId);
	}

}
