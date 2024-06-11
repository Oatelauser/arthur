package com.arthur.oauth2.server;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.util.Assert;

import java.time.Duration;

/**
 * Redis实现的{@link OAuth2AuthorizationConsentService}
 *
 * @author DearYang
 * @date 2022-08-31
 * @since 1.0
 */
public class RedisOAuth2AuthorizationConsentService implements OAuth2AuthorizationConsentService {

	private final Duration timeout;
	private final RedisTemplate<String, Object> redisTemplate;
	private final RedisKeyGenerator<OAuth2AuthorizationConsent> keyGenerator;

	public RedisOAuth2AuthorizationConsentService(Duration timeout,
			RedisKeyGenerator<OAuth2AuthorizationConsent> keyGenerator,
			RedisTemplate<String, Object> redisTemplate) {
		this.timeout = timeout;
		this.keyGenerator = keyGenerator;
		this.redisTemplate = redisTemplate;
	}

	@Override
	public void save(OAuth2AuthorizationConsent authorizationConsent) {
		Assert.notNull(authorizationConsent, "authorizationConsent can`t be null");
		redisTemplate.opsForValue().set(keyGenerator.generate(authorizationConsent),
			authorizationConsent, timeout);
	}

	@Override
	public void remove(OAuth2AuthorizationConsent authorizationConsent) {
		Assert.notNull(authorizationConsent, "authorizationConsent can`t be null");
		redisTemplate.delete(keyGenerator.generate(authorizationConsent));
	}

	@Override
	public OAuth2AuthorizationConsent findById(String registeredClientId, String principalName) {
		Assert.hasText(principalName, "principalName can`t be empty");
		Assert.hasText(registeredClientId, "registeredClientId can`t be empty");
		String key = keyGenerator.generate(OAuth2AuthorizationConsent.withId(registeredClientId, principalName).build());
		return (OAuth2AuthorizationConsent) redisTemplate.opsForValue().get(key);
	}

}
