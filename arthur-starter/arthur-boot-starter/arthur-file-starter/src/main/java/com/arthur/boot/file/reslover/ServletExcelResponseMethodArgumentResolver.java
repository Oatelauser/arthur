package com.arthur.boot.file.reslover;

import com.arthur.boot.file.excel.HttpExcelResponse;
import com.arthur.web.servlet.utils.WebMvcUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.MethodParameter;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;


/**
 * 支持{@link HttpExcelResponse}响应对象的控制器方法参数注入
 *
 * @author DearYang
 * @date 2022-10-01
 * @since 1.0
 */
public class ServletExcelResponseMethodArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return HttpExcelResponse.class.isAssignableFrom(parameter.getParameterType());
	}

	@Override
	public Object resolveArgument(@NonNull MethodParameter parameter, ModelAndViewContainer mavContainer,
	  		@NonNull NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
		if (mavContainer != null) {
			mavContainer.setRequestHandled(true);
		}

		HttpServletResponse response = WebMvcUtils.getNativeResponse(HttpServletResponse.class, webRequest);
		return new HttpExcelResponse(response);
	}

}
