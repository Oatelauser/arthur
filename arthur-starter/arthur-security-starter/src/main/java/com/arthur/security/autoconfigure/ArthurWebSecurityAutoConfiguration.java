package com.arthur.security.autoconfigure;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.arthur.security.context.TransmittableThreadLocalSecurityContextHolderStrategy;
import com.arthur.security.crypto.PasswordEncoderFactory;
import com.arthur.security.crypto.PasswordEncoderProvider;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

/**
 * 概要描述
 * <p>
 * 详细描述
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-01-29
 * @since 1.0
 */
@AutoConfiguration
public class ArthurWebSecurityAutoConfiguration {

	@Bean
	@ConditionalOnClass(TransmittableThreadLocal.class)
	SecurityContextHolderStrategy transmittableSecurityContextHolderStrategy() {
		return new TransmittableThreadLocalSecurityContextHolderStrategy();
	}

	@Configuration
	static class PasswordEncoderConfiguration {

		@Bean
		PasswordEncoder passwordEncoder(ObjectProvider<List<PasswordEncoderProvider>> provider) {
			return PasswordEncoderFactory.createDelegatingPasswordEncoder(provider);
		}

	}

}
