package com.arthur.web.antisamy.constant;

import org.springframework.core.Ordered;

/**
 * AntiSamy常量
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-06
 * @since 1.0
 */
public interface AntiSamyConstants {

	/**
	 * 通配符
	 */
	String WILD_CARD = "*";

	/**
	 * Webflux AntiSamy Filter
	 */
	String FILTER_BEAN_NAME = "antiSamyWebfluxFilter";

	/**
	 * AntiSamy实现的XSS过滤器order
	 */
	int ORDER = Ordered.HIGHEST_PRECEDENCE + 1;

	/**
	 * 缓存{@link com.arthur.web.antisamy.context.AntiSamyService}对象
	 *
	 * @see org.springframework.web.server.ServerWebExchange
	 */
	String ANTISAMY_SERVICE_PARAMETER = "__ANTISAMY_SERVICE__";

}
