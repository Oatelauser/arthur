package com.arthur.sentinel.handler;

import com.alibaba.csp.sentinel.adapter.spring.webflux.callback.BlockRequestHandler;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static com.arthur.sentinel.constant.WebfluxConstants.FALLBACK_SERVER_RESPONSE;


/**
 * Webflux 阻塞请求处理器
 *
 * @author DearYang
 * @date 2022-09-12
 * @since 1.0
 */
public class DefaultBlockRequestHandler implements BlockRequestHandler {

	@Override
	public Mono<ServerResponse> handleRequest(ServerWebExchange exchange, Throwable t) {
		return FALLBACK_SERVER_RESPONSE;
	}

}
