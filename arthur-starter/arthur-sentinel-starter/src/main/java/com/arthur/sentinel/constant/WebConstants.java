package com.arthur.sentinel.constant;

import com.arthur.sentinel.utils.WebServerResponse;
import com.arthur.common.response.ServerResponse;
import org.springframework.http.HttpStatus;

/**
 * WebMVC Sentinel异常响应类
 *
 * @author DearYang
 * @date 2022-09-14
 * @since 1.0
 */
public interface WebConstants {

	/**
	 * 兜底阻塞异常响应
	 */
	ServerResponse FALLBACK_RESPONSE = WebServerResponse.ofStatus(HttpStatus.TOO_MANY_REQUESTS, "Too many requests, Please retry later");

	/**
	 * 流控规则异常响应
	 */
	ServerResponse FLOW_RESPONSE = WebServerResponse.ofStatus(HttpStatus.TOO_MANY_REQUESTS, "Too many requests are exceeded current limit threshold, Please retry later");

	/**
	 * 熔断规则异常响应
	 */
	ServerResponse DEGRADE_RESPONSE = WebServerResponse.ofStatus(HttpStatus.REQUEST_TIMEOUT, "Response is too slowed, Please retry later");

	/**
	 * 热点规则异常响应
	 */
	ServerResponse PARAM_FLOW_RESPONSE = WebServerResponse.ofStatus(HttpStatus.TOO_MANY_REQUESTS, "Too many requests access hotspot param rules, Please retry later");

	/**
	 * 黑白名单异常响应
	 */
	ServerResponse AUTHORITY_RESPONSE = WebServerResponse.ofStatus(HttpStatus.FORBIDDEN, "Forbidden access, remote ip address is in blacklist");

	/**
	 * 系统规则异常响应
	 */
	ServerResponse SYSTEM_RESPONSE = WebServerResponse.ofStatus(HttpStatus.FORBIDDEN, "Forbidden access, insufficient system resources");
}
