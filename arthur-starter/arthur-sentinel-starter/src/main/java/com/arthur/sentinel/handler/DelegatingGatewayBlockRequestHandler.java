package com.arthur.sentinel.handler;

import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.arthur.sentinel.handler.DelegatingBlockExceptionHandler.getHandlers;

/**
 * Spring Cloud Gateway 阻塞请求处理器
 *
 * @author DearYang
 * @date 2022-09-14
 * @since 1.0
 */
public class DelegatingGatewayBlockRequestHandler implements BlockRequestHandler {

	private final List<BlockHandler> delegates;
	private final BlockRequestHandler defaultHandler;

	public DelegatingGatewayBlockRequestHandler(BlockRequestHandler defaultHandler,
			ObjectProvider<List<BlockHandler>> provider) {
		this.defaultHandler = defaultHandler;
		this.delegates = getHandlers(provider);
	}

	@Override
	public Mono<ServerResponse> handleRequest(ServerWebExchange exchange, Throwable t) {
		for (BlockHandler delegate : delegates) {
			if (delegate.support((BlockException) t)) {
				return delegate.handleException(exchange, (BlockException) t);
			}
		}

		return defaultHandler.handleRequest(exchange, t);
	}

}
