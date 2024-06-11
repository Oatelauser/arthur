package com.arthur.auth.uaa.autoconfigure;

import com.arthur.auth.uaa.authentication.CompositeDaoAuthenticationProvider;
import com.arthur.auth.uaa.handler.LogoutSuccessEventHandler;
import com.arthur.oauth2.resource.OAuth2UserDetailsService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

import static com.arthur.oauth2.constant.SecurityConstants.MESSAGE_SOURCE_BEAN_NAME;

/**
 * 应用服务安全配置
 *
 * @author DearYang
 * @date 2022-08-26
 * @since 1.0
 */
@Configuration
public class WebSecurityAutoConfiguration {

    private final ObjectProvider<PasswordEncoder> passwordProvider;
	private final List<OAuth2UserDetailsService> userDetailsServices;

    public WebSecurityAutoConfiguration(ObjectProvider<PasswordEncoder> passwordProvider,
			List<OAuth2UserDetailsService> userDetailsServices) {
        this.passwordProvider = passwordProvider;
		this.userDetailsServices = userDetailsServices;
    }

    @Bean
    public LogoutSuccessEventHandler logoutSuccessEventHandler() {
        return new LogoutSuccessEventHandler();
    }

	@Bean
	public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity security,
			@Qualifier(MESSAGE_SOURCE_BEAN_NAME) MessageSource messageSource) throws Exception {
		security.authorizeHttpRequests(request -> request.requestMatchers("/token/*").permitAll()
				.anyRequest().authenticated())
			.headers().frameOptions().sameOrigin().and()
			.apply(new FormIdentityLoginConfigurer());

        // 自定义DaoAuthenticationProvider
        CompositeDaoAuthenticationProvider authenticationProvider = new CompositeDaoAuthenticationProvider(userDetailsServices);
        authenticationProvider.setMessageSource(messageSource);
        passwordProvider.ifAvailable(authenticationProvider::setPasswordEncoder);

        security.authenticationProvider(authenticationProvider);
        return security.build();
    }

    /**
     * 暴露静态资源
     *
     * @param security {@link HttpSecurity}
     * @return {@link SecurityFilterChain}
     * @throws Exception 安全异常
     */
	@Bean
	@Order(0)
	public SecurityFilterChain webUrlResources(HttpSecurity security) throws Exception {
		return security.securityMatcher("/actuator/**", "/css/**", "/error")
			.authorizeHttpRequests(request -> request.anyRequest().permitAll())
			.requestCache().disable()
			.securityContext().disable()
			.sessionManagement().disable()
			.build();
	}

}
