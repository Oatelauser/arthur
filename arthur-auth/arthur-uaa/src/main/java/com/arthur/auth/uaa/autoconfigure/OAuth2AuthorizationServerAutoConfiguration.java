package com.arthur.auth.uaa.autoconfigure;

import com.arthur.auth.uaa.authentication.AbstractOAuth2ResourceOwnerAuthenticationProvider;
import com.arthur.auth.uaa.authentication.ArthurOAuth2TokenCustomizer;
import com.arthur.auth.uaa.authentication.ArthurOAuth2TokenGenerator;
import com.arthur.auth.uaa.authentication.CompositeDaoAuthenticationProvider;
import com.arthur.auth.uaa.authentication.password.OAuth2PasswordResourceOwnerAuthenticationConverter;
import com.arthur.auth.uaa.authentication.password.OAuth2PasswordResourceOwnerAuthenticationProvider;
import com.arthur.auth.uaa.handler.OAuth2AuthenticationFailureHandler;
import com.arthur.auth.uaa.handler.OAuth2AuthenticationSuccessHandler;
import com.arthur.auth.uaa.support.AuthenticationExceptionTranslator;
import com.arthur.auth.uaa.support.AuthenticationExceptionTranslatorFactory.*;
import com.arthur.auth.uaa.support.DelegatingAuthenticationExceptionTranslator;
import com.arthur.boot.utils.BeanUtils;
import com.arthur.oauth2.constant.SecurityConstants;
import com.arthur.oauth2.resource.OAuth2UserDetailsService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.DelegatingOAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2RefreshTokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.web.authentication.DelegatingAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2AuthorizationCodeRequestAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.List;

/**
 * 认证服务器自动配置
 *
 * @author DearYang
 * @date 2022-08-24
 * @since 1.0
 */
@Configuration
public class OAuth2AuthorizationServerAutoConfiguration {

	private final OAuth2AuthorizationService authorizationService;
	private final MappingJackson2HttpMessageConverter httpMessageConverter;

	public OAuth2AuthorizationServerAutoConfiguration(OAuth2AuthorizationService authorizationService,
			MappingJackson2HttpMessageConverter httpMessageConverter) {
		this.authorizationService = authorizationService;
		this.httpMessageConverter = httpMessageConverter;
	}

	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public SecurityFilterChain authorizationSecurityFilterChain(HttpSecurity security) throws Exception {
		OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer();
		AuthenticationFailureHandler authenticationFailureHandler = new OAuth2AuthenticationFailureHandler(httpMessageConverter);
		List<AuthenticationConverter> authenticationConverters = this.obtainsAuthenticationConverters();
		security.apply(authorizationServerConfigurer.tokenEndpoint(tokenEndpoint -> {
			authenticationConverters.forEach(tokenEndpoint::accessTokenRequestConverter);
			tokenEndpoint.accessTokenResponseHandler(new OAuth2AuthenticationSuccessHandler())
				.errorResponseHandler(authenticationFailureHandler);
		}).clientAuthentication(configurer -> {
			//authenticationConverters.forEach(configurer::authenticationConverter);
			configurer.errorResponseHandler(authenticationFailureHandler);
		}).authorizationEndpoint(endpoint ->
			endpoint.consentPage(SecurityConstants.CONSENT_PAGE_URI)
		));

		RequestMatcher endpointsMatcher = authorizationServerConfigurer.getEndpointsMatcher();
		SecurityFilterChain securityFilterChain = security.securityMatcher(endpointsMatcher)
			.authorizeHttpRequests(requests -> requests.anyRequest().authenticated())
			.csrf(csrf -> csrf.ignoringRequestMatchers(endpointsMatcher))
			.apply(authorizationServerConfigurer.authorizationService(authorizationService)
				.authorizationServerSettings(AuthorizationServerSettings.builder().build()))
			.and()
			.apply(new FormIdentityLoginConfigurer()).and()
			.build();
		this.addOAuth2AuthenticationProvider(security);
		return securityFilterChain;
	}

