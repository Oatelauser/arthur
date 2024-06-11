package com.arthur.sentinel.callback;

import org.springframework.web.server.ServerWebExchange;

/**
 * 基于Webflux实现{@link com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.UrlCleaner}
 *
 * @author DearYang
 * @date 2022-09-12
 * @since 1.0
 */
public interface WebfluxUrlCleaner {

	/**
	 * 处理URL
	 *
	 * @param originUrl 源URL
	 * @param exchange  {@link ServerWebExchange}
	 * @return URL
	 */
	String clean(String originUrl, ServerWebExchange exchange);

}
