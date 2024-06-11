package com.arthur.web.reactive.process;

import com.arthur.web.process.ExceptionResponseProcessor;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.reactive.result.method.RequestMappingInfo;

import java.util.Set;

/**
 * Webflux实现的{@link com.arthur.web.process.ExceptionResponseProcessor}
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-04-19
 * @since 1.0
 */
public class WebfluxExceptionResponseProcessor extends ExceptionResponseProcessor implements HandleMethodReactiveProcessor {

	@Override
	public boolean supports(Set<String> urls, HandlerMethod handlerMethod) {
		return true;
	}

	@Override
	public void handleMethod(Set<String> urls, RequestMappingInfo requestMappingInfo, HandlerMethod handlerMethod) {
		this.processHandlerMethod(handlerMethod);
	}

}
