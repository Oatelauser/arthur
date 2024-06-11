package com.arthur.sentinel.handler;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.core.Ordered;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Webflux Sentinel 异常处理器
 *
 * @author DearYang
 * @date 2022-09-14
 * @since 1.0
 */
public interface BlockHandler extends Ordered {

	/**
	 * 是否支持该异常处理
	 *
	 * @param ex {@link BlockException}
	 * @return yes or no
	 */
	boolean support(BlockException ex);

	/**
	 * Handle the blocked request.
	 *
	 * @param exchange server exchange object
	 * @param ex        block exception
	 * @return server response to return
	 */
	Mono<ServerResponse> handleException(ServerWebExchange exchange, BlockException ex);

	@Override
	default int getOrder() {
		return 0;
	}

}
