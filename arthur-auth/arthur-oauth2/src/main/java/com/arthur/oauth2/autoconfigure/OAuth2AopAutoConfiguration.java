package com.arthur.oauth2.autoconfigure;

import com.arthur.oauth2.annotation.AuthPermissionAdvisor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Role;

import static org.springframework.beans.factory.config.BeanDefinition.ROLE_INFRASTRUCTURE;

/**
 * OAuth2 AOP自动配置类
 *
 * @author DearYang
 * @date 2022-08-18
 * @since 1.0
 */
@AutoConfiguration
public class OAuth2AopAutoConfiguration {

	@Bean
	@Role(ROLE_INFRASTRUCTURE)
	public AuthPermissionAdvisor authAdvisor(HttpServletRequest request) {
		return new AuthPermissionAdvisor(request);
	}

}
