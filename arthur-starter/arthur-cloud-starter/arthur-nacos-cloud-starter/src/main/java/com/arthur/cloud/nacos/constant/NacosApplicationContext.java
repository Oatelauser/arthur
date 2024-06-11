package com.arthur.cloud.nacos.constant;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * Nacos Cloud常量类
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-03-03
 * @since 1.0
 */
@SuppressWarnings("SpellCheckingInspection")
public interface NacosApplicationContext {

	/**
	 * 空实现的 Spring {@link org.springframework.core.env.Environment}
	 */
	ConfigurableEnvironment EMPTY_ENVIRONMENT = new AbstractEnvironment() {
	};

	/**
	 * 空实现的 Spring {@link ApplicationContext}
	 */
	ApplicationContext EMPTY_APPLICATION_CONTEXT = getApplicationContext();

	private static ApplicationContext getApplicationContext() {
		StaticApplicationContext applicationContext = new StaticApplicationContext();
		((ConfigurableApplicationContext) applicationContext).setEnvironment(EMPTY_ENVIRONMENT);
		return applicationContext;
	}

}
