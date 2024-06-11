package com.arthur.web.servlet.process;

import com.arthur.web.process.ExceptionResponseProcessor;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import java.util.Set;

/**
 * Webmvc实现的{@link com.arthur.web.process.ExceptionResponseProcessor}
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-04-19
 * @since 1.0
 */
public class ServletExceptionResponseProcessor extends ExceptionResponseProcessor implements HandlerMethodProcessor {

	@Override
	public boolean supports(Set<String> urls, HandlerMethod handlerMethod) {
		return true;
	}

	@Override
	public void handleMethod(Set<String> urls, RequestMappingInfo requestMappingInfo, HandlerMethod handlerMethod) {
		this.processHandlerMethod(handlerMethod);
	}

}
