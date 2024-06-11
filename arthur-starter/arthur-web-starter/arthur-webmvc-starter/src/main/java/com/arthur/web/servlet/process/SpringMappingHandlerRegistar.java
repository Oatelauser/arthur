package com.arthur.web.servlet.process;

import com.arthur.web.annotation.SpringMapping;
import com.arthur.web.servlet.constant.SearchStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration.EnableWebMvcConfiguration;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.StringValueResolver;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

/**
 * 自定义注册接口，支持两种注解的API注册，完全兼容MVC的注册流程
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-04-20
 * @see org.springframework.web.bind.annotation.RequestMapping
 * @see SpringMapping
 * @since 1.0
 */
@SuppressWarnings("SpellCheckingInspection")
public class SpringMappingHandlerRegistar implements InitializingBean, EmbeddedValueResolverAware {

	private static final Logger LOG = LoggerFactory.getLogger(SpringMappingHandlerRegistar.class);

	private final RequestMappingHandlerMapping delegate;
	private final SpringMappingHandlerMapping springMapping;

	public SpringMappingHandlerRegistar(RequestMappingHandlerMapping delegate,
			EnableWebMvcConfiguration webMvcConfiguration) {
		this.delegate = delegate;
		this.springMapping = new WebmvcConfigurationOperator(webMvcConfiguration)
			.requestMappingHandlerMapping(delegate);
	}

	/**
	 * 注册当前控制器中标识注解的方法请求
	 *
	 * @param handler 控制器
	 * @see org.springframework.web.bind.annotation.RequestMapping
	 * @see SpringMapping
	 */
	public void registerRequestMapping(Object handler) {
		springMapping.detectHandlerMethods(handler);
	}

	/**
	 * @see #registerRequestMapping(Method, Object, Class)
	 */
	public void registerRequestMapping(Method method, Object handler) {
		this.registerRequestMapping(method, handler, handler.getClass());
	}

	/**
	 * 注册请求
	 *
	 * @param method      指定方法上获取请求注解
	 * @param handler     方法所在的执行器对象
	 * @param handlerType 指定类上获取请求注解
	 * @see org.springframework.web.bind.annotation.RequestMapping
	 * @see SpringMapping
	 */
	public void registerRequestMapping(Method method, Object handler, Class<?> handlerType) {
		RequestMappingInfo requestMappingInfo = springMapping.getMappingForMethod(method, handlerType);
		this.registerRequestMapping(method, handler, requestMappingInfo);
	}

	/**
	 * 注册API接口
	 *
	 * @param method             接口方法
	 * @param handler            控制器
	 * @param requestMappingInfo API接口信息
	 */
	public void registerRequestMapping(Method method, Object handler, RequestMappingInfo requestMappingInfo) {
		if (requestMappingInfo != null) {
			delegate.registerMapping(requestMappingInfo, handler, method);
			return;
		}

		if (LOG.isInfoEnabled()) {
			LOG.info("Cannot register handler[{}] method[{}]", handler, method);
		}
	}

	/**
	 * @see SpringMappingHandlerMapping#getMappingForElement(AnnotatedElement, SearchStrategy, Class, SearchStrategy)
	 */
	public RequestMappingInfo getMappingForElement(AnnotatedElement element, SearchStrategy strategy,
			@Nullable Class<?> handlerType, SearchStrategy handlerStrategy) {
		return springMapping.getMappingForElement(element, strategy, handlerType, handlerStrategy);
	}

	@Override
	public void setEmbeddedValueResolver(@NonNull StringValueResolver resolver) {
		springMapping.setEmbeddedValueResolver(resolver);
	}

	@Override
	public void afterPropertiesSet() {
		springMapping.setApplicationContext(new StaticApplicationContext());
		springMapping.afterPropertiesSet();
	}

}
