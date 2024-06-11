package com.arthur.sentinel.callback;

import org.springframework.web.server.ServerWebExchange;

/**
 * 基于Webflux自定义{@link com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.RequestOriginParser}
 *
 * @author DearYang
 * @date 2022-09-12
 * @since 1.0
 */
public interface WebfluxOriginParser {

	/**
	 * 解析{@link ServerWebExchange}中的IP地址
	 *
	 * @param exchange {@link ServerWebExchange}
	 * @return IP地址
	 */
	String parseOrigin(ServerWebExchange exchange);

}
