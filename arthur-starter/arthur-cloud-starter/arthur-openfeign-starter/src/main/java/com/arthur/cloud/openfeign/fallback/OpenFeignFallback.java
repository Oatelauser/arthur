package com.arthur.cloud.openfeign.fallback;

import com.arthur.cloud.openfeign.autoconfigure.FallbackProperties;
import com.arthur.cloud.openfeign.component.OpenFeignErrorDecoder;
import com.arthur.cloud.openfeign.constant.FallbackMode;
import com.arthur.cloud.openfeign.utils.WebServerResponse;
import com.arthur.common.exception.WebServerException;
import com.arthur.common.response.ServerResponse;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.http.HttpStatus;

import java.lang.reflect.Method;

/**
 * Fallback处理程序
 *
 * @author DearYang
 * @date 2022-09-07
 * @see OpenFeignFallbackFactory
 * @see OpenFeignErrorDecoder
 * @since 1.0
 */
public class OpenFeignFallback<T> implements MethodInterceptor {

	private static final Logger LOG = LoggerFactory.getLogger(OpenFeignFallback.class);

	private final Class<T> targetType;
	private final String targetName;
	private final Throwable cause;
	private final FallbackProperties fallbackProperties;

	public OpenFeignFallback(Class<T> targetType, String targetName, Throwable cause, FallbackProperties fallbackProperties) {
		this.targetType = targetType;
		this.targetName = targetName;
		this.cause = cause;
		this.fallbackProperties = fallbackProperties;
	}

	@Override
	public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) {
		String errorMessage = cause.getLocalizedMessage();
		LOG.error("OpenFeign fallback:[{}.{}] serviceId:[{}] message:[{}]", targetType.getName(), method.getName(), targetName, errorMessage);

		if (FallbackMode.DEFAULT.equals(fallbackProperties.getMode())) {
			return ServerResponse.ofError(fallbackProperties.getResponseStatus(), fallbackProperties.getResponseBody());
		}

		return handleException(errorMessage);
	}

	private ServerResponse handleException(String errorMessage) {
		// FeignException
		if (cause instanceof FeignException) {
			//此处只是示例，具体可以返回带有业务错误数据的对象
			FeignException exception = (FeignException) cause;
			HttpStatus status = HttpStatus.resolve(exception.status());
			if (status == null || status.is2xxSuccessful()) {
				status = HttpStatus.INTERNAL_SERVER_ERROR;
			}
			return WebServerResponse.ofStatus(status, exception.contentUTF8());
		}

		// 自定义内部异常
		if (cause instanceof WebServerException) {
			WebServerException exception = (WebServerException) cause;
			ServerResponse serverResponse = exception.getServerResponse();
			if (serverResponse != null) {
				return serverResponse;
			}
		}

		return WebServerResponse.ofStatus(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage);
	}

}
