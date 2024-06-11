package com.arthur.boot.autoconfigure;

import com.arthur.boot.context.format.ConversionServiceAware;
import com.arthur.boot.process.AwareFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Aware配置类
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-04-28
 * @since 1.0
 */
@Configuration(proxyBeanMethods = false)
public class SpringAwareConfiguration {

	@Bean
	@ConditionalOnBean
	public AwareFactoryBean formatterRegistryAware() {
		return new AwareFactoryBean(ConversionServiceAware.class);
	}


}
