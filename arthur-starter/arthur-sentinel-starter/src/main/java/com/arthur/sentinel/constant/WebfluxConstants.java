package com.arthur.sentinel.constant;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * Webflux Sentinel异常响应类
 *
 * @author DearYang
 * @date 2022-09-14
 * @since 1.0
 */
public interface WebfluxConstants {

	/**
	 * 远程IP
	 */
	String REMOTE_IP_ATTR = WebfluxConstants.class.getName() + "." + "remoteIp";

	/**
	 * 默认兜底异常响应
	 */
	Mono<ServerResponse> FALLBACK_SERVER_RESPONSE = getServerResponse(WebConstants.FALLBACK_RESPONSE);

	/**
	 * 流控规则异常响应
	 */
	Mono<ServerResponse> FLOW_SERVER_RESPONSE = getServerResponse(WebConstants.FLOW_RESPONSE);

	/**
	 * 熔断规则异常响应
	 */
	Mono<ServerResponse> DEGRADE_SERVER_RESPONSE = getServerResponse(WebConstants.DEGRADE_RESPONSE);

	/**
	 * 热点规则异常响应
	 */
	Mono<ServerResponse> PARAM_FLOW_SERVER_RESPONSE = getServerResponse(WebConstants.PARAM_FLOW_RESPONSE);

	/**
	 * 黑白名单异常响应
	 */
	Mono<ServerResponse> AUTHORITY_SERVER_RESPONSE = getServerResponse(WebConstants.AUTHORITY_RESPONSE);

	/**
	 * 系统规则异常响应
	 */
	Mono<ServerResponse> SYSTEM_SERVER_RESPONSE = getServerResponse(WebConstants.SYSTEM_RESPONSE);


	static Mono<ServerResponse> getServerResponse(com.arthur.common.response.ServerResponse response) {
		return ServerResponse.status(HttpStatus.TOO_MANY_REQUESTS.value())
			.contentType(MediaType.APPLICATION_JSON)
			.body(BodyInserters.fromValue(response));
	}

}
