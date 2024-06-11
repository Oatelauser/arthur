package com.arthur.web.reactive.process;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.reactive.result.method.RequestMappingInfo;

import java.util.Set;

/**
 * 请求方法处理器
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-08
 * @since 1.0
 */
public interface HandleMethodReactiveProcessor {

	/**
	 * 是否支持请求方法
	 *
	 * @param urls          请求地址
	 * @param handlerMethod 请求方法
	 * @return yes or no
	 */
	boolean supports(Set<String> urls, HandlerMethod handlerMethod);

	/**
	 * 处理请求方法
	 *
	 * @param urls               请求地址
	 * @param requestMappingInfo 请求映射信息
	 * @param handlerMethod      请求方法
	 */
	void handleMethod(Set<String> urls, RequestMappingInfo requestMappingInfo,
		HandlerMethod handlerMethod);

}
