package com.arthur.sentinel.handler;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static com.arthur.sentinel.constant.WebConstants.FALLBACK_RESPONSE;


/**
 * Sentinel BlockException 处理器
 *
 * @author DearYang
 * @date 2022-09-12
 * @see BlockExceptionHandler
 * @since 1.0
 */
public class DefaultBlockExceptionHandler implements BlockExceptionHandler {

	private volatile String response;
	private final ObjectMapper objectMapper;

	public DefaultBlockExceptionHandler(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, BlockException e) throws Exception {
		response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		if (this.response == null) {
			this.response = objectMapper.writeValueAsString(FALLBACK_RESPONSE);
		}
		response.getWriter().print(this.response);
	}

}
