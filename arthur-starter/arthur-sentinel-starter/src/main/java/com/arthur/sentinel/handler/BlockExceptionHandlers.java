package com.arthur.sentinel.handler;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.arthur.common.response.ServerResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static com.arthur.sentinel.constant.WebConstants.*;


/**
 * WebMVC 阻塞异常处理器工具
 *
 * @author DearYang
 * @date 2022-09-14
 * @since 1.0
 */
@SuppressWarnings("unused")
public abstract class BlockExceptionHandlers {

	static abstract class AbstractBlockExceptionHandler implements BlockExceptionHandler {

		volatile String response;
		final ObjectMapper objectMapper;

		AbstractBlockExceptionHandler(ObjectMapper objectMapper) {
			this.objectMapper = objectMapper;
		}

		@Override
		public void handleException(HttpServletRequest request, HttpServletResponse response, BlockException ex) throws Exception {
			response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			if (this.response == null) {
				this.response = objectMapper.writeValueAsString(getServerResponse());
			}
			response.getWriter().print(this.response);
		}

		abstract ServerResponse getServerResponse();

	}

	public static class FlowBlockExceptionHandler extends AbstractBlockExceptionHandler {

		public FlowBlockExceptionHandler(ObjectMapper objectMapper) {
			super(objectMapper);
		}

		@Override
		public boolean support(BlockException ex) {
			return ex instanceof FlowException;
		}

		@Override
		ServerResponse getServerResponse() {
			return FLOW_RESPONSE;
		}
	}

	public static class ParamFlowBlockExceptionHandler extends AbstractBlockExceptionHandler {

		public ParamFlowBlockExceptionHandler(ObjectMapper objectMapper) {
			super(objectMapper);
		}

		@Override
		public boolean support(BlockException ex) {
			return ex instanceof ParamFlowException;
		}

		@Override
		ServerResponse getServerResponse() {
			return PARAM_FLOW_RESPONSE;
		}
	}

	public static class DegradeBlockExceptionHandler extends AbstractBlockExceptionHandler {

		public DegradeBlockExceptionHandler(ObjectMapper objectMapper) {
			super(objectMapper);
		}

		@Override
		public boolean support(BlockException ex) {
			return ex instanceof DegradeException;
		}

		@Override
		ServerResponse getServerResponse() {
			return DEGRADE_RESPONSE;
		}

	}

	public static class AuthorityBlockExceptionHandler extends AbstractBlockExceptionHandler {

		public AuthorityBlockExceptionHandler(ObjectMapper objectMapper) {
			super(objectMapper);
		}

		@Override
		public boolean support(BlockException ex) {
			return ex instanceof AuthorityException;
		}

		@Override
		ServerResponse getServerResponse() {
			return AUTHORITY_RESPONSE;
		}

	}

	public static class SystemBlockExceptionHandler extends AbstractBlockExceptionHandler {

		public SystemBlockExceptionHandler(ObjectMapper objectMapper) {
			super(objectMapper);
		}

		@Override
		public boolean support(BlockException ex) {
			return ex instanceof SystemBlockException;
		}

		@Override
		ServerResponse getServerResponse() {
			return SYSTEM_RESPONSE;
		}

	}

	public static List<BlockExceptionHandler> getHandlers(ObjectMapper objectMapper) {
		return List.of(
			new FlowBlockExceptionHandler(objectMapper),
			new SystemBlockExceptionHandler(objectMapper),
			new DegradeBlockExceptionHandler(objectMapper),
			new ParamFlowBlockExceptionHandler(objectMapper),
			new AuthorityBlockExceptionHandler(objectMapper)
		);
	}

}
