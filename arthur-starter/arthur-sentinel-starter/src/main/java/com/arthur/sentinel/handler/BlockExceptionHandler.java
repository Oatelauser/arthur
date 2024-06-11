package com.arthur.sentinel.handler;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;


/**
 * Spring MVC Sentinel 异常处理器
 *
 * @author DearYang
 * @date 2022-09-14
 * @since 1.0
 */
public interface BlockExceptionHandler extends Ordered {

	/**
	 * 是否支持该异常处理
	 *
	 * @param ex {@link BlockException}
	 * @return yes or no
	 */
	boolean support(BlockException ex);

	/**
	 * Handle the request when blocked.
	 *
	 * @param request  Servlet request
	 * @param response Servlet response
	 * @param ex       the block exception
	 * @throws Exception users may throw out the BlockException or other error occurs
	 */
	void handleException(HttpServletRequest request, HttpServletResponse response, BlockException ex) throws Exception;

	@Override
	default int getOrder() {
		return 0;
	}

}
