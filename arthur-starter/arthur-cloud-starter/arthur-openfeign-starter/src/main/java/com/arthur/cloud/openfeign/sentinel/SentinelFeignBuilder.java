/*
 * Copyright 2013-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.arthur.cloud.openfeign.sentinel;

import com.alibaba.cloud.sentinel.feign.SentinelContractHolder;
import com.alibaba.cloud.sentinel.feign.SentinelFeign;
import com.arthur.cloud.openfeign.autoconfigure.FallbackProperties;
import com.arthur.cloud.openfeign.fallback.OpenFeignFallbackFactory;
import feign.Contract;
import feign.Feign;
import feign.InvocationHandlerFactory;
import feign.Target;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClientFactory;
import org.springframework.cloud.openfeign.FeignClientFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * copy by {@link SentinelFeign.Builder}
 * <p>
 * 如果开发没有添加{@link FallbackFactory}，则默认设置{@link OpenFeignFallbackFactory}
 *
 * @author DearYang
 * @date 2022-09-07
 * @since 1.0
 */
@SuppressWarnings("all")
public class SentinelFeignBuilder extends Feign.Builder implements ApplicationContextAware {

	private final FallbackProperties fallbackProperties;
	private Contract contract = new Contract.Default();
	private FeignClientFactory feignClientFactory;
	private ApplicationContext applicationContext;
	private boolean eagerlyRegister;

	public SentinelFeignBuilder(FallbackProperties fallbackProperties) {
		this.fallbackProperties = fallbackProperties;
	}

	@Override
	public SentinelFeignBuilder contract(Contract contract) {
		this.contract = contract;
		return this;
	}

	@Override
	public Feign build() {
		super.invocationHandlerFactory(new InvocationHandlerFactory() {
			@Override
			public InvocationHandler create(Target target, Map<Method, MethodHandler> dispatch) {
				/*
				 * Due to the change of the initialization sequence,
				 * BeanFactory.getBean will cause a circular dependency. So
				 * FeignClientFactoryBean can only be obtained from BeanDefinition
				 */
				FeignClientFactoryBean feignClientFactoryBean = determinFeignClientFactoryBean(target);

				Class fallback = feignClientFactoryBean.getFallback();
				Class fallbackFactory = feignClientFactoryBean.getFallbackFactory();
				String beanName = feignClientFactoryBean.getContextId();
				if (!StringUtils.hasText(beanName)) {
					beanName = (String) getFieldValue(feignClientFactoryBean, "name");
				}

				Object fallbackInstance;
				FallbackFactory fallbackFactoryInstance;
				// check fallback and fallbackFactory properties
				if (void.class != fallback) {
					fallbackInstance = getFromContext(beanName, "fallback", fallback,
						target.type());
					return new SentinelInvocationHandler(target, dispatch,
						new FallbackFactory.Default(fallbackInstance));
				}
				if (void.class != fallbackFactory) {
					fallbackFactoryInstance = (FallbackFactory) getFromContext(
						beanName, "fallbackFactory", fallbackFactory,
						FallbackFactory.class);
					return new SentinelInvocationHandler(target, dispatch,
						fallbackFactoryInstance);
				}

				/**
				 * 如果没有设置任何{@link FallbackFactory}，则添加自定义实现的{@link OpenFeignFallbackFactory}
				 */
				fallbackFactoryInstance = new OpenFeignFallbackFactory<>(target, fallbackProperties);
				return new SentinelInvocationHandler(target, dispatch, fallbackFactoryInstance);
			}

			private Object getFromContext(String name, String type, Class fallbackType, Class targetType) {
				Object fallbackInstance = feignClientFactory.getInstance(name, fallbackType);
				if (fallbackInstance == null) {
					throw new IllegalStateException(String.format(
						"No %s instance of type %s found for feign client %s",
						type, fallbackType, name));
				}

				if (!targetType.isAssignableFrom(fallbackType)) {
					throw new IllegalStateException(String.format(
						"Incompatible %s instance. Fallback/fallbackFactory of type %s is not assignable to %s for feign client %s",
						type, fallbackType, targetType, name));
				}
				return fallbackInstance;
			}
		});

		super.contract(new SentinelContractHolder(contract));
		return super.build();
	}

	private Object getFieldValue(Object instance, String fieldName) {
		Field field = ReflectionUtils.findField(instance.getClass(), fieldName);
		field.setAccessible(true);
		try {
			return field.get(instance);
		} catch (IllegalAccessException e) {
			// ignore
		}
		return null;
	}

	// TODO: 等待Sentinel官方修复，这里只是个人解决方案
	private FeignClientFactoryBean determinFeignClientFactoryBean(Target target) {
		GenericApplicationContext gctx = (GenericApplicationContext) applicationContext;
		if (eagerlyRegister) {
			return gctx.getBean("&" + target.type().getName(), FeignClientFactoryBean.class);
		}
		BeanDefinition def = gctx.getBeanDefinition(target.type().getName());
		return  (FeignClientFactoryBean) def
			.getAttribute("feignClientsRegistrarFactoryBean");
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
		this.feignClientFactory = this.applicationContext.getBean(FeignClientFactory.class);
		this.eagerlyRegister = String.valueOf(false).equals(
			applicationContext.getEnvironment().getProperty("spring.cloud.openfeign.lazy-attributes-resolution", String.valueOf(false)));
	}

}
