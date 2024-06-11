package com.arthur.web.reactive.filter;

import com.arthur.web.reactive.utils.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.lang.NonNull;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import static com.arthur.web.constant.RemoteIpConstants.REMOTE_FILTER_ORDER;
import static com.arthur.web.constant.RemoteIpConstants.REMOTE_IP_ATTR;

/**
 * 远程IP解析过滤器
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-03-01
 * @since 1.0
 */
public class WebfluxRemoteIpWebFilter implements WebFilter, Ordered {

	@NonNull
	@Override
	public Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
		String remoteIp = ServerWebExchangeUtils.resolveRemoteIp(exchange);
		exchange.getAttributes().put(REMOTE_IP_ATTR, remoteIp);
		return chain.filter(exchange);
	}

	@Override
	public int getOrder() {
		return REMOTE_FILTER_ORDER;
	}

}
