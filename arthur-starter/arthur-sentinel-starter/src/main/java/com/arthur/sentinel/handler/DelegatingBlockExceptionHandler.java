package com.arthur.sentinel.handler;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.arthur.boot.utils.BeanUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.core.Ordered;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * Sentinel BlockException 处理器
 *
 * @author DearYang
 * @date 2022-09-14
 * @since 1.0
 */
public class DelegatingBlockExceptionHandler implements com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler {

	private final com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler defaultHandler;
	private final List<BlockExceptionHandler> delegates;

	public DelegatingBlockExceptionHandler(com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler defaultHandler,
			ObjectProvider<List<BlockExceptionHandler>> provider) {
		this.defaultHandler = defaultHandler;
		this.delegates = getHandlers(provider);
	}

	static <T extends Ordered> List<T> getHandlers(ObjectProvider<List<T>> provider) {
		List<T> handlers = provider.getIfAvailable();
		if (CollectionUtils.isEmpty(handlers)) {
			return Collections.emptyList();
		}

		return BeanUtils.sort(handlers);
	}

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, BlockException e) throws Exception {
		for (BlockExceptionHandler delegate : delegates) {
			// 匹配第一个就退出
			if (delegate.support(e)) {
				delegate.handleException(request, response, e);
				return;
			}
		}

		this.defaultHandler.handle(request, response, e);
	}

}