	/**
	 * 令牌生成器
	 *
	 * @return {@link OAuth2TokenGenerator}
	 */
	@Bean
	public OAuth2TokenGenerator<OAuth2Token> arthurOAuth2TokenGenerator() {
		ArthurOAuth2TokenGenerator accessTokenGenerator = new ArthurOAuth2TokenGenerator();
		accessTokenGenerator.setAccessTokenCustomizer(new ArthurOAuth2TokenCustomizer());
		return new DelegatingOAuth2TokenGenerator(accessTokenGenerator, new OAuth2RefreshTokenGenerator());
	}

	/**
	 * {@link AbstractOAuth2ResourceOwnerAuthenticationProvider}异常处理器
	 *
	 * @param messageSource {@link MessageSource}
	 * @return {@link AuthenticationExceptionTranslator}
	 */
	@Bean
	public AuthenticationExceptionTranslator authenticationExceptionTranslator(
			MessageSourceAccessor messageSource) {
		List<AuthenticationExceptionTranslator> handlers = List.of(
			new UsernameNotFoundExceptionFailureTranslator(messageSource),
			new BadCredentialsExceptionFailureTranslator(messageSource),
			new LockedExceptionFailureTranslator(messageSource),
			new DisabledExceptionFailureTranslator(messageSource),
			new AccountExpiredExceptionFailureTranslator(messageSource),
			new CredentialsExpiredExceptionFailureTranslator(messageSource),
			new ScopeExceptionFailureTranslator(messageSource));
		DefaultAuthenticationExceptionTranslator defaultHandler = new DefaultAuthenticationExceptionTranslator();
		return new DelegatingAuthenticationExceptionTranslator(defaultHandler, handlers);
	}

	/**
	 * 自定义的授权认证转换器
	 *
	 * @return {@link DelegatingAuthenticationConverter}
	 */
	private List<AuthenticationConverter> obtainsAuthenticationConverters() {
		return List.of(
			//new OAuth2RefreshTokenAuthenticationConverter(),
			//new OAuth2ClientCredentialsAuthenticationConverter(),
			//new OAuth2AuthorizationCodeAuthenticationConverter(),
			new OAuth2PasswordResourceOwnerAuthenticationConverter(),
			new OAuth2AuthorizationCodeRequestAuthenticationConverter()
		);
	}

	/**
	 * 添加自定义实现的{@link AbstractOAuth2ResourceOwnerAuthenticationProvider}
	 *
	 * @param security   {@link HttpSecurity}
	 */
	private void addOAuth2AuthenticationProvider(HttpSecurity security) {
		ApplicationContext applicationContext = security.getSharedObject(ApplicationContext.class);
		AuthenticationManager authenticationManager = security.getSharedObject(AuthenticationManager.class);
		OAuth2AuthorizationService authorizationService = security.getSharedObject(OAuth2AuthorizationService.class);

		AuthenticationExceptionTranslator translator = applicationContext.getBean(AuthenticationExceptionTranslator.class);
		List<OAuth2UserDetailsService> userDetailsServices = BeanUtils.getBeanOfList(applicationContext, OAuth2UserDetailsService.class);

		// 处理密码模式
		OAuth2PasswordResourceOwnerAuthenticationProvider passwordAuthenticationProvider = new OAuth2PasswordResourceOwnerAuthenticationProvider(authenticationManager, authorizationService,
			this.arthurOAuth2TokenGenerator(), translator);
		// 处理用户名密码模式
		CompositeDaoAuthenticationProvider daoAuthenticationProvider = new CompositeDaoAuthenticationProvider(userDetailsServices);
		daoAuthenticationProvider.setPasswordEncoder(applicationContext.getBean(PasswordEncoder.class));

		security.authenticationProvider(daoAuthenticationProvider);
		security.authenticationProvider(passwordAuthenticationProvider);
	}

}
