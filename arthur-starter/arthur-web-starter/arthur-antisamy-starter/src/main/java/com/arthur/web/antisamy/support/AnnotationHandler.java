package com.arthur.web.antisamy.support;

import com.arthur.web.antisamy.annotation.AntiSamy;
import org.springframework.core.MethodParameter;
import org.springframework.util.ObjectUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.multipart.support.MultipartResolutionDelegate;

import java.util.Set;

/**
 * {@link AntiSamy}注解处理器
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-08
 * @since 1.0
 */
public interface AnnotationHandler {

	/**
	 * 处理注解对象
	 *
	 * @param antiSamy 注解对象
	 */
	void handleAnnotation(Set<String> urls, AntiSamy antiSamy);

	/**
	 * 排除文件上传的请求方法
	 *
	 * @param handlerMethod 请求方法
	 * @return 是否支持
	 */
	static boolean supports(HandlerMethod handlerMethod) {
		MethodParameter[] methodParameters = handlerMethod.getMethodParameters();
		if (ObjectUtils.isEmpty(methodParameters)) {
			return true;
		}

		for (MethodParameter methodParameter : methodParameters) {
			if (MultipartResolutionDelegate.isMultipartArgument(methodParameter)) {
				return false;
			}
		}
		return true;
	}

}
