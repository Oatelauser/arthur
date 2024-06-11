package com.arthur.oauth2.autoconfigure;

import com.arthur.auth.user.api.RemoteClientDetailService;
import com.arthur.oauth2.constant.OAuth2Constants;
import com.arthur.oauth2.server.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.security.oauth2.server.authorization.*;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

import java.util.Collections;
import java.util.List;

import static com.arthur.oauth2.constant.SecurityConstants.AUTHORIZATION_SERVER_REPOSITORY;

/**
 * OAuth2授权服务器自动配置类
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-01-22
 * @since 1.0
 */
@AutoConfiguration(before = OAuth2ResourceServerAutoConfiguration.class)
@EnableConfigurationProperties(OAuth2AuthorizationServerProperties.class)
public class OAuth2AuthorizationServerAutoConfiguration {

	@Configuration(proxyBeanMethods = false)
	@ConditionalOnProperty(value = AUTHORIZATION_SERVER_REPOSITORY, havingValue = "memory")
	static class InMemoryOAuth2AuthorizationServerConfiguration {

		@Bean
		@ConditionalOnMissingBean
		OAuth2AuthorizationService inMemoryOAuth2AuthorizationService() {
			return new InMemoryOAuth2AuthorizationService();
		}

		@Bean
		@ConditionalOnMissingBean
		OAuth2AuthorizationConsentService inMemoryOAuth2AuthorizationConsentService() {
			return new InMemoryOAuth2AuthorizationConsentService();
		}

		@Bean
		@ConditionalOnMissingBean
		RegisteredClientRepository inMemoryRegisteredClientRepository(ObjectProvider<List<RegisteredClient>> provider) {
			return new InMemoryRegisteredClientRepository(provider.getIfAvailable(Collections::emptyList));
		}

	}

	@Configuration(proxyBeanMethods = false)
	@ConditionalOnClass(JdbcOperations.class)
	@ConditionalOnProperty(value = AUTHORIZATION_SERVER_REPOSITORY, havingValue = "jdbc")
	static class JdbcOAuth2AuthorizationServerConfiguration {

		@Bean
		@ConditionalOnMissingBean
		OAuth2AuthorizationService jdbcOAuth2AuthorizationService(JdbcOperations jdbcOperations,
			RegisteredClientRepository registeredClientRepository) {
			return new JdbcOAuth2AuthorizationService(jdbcOperations, registeredClientRepository);
		}

		@Bean
		@ConditionalOnMissingBean
		OAuth2AuthorizationConsentService jdbcOAuth2AuthorizationConsentService(JdbcOperations jdbcOperations,
			RegisteredClientRepository registeredClientRepository) {
			return new JdbcOAuth2AuthorizationConsentService(jdbcOperations, registeredClientRepository);
		}

		@Bean
		@ConditionalOnMissingBean
		RegisteredClientRepository jdbcRegisteredClientRepository(JdbcOperations jdbcOperations) {
			return new JdbcRegisteredClientRepository(jdbcOperations);
		}

	}

	@Configuration(proxyBeanMethods = false)
	@Import(RedisKeyGeneratorConfiguration.class)
	@ConditionalOnClass(name = "org.springframework.data.redis.core.RedisTemplate")
	@ConditionalOnProperty(value = AUTHORIZATION_SERVER_REPOSITORY, havingValue = "redis", matchIfMissing = true)
	static class RedisOAuth2AuthorizationServerConfiguration {

		@Bean
		@ConditionalOnMissingBean
		RedisTemplate<String, Object> oAuth2RedisTemplate(ObjectMapper objectMapper,
				RedisConnectionFactory redisConnectionFactory) {
			RedisTemplate<String, Object> template = new RedisTemplate<>();
			StringRedisSerializer serializer = new StringRedisSerializer();
			template.setConnectionFactory(redisConnectionFactory);
			template.setKeySerializer(serializer);
			template.setHashKeySerializer(serializer);
			Jackson2JsonRedisSerializer<Object> valueSerializer = new Jackson2JsonRedisSerializer<>(objectMapper, Object.class);
			template.setValueSerializer(valueSerializer);
			template.setHashValueSerializer(valueSerializer);
			return template;
		}

		@Bean
		@ConditionalOnMissingBean
		OAuth2AuthorizationService redisOAuth2AuthorizationService(OAuth2AuthorizationServerProperties properties,
			RedisKeyGenerator<String[]> authorizationKeyGenerator,
			RedisTemplate<String, Object> redisTemplate) {
			return new RedisOAuth2AuthorizationService(properties.getAuthorizationTimeout(),
				authorizationKeyGenerator, redisTemplate);
		}

		@Bean
		@ConditionalOnMissingBean
		OAuth2AuthorizationConsentService redisOAuth2AuthorizationConsentService(OAuth2AuthorizationServerProperties properties,
			RedisKeyGenerator<OAuth2AuthorizationConsent> authorizationConsentKeyGenerator,
			RedisTemplate<String, Object> redisTemplate) {
			return new RedisOAuth2AuthorizationConsentService(properties.getAuthorizationConsentTimeout(),
				authorizationConsentKeyGenerator, redisTemplate);
		}

		@Bean
		@SuppressWarnings("all")
		@ConditionalOnMissingBean
		RegisteredClientRepository remoteRegisteredClientRepository(OAuth2AuthorizationServerProperties properties,
				RemoteClientDetailService clientDetailService) {
			return new RemoteRegisteredClientRepository(properties, clientDetailService);
		}

	}

	static class RedisKeyGeneratorConfiguration {

		@Bean
		@ConditionalOnMissingBean(name = "authorizationKeyGenerator")
		RedisKeyGenerator<String[]> authorizationKeyGenerator() {
			return new AuthorizationKeyGenerator();
		}

		@Bean
		@ConditionalOnMissingBean(name = "authorizationConsentKeyGenerator")
		RedisKeyGenerator<OAuth2AuthorizationConsent> authorizationConsentKeyGenerator() {
			return consent -> OAuth2Constants.TOKEN_KEY_PREFIX + ":consent:" + consent.getRegisteredClientId() + consent.getPrincipalName();
		}

	}

}
