package com.arthur.sentinel.handler;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.arthur.sentinel.constant.WebfluxConstants.*;


/**
 * Webflux 阻塞请求处理工具类
 *
 * @author DearYang
 * @date 2022-09-14
 * @since 1.0
 */
@SuppressWarnings("unused")
public abstract class BlockHandlers {

	public static class FlowBlockHandler implements BlockHandler {

		@Override
		public boolean support(BlockException ex) {
			return ex instanceof FlowException;
		}

		@Override
		public Mono<ServerResponse> handleException(ServerWebExchange exchange, BlockException ex) {
			return FLOW_SERVER_RESPONSE;
		}

	}

	public static class ParamFlowBlockHandler implements BlockHandler {

		@Override
		public boolean support(BlockException ex) {
			return ex instanceof ParamFlowException;
		}

		@Override
		public Mono<ServerResponse> handleException(ServerWebExchange exchange, BlockException ex) {
			return PARAM_FLOW_SERVER_RESPONSE;
		}

	}

	public static class DegradeBlockHandler implements BlockHandler {

		@Override
		public boolean support(BlockException ex) {
			return ex instanceof DegradeException;
		}

		@Override
		public Mono<ServerResponse> handleException(ServerWebExchange exchange, BlockException ex) {
			return DEGRADE_SERVER_RESPONSE;
		}

	}

	public static class AuthorityBlockHandler implements BlockHandler {

		@Override
		public boolean support(BlockException ex) {
			return ex instanceof AuthorityException;
		}

		@Override
		public Mono<ServerResponse> handleException(ServerWebExchange exchange, BlockException ex) {
			return AUTHORITY_SERVER_RESPONSE;
		}

	}

	public static class SystemBlockHandler implements BlockHandler {

		@Override
		public boolean support(BlockException ex) {
			return ex instanceof SystemBlockException;
		}

		@Override
		public Mono<ServerResponse> handleException(ServerWebExchange exchange, BlockException ex) {
			return SYSTEM_SERVER_RESPONSE;
		}

	}

	/**
	 * 获取默认的异常处理器
	 *
	 * @return {@link BlockHandler}
	 */
	public List<BlockHandler> getHandlers() {
		return List.of(
			new FlowBlockHandler(),
			new SystemBlockHandler(),
			new DegradeBlockHandler(),
			new AuthorityBlockHandler(),
			new ParamFlowBlockHandler()
		);
	}

}
