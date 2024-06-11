package com.arthur.web.servlet.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.Nullable;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Spring Web工具类
 *
 * @author DearYang
 * @date 2022-08-14
 * @since 1.0
 */
public abstract class WebMvcUtils {

	/**
	 * 获取请求参数为{@link MultiValueMap}
	 *
	 * @param request {@link HttpServletRequest}
	 * @return 请求头
	 */
	public static MultiValueMap<String, String> getParameters(HttpServletRequest request) {
		Map<String, String[]> parameters = request.getParameterMap();
		MultiValueMap<String, String> multiParameters = new LinkedMultiValueMap<>(parameters.size());
		for (Map.Entry<String, String[]> entry : parameters.entrySet()) {
			multiParameters.put(entry.getKey(), List.of(entry.getValue()));
		}

		return multiParameters;
	}

	/**
	 * 获取当前的HttpServletRequest对象
	 *
	 * @return {@link HttpServletRequest}
	 */
	public static Optional<HttpServletRequest> getRequest() {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (attributes == null) {
			return Optional.empty();
		}

		return Optional.of(attributes.getRequest());
	}

	/**
	 * 获取当前的HttpServletRequest对象
	 *
	 * @return {@link HttpServletRequest}
	 */
	public static Optional<HttpServletResponse> getResponse() {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (attributes == null) {
			return Optional.empty();
		}

		return Optional.ofNullable(attributes.getResponse());
	}

	/**
	 * 转换{@link NativeWebRequest}为指定的请求类型
	 *
	 * @param requiredType 转换后的请求类型
	 * @param webRequest   {@link NativeWebRequest}
	 * @param <T>          转换请求对象类型
	 * @return 转换后的请求对象
	 */
	public static <T> T getNativeRequest(Class<T> requiredType, NativeWebRequest webRequest) {
		T request = webRequest.getNativeRequest(requiredType);
		if (request == null) {
			throw new IllegalStateException("Current request is not of type ["
				+ requiredType.getName() + "]: " + webRequest);
		}
		return request;
	}

	/**
	 * 转换{@link NativeWebRequest}为指定的响应类型
	 *
	 * @param requiredType 转换后的响应类型
	 * @param webRequest   {@link NativeWebRequest}
	 * @param <T>          转换响应对象类型
	 * @return 转换后的响应对象
	 */
	public static <T> T getNativeResponse(Class<T> requiredType, NativeWebRequest webRequest) {
		T response = webRequest.getNativeResponse(requiredType);
		if (response == null) {
			throw new IllegalStateException("Current response is not of type ["
				+ requiredType.getName() + "]: " + webRequest);
		}
		return response;
	}

	/**
	 * 当获取当前的控制器方法对象
	 *
	 * @param handlerMapping {@link RequestMappingHandlerMapping}
	 * @return {@link HandlerMethod}
	 * @throws Exception 获取{@link HandlerMethod}异常
	 */
	@Nullable
	public static HandlerMethod getHandlerMethod(RequestMappingHandlerMapping handlerMapping) throws Exception {
		Optional<HttpServletRequest> request = getRequest();
		if (request.isEmpty()) {
			return null;
		}
		HandlerExecutionChain handlerExecutionChain = handlerMapping.getHandler(request.get());
		if (handlerExecutionChain == null) {
			return null;
		}
		return (HandlerMethod) handlerExecutionChain.getHandler();
	}

}
