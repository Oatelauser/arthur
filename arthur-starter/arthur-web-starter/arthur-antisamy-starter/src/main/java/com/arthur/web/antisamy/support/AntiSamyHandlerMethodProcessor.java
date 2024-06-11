package com.arthur.web.antisamy.support;

import com.arthur.boot.utils.AnnotationUtils;
import com.arthur.web.antisamy.annotation.AntiSamy;
import com.arthur.web.servlet.process.HandlerMethodProcessor;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import java.util.Set;

/**
 * {@link HandlerMethod}处理器解析{@link AntiSamy}注解
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-08
 * @since 1.0
 */
public class AntiSamyHandlerMethodProcessor implements HandlerMethodProcessor {

	private final AnnotationHandler annotationHandler;

	public AntiSamyHandlerMethodProcessor(AnnotationHandler annotationHandler) {
		this.annotationHandler = annotationHandler;
	}

	@Override
	public boolean supports(Set<String> urls, HandlerMethod handlerMethod) {
		return AnnotationUtils.findMethodAnnotation(handlerMethod.getMethod(),
			AntiSamy.class, handlerMethod.getBeanType()) != null
			&& AnnotationHandler.supports(handlerMethod);
	}

	@Override
	public void handleMethod(Set<String> urls, RequestMappingInfo requestMappingInfo,
		HandlerMethod handlerMethod) {
		AntiSamy annotation = AnnotationUtils.findMergedMethodAnnotation(handlerMethod.getMethod(),
			AntiSamy.class, handlerMethod.getBeanType());


		this.annotationHandler.handleAnnotation(urls, annotation);
	}

}
