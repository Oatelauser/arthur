package com.arthur.oauth2.autoconfigure;

import com.arthur.auth.user.api.RemoteUserDetailService;
import com.arthur.boot.core.url.UrlPathMather;
import com.arthur.oauth2.authentication.OAuth2AnnotationAnalysisProcessor;
import com.arthur.oauth2.authentication.ResourceExceptionAuthenticationEntryPoint;
import com.arthur.oauth2.cache.OAuth2CacheManager;
import com.arthur.oauth2.resource.ArthurBearerTokenResolver;
import com.arthur.oauth2.resource.OAuth2OpaqueTokenIntrospector;
import com.arthur.oauth2.resource.OAuth2UserDetailsService;
import com.arthur.oauth2.resource.RemoteUserDetailsService;
import com.arthur.oauth2.server.PermissionAuthorizer;
import com.arthur.oauth2.utils.SpringUrlPathMather;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.List;

/**
 * OAuth2资源服务器自动配置类
 *
 * @author DearYang
 * @date 2022-08-14
 * @since 1.0
 */
@EnableConfigurationProperties(OAuth2ResourceServerProperties.class)
@AutoConfiguration(after = { OAuth2CacheAutoConfiguration.class })
public class OAuth2ResourceServerAutoConfiguration {

	@Configuration(proxyBeanMethods = false)
	static class OAuth2UrlPathMatherConfiguration {

		@Bean
		@ConditionalOnMissingBean
		@ConditionalOnClass(name = "org.springframework.web.reactive.function.client.WebClient")
		PathPatternParser pathPatternParser() {
			return new PathPatternParser();
		}

		@Bean
		@ConditionalOnMissingBean
		UrlPathMather springUrlPathMather(PathPatternParser parser) {
			return new SpringUrlPathMather(parser);
		}

	}

	@Configuration(proxyBeanMethods = false)
	static class OAuth2UserDetailsServiceConfiguration {

		@Bean
		@SuppressWarnings("all")
        OAuth2UserDetailsService clientUserDetailsService(OAuth2CacheManager cacheManager,
				RemoteUserDetailService userDetailService) {
			return new RemoteUserDetailsService(cacheManager, userDetailService);
		}

	}

	@Configuration(proxyBeanMethods = false)
	static class OAuth2ResourceServerConfiguration {

		@Bean
		@ConditionalOnMissingBean
		public PermissionAuthorizer permissionAuthorizer() {
			return new PermissionAuthorizer();
		}

		@Bean
		@ConditionalOnMissingBean
		public AuthenticationEntryPoint jsonAuthenticationEntryPoint(ObjectMapper objectMapper,
				MessageSourceAccessor messageSourceAccessor) {
			return new ResourceExceptionAuthenticationEntryPoint(objectMapper, messageSourceAccessor);
		}

		@Bean
		@ConditionalOnMissingBean
		public BearerTokenResolver arthurBearerTokenResolver(OAuth2ResourceServerProperties properties,
				UrlPathMather mather) {
			return new ArthurBearerTokenResolver(properties, mather);
		}

		@Bean
		@ConditionalOnMissingBean
		public OpaqueTokenIntrospector opaqueTokenIntrospector(OAuth2AuthorizationService authorizationService,
				List<OAuth2UserDetailsService> userDetailsServices) {
			return new OAuth2OpaqueTokenIntrospector(authorizationService, userDetailsServices);
		}

		@Bean
		@ConditionalOnMissingBean
		public OAuth2AnnotationAnalysisProcessor authenticationAnnotationScanner(OAuth2ResourceServerProperties properties,
				@Qualifier(value = "requestMappingHandlerMapping") RequestMappingHandlerMapping handlerMapping) {
			return new OAuth2AnnotationAnalysisProcessor(properties, handlerMapping);
		}

	}

	@Configuration(proxyBeanMethods = false)
	static class OAuth2ResourceServerSecurityConfiguration {

		private final OAuth2ResourceServerProperties properties;
		private final AuthenticationEntryPoint entryPoint;
		private final OpaqueTokenIntrospector introspector;
		private final BearerTokenResolver bearerTokenResolver;

		OAuth2ResourceServerSecurityConfiguration(OAuth2ResourceServerProperties properties,
				AuthenticationEntryPoint entryPoint, OpaqueTokenIntrospector introspector,
				BearerTokenResolver bearerTokenResolver) {
			this.properties = properties;
			this.entryPoint = entryPoint;
			this.introspector = introspector;
			this.bearerTokenResolver = bearerTokenResolver;
		}

		@Bean
		@Order(Ordered.HIGHEST_PRECEDENCE)
		SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {
			return security.authorizeHttpRequests(request ->
					request.requestMatchers(properties.getExcludeUrls().toArray(new String[0]))
						.permitAll()
						.anyRequest()
						.authenticated())
				.oauth2ResourceServer(oauth2 ->
					oauth2.opaqueToken(token -> token.introspector(introspector))
						.authenticationEntryPoint(entryPoint)
						.bearerTokenResolver(bearerTokenResolver))
				.headers().frameOptions().disable().and()
				.csrf().disable()
				.build();
		}

	}

}
