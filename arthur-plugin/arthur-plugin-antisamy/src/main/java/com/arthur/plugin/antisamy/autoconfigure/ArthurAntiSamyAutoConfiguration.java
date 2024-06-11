package com.arthur.plugin.antisamy.autoconfigure;

import com.arthur.boot.conditional.ConditionalOnGateway;
import com.arthur.plugin.antisamy.AntiSamyGatewayFilterFactory;
import com.arthur.web.antisamy.context.AntiSamyPolicyService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.cloud.gateway.config.conditional.ConditionalOnEnabledFilter;
import org.springframework.context.annotation.Bean;

/**
 * AntiSamy XSS防御网关自动配置类
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-14
 * @since 1.0
 */
@AutoConfiguration
public class ArthurAntiSamyAutoConfiguration {

	@ConditionalOnGateway
	//@Configuration(proxyBeanMethods = false)
	static class AntiSamyGatewayConfiguration {

		@Bean
		@ConditionalOnEnabledFilter
		AntiSamyGatewayFilterFactory antiSamyGatewayFilterFactory(AntiSamyPolicyService antiSamyService) {
			return new AntiSamyGatewayFilterFactory(antiSamyService);
		}

	}

}
