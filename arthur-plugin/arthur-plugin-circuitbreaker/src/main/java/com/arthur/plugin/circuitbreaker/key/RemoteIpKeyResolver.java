package com.arthur.plugin.circuitbreaker.key;

import com.arthur.web.reactive.utils.ServerWebExchangeUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static com.arthur.web.constant.RemoteIpConstants.REMOTE_IP_ATTR;

/**
 * 请求IP地址解析器
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-20
 * @since 1.0
 */
public class RemoteIpKeyResolver implements NamedKeyResolver {

	@Override
	public Mono<String> resolve(ServerWebExchange exchange) {
		String remoteIp = exchange.getAttribute(REMOTE_IP_ATTR);
		if (!StringUtils.hasText(remoteIp)) {
			remoteIp = ServerWebExchangeUtils.resolveRemoteIp(exchange);
			exchange.getAttributes().put(REMOTE_IP_ATTR, remoteIp);
		}

		return Mono.just(remoteIp);
	}

}
