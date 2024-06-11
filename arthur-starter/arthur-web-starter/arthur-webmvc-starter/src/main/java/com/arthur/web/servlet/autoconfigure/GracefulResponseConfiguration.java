package com.arthur.web.servlet.autoconfigure;

import com.arthur.web.model.ServerResponse;
import com.arthur.web.servlet.process.ServletExceptionResponseProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Method;

import static org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type.SERVLET;

/**
 * SpringMVC统一响应配置
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-05-17
 * @since 1.0
 */
@Configuration(proxyBeanMethods = false)
public class GracefulResponseConfiguration {

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnWebApplication(type = SERVLET)
	public ServletExceptionResponseProcessor servletExceptionResponseProcessor() {
		return new ServletExceptionResponseProcessor();
	}

	@RestControllerAdvice
	public static class GracefulResponseBodyAdvice implements ResponseBodyAdvice<Object> {

		@Override
		public boolean supports(@NonNull MethodParameter returnType,
				@NonNull Class<? extends HttpMessageConverter<?>> converterType) {
			Method method = returnType.getMethod();
			if (method == null) {
				return false;
			}
			Class<?> returnClass = method.getReturnType();
			return !ServerResponse.class.isAssignableFrom(returnClass) && !Void.TYPE.equals(returnClass);
		}

		@Override
		public Object beforeBodyWrite(Object body, @NonNull MethodParameter returnType, @NonNull MediaType selectedContentType,
				@NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
				@NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response) {
			if (body == null) {
				return ServerResponse.ofSuccess();
			}
			if (body instanceof String || body instanceof byte[]) {
				return body;
			}
			return ServerResponse.ofSuccess(body);
		}
	}

}
