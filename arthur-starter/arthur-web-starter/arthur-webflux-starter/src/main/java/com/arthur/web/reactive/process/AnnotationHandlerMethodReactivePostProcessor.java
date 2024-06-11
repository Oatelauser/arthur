package com.arthur.web.reactive.process;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.lang.NonNull;
import org.springframework.util.CollectionUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.reactive.result.method.RequestMappingInfo;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPattern;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.arthur.boot.constant.BootConstants.SPRING_BASE_PACKAGE;

/**
 * 处理Controller中的请求方法后置处理器
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-08
 * @since 1.0
 */
@SuppressWarnings("unused")
public class AnnotationHandlerMethodReactivePostProcessor implements BeanFactoryPostProcessor, SmartInitializingSingleton {

	private static final Logger LOG = LoggerFactory.getLogger(AnnotationHandlerMethodReactivePostProcessor.class);

	private ConfigurableListableBeanFactory beanFactory;
	private final List<HandleMethodReactiveProcessor> handleMethodProcessors;

	public AnnotationHandlerMethodReactivePostProcessor(ObjectProvider<List<HandleMethodReactiveProcessor>> provider) {
		this.handleMethodProcessors = provider.getIfAvailable();
	}

	public void postProcessHandleMethods(Map<RequestMappingInfo, HandlerMethod> handlerMethods) {
		for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
			RequestMappingInfo requestMappingInfo = entry.getKey();
			HandlerMethod handlerMethod = entry.getValue();
			if (!this.shouldIgnoreHandleMethod(handlerMethod)) {
				this.postProcessHandleMethod(requestMappingInfo, handlerMethod);
			}
		}
	}

	@SuppressWarnings("ConstantConditions")
	public void postProcessHandleMethod(RequestMappingInfo requestMappingInfo, HandlerMethod handlerMethod) {
		Set<String> urls = requestMappingInfo.getPatternsCondition().getPatterns()
			.stream().map(PathPattern::getPatternString).collect(Collectors.toSet());
		for (HandleMethodReactiveProcessor processor : handleMethodProcessors) {
			if (processor.supports(urls, handlerMethod)) {
				if (LOG.isDebugEnabled()) {
					LOG.debug("processor [{}] handle url [{}] and method [{}}]",
						processor, urls, handlerMethod);
				}

				try {
					processor.handleMethod(urls, requestMappingInfo, handlerMethod);
				} catch (Exception e) {
					if (LOG.isDebugEnabled()) {
						LOG.debug("processor [{}] handle method [{}}] has error: {}",
							processor, handlerMethod, e.getLocalizedMessage());
					}
				}
			}
		}
	}

	/**
	 * 排出Spring内部的请求方法
	 *
	 * @param handlerMethod 请求方法
	 * @return yes or no
	 */
	private boolean shouldIgnoreHandleMethod(HandlerMethod handlerMethod) {
		return handlerMethod.getBeanType().getName().contains(SPRING_BASE_PACKAGE);
	}

	@Override
	public void afterSingletonsInstantiated() {
		RequestMappingHandlerMapping handlerMapping = beanFactory.getBean(RequestMappingHandlerMapping.class);
		Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();
		if (!CollectionUtils.isEmpty(handlerMethods)) {
			this.postProcessHandleMethods(handlerMethods);
		}
	}

	@Override
	public void postProcessBeanFactory(@NonNull ConfigurableListableBeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

}
