package com.arthur.cloud.openfeign.component;

import com.arthur.cloud.openfeign.fallback.OpenFeignFallback;
import com.arthur.cloud.openfeign.utils.WebServerResponse;
import com.arthur.common.exception.WebServerException;
import com.arthur.common.response.ServerResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Feign异常解码器
 *
 * @author DearYang
 * @date 2022-09-07
 * @see OpenFeignFallback
 * @since 1.0
 */
public class OpenFeignErrorDecoder implements ErrorDecoder {

	private final ObjectMapper objectMapper;

	public OpenFeignErrorDecoder(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Override
	public Exception decode(String methodKey, Response response) {
		ServerResponse serverResponse;
		try {
			String body = Util.toString(response.body().asReader(StandardCharsets.UTF_8));
			serverResponse = objectMapper.readValue(body, ServerResponse.class);
			if (ObjectUtils.isEmpty(serverResponse)) {
				serverResponse = WebServerResponse.ofStatus(body, HttpStatus.INTERNAL_SERVER_ERROR, "Feign remote call [" + methodKey + "] error");
			}
		} catch (IOException e) {
			serverResponse = WebServerResponse.ofStatus(HttpStatus.INTERNAL_SERVER_ERROR, "Feign parse fallback error message fail");
		}

		return new WebServerException(serverResponse);
	}

}
