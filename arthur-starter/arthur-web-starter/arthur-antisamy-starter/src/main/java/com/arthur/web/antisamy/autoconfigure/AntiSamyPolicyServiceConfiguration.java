package com.arthur.web.antisamy.autoconfigure;

import com.arthur.web.antisamy.context.AntiSamyPolicyService;
import com.arthur.web.antisamy.context.InMemoryPolicyService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * AntiSamy策略配置类
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-06
 * @since 1.0
 */
@Configuration(proxyBeanMethods = false)
public class AntiSamyPolicyServiceConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public AntiSamyPolicyService inMemoryPolicyService(AntiSamyProperties properties) {
		return new InMemoryPolicyService(properties);
	}

}
