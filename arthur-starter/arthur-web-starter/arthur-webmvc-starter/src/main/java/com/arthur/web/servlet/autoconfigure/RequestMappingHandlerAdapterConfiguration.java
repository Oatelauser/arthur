package com.arthur.web.servlet.autoconfigure;

import com.arthur.boot.utils.BeanUtils;
import com.arthur.common.lifecycle.InitializeListener;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.lang.NonNull;
import org.springframework.util.CollectionUtils;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.List;

/**
 * {@link RequestMappingHandlerAdapter}参数配置类
 *
 * @author DearYang
 * @date 2022-09-28
 * @since 1.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnBean(RequestMappingHandlerAdapter.class)
public class RequestMappingHandlerAdapterConfiguration implements InitializeListener, ApplicationContextAware, Ordered {

	private ApplicationContext applicationContext;
	private final RequestMappingHandlerAdapter handlerAdapter;

	public RequestMappingHandlerAdapterConfiguration(RequestMappingHandlerAdapter handlerAdapter) {
		this.handlerAdapter = handlerAdapter;
	}

	/**
	 * 配置自定义{@link HandlerMethodReturnValueHandler}
	 */
	private void configureHandlerMethodReturnValueHandlers() {
		List<HandlerMethodReturnValueHandler> handlers = BeanUtils.getBeanOfList(applicationContext,
			HandlerMethodReturnValueHandler.class);
		if (CollectionUtils.isEmpty(handlers)) {
			return;
		}

		// 排序
		handlers = BeanUtils.sort(handlers);
		List<HandlerMethodReturnValueHandler> targetHandlers = handlerAdapter.getReturnValueHandlers();
		if (CollectionUtils.isEmpty(targetHandlers)) {
			handlerAdapter.setReturnValueHandlers(handlers);
			return;
		}

		// 删除已经注册过的
		handlers.removeIf(targetHandlers::contains);
		if (!CollectionUtils.isEmpty(handlers)) {
			handlers.addAll(targetHandlers);
			handlerAdapter.setReturnValueHandlers(handlers);
		}
	}

	/**
	 * 配置自定义{@link HandlerMethodArgumentResolver}
	 */
	private void configureHandlerMethodArgumentResolver() {
		List<HandlerMethodArgumentResolver> resolvers = BeanUtils.getBeanOfList(applicationContext,
			HandlerMethodArgumentResolver.class);
		if (CollectionUtils.isEmpty(resolvers)) {
			return;
		}

		// 排序
		resolvers = BeanUtils.sort(resolvers);
		List<HandlerMethodArgumentResolver> targetResolvers = handlerAdapter.getArgumentResolvers();
		if (CollectionUtils.isEmpty(targetResolvers)) {
			handlerAdapter.setArgumentResolvers(resolvers);
			return;
		}

		// 删除已经注册过的
		resolvers.removeIf(targetResolvers::contains);
		if (!CollectionUtils.isEmpty(resolvers)) {
			resolvers.addAll(targetResolvers);
			handlerAdapter.setArgumentResolvers(resolvers);
		}
	}

	@Override
	public void start() {
		this.configureHandlerMethodArgumentResolver();
		this.configureHandlerMethodReturnValueHandlers();
	}

	@Override
	public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE;
	}

}
