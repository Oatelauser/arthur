package com.arthur.cloud.openfeign.utils;

import com.arthur.common.response.GenericServerResponse;
import com.arthur.common.response.ServerResponse;
import org.springframework.http.HttpStatus;

/**
 * copy by {@code com.broadtech.arthur.spring.web.utils.WebServerResponse}
 *
 * 拓展{@link ServerResponse}提供对{@link HttpStatus}的支持
 *
 * @author DearYang
 * @date 2022-09-08
 * @since 1.0
 */
public class WebServerResponse {

	public static ServerResponse ofStatus(HttpStatus status, String msg) {
		return ofStatus(null, status, msg);
	}

	public static ServerResponse ofStatus(Object data, HttpStatus status, String msg) {
		if (status.isError()) {
			return ServerResponse.ofError(String.valueOf(status.value()), msg);
		}

		return ServerResponse.ofSuccess(msg, data);
	}

	public static <T> GenericServerResponse<T> ofGenericStatus(HttpStatus status, String msg) {
		return ofGenericStatus(null, status, msg);
	}

	public static <T> GenericServerResponse<T> ofGenericStatus(T data, HttpStatus status, String msg) {
		if (status.isError()) {
			return GenericServerResponse.ofError(String.valueOf(status.value()), msg);
		}

		return GenericServerResponse.ofSuccess(msg, data);
	}

}
