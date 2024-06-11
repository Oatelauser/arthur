package com.arthur.oauth2.server;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.util.Assert;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.*;

/**
 * Redis实现的{@link OAuth2AuthorizationService}
 *
 * @author DearYang
 * @date 2022-08-31
 * @since 1.0
 */
public class RedisOAuth2AuthorizationService implements OAuth2AuthorizationService {

	private final Duration timeout;
	private final RedisKeyGenerator<String[]> keyGenerator;
	private final RedisTemplate<String, Object> redisTemplate;

	public RedisOAuth2AuthorizationService(Duration timeout, RedisKeyGenerator<String[]> keyGenerator,
			RedisTemplate<String, Object> redisTemplate) {
		this.timeout = timeout;
		this.keyGenerator = keyGenerator;
		this.redisTemplate = redisTemplate;
	}

	@Override
	public void save(OAuth2Authorization authorization) {
		Assert.notNull(authorization, "authorization can`t be null");
		// 1.state
		this.getState(authorization).ifPresent(key -> redisTemplate.opsForValue().set(key, authorization, timeout));
		// 2.codeToken
		this.getCode(authorization).ifPresent(token -> saveToken(CODE, token, authorization));
		// 3.RefreshToken
		Optional.ofNullable(authorization.getRefreshToken()).ifPresent(token -> saveToken(REFRESH_TOKEN, token, authorization));
		// 4. AccessToken
		Optional.ofNullable(authorization.getAccessToken()).ifPresent(token -> saveToken(ACCESS_TOKEN, token, authorization));
	}

	@Override
	public void remove(OAuth2Authorization authorization) {
		Assert.notNull(authorization, "authorization can`t be null");
		List<String> keys = new ArrayList<>(4);
		// 1.state
		this.getState(authorization).ifPresent(keys::add);
		// 2.codeToken
		this.getCode(authorization).ifPresent(token -> keys.add(getTokenKey(CODE, token)));
		// 3.RefreshToken
		Optional.ofNullable(authorization.getRefreshToken()).ifPresent(token -> keys.add(getTokenKey(REFRESH_TOKEN, token)));
		// 4. AccessToken
		Optional.ofNullable(authorization.getAccessToken()).ifPresent(token -> keys.add(getTokenKey(ACCESS_TOKEN, token)));
		if (!keys.isEmpty()) {
			redisTemplate.delete(keys);
		}
	}

	@Override
	public OAuth2Authorization findById(String id) {
		throw new UnsupportedOperationException("Unsupported findById method");
	}

	@Override
	public OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType) {
		Assert.hasText(token, "token can`t be empty");
		Assert.notNull(tokenType, "tokenType can`t be null");
		return (OAuth2Authorization) redisTemplate.opsForValue().get(keyGenerator.generate(new String[]{ tokenType.getValue(), token }));
	}

	private Optional<String> getState(OAuth2Authorization authorization) {
		String state = authorization.getAttribute(STATE);
		if (state != null) {
			state = keyGenerator.generate(new String[]{ STATE, state });
		}
		return Optional.ofNullable(state);
	}

	private Optional<OAuth2Authorization.Token<OAuth2AuthorizationCode>> getCode(OAuth2Authorization authorization) {
		OAuth2Authorization.Token<OAuth2AuthorizationCode> codeToken = authorization.getToken(OAuth2AuthorizationCode.class);
		return Optional.ofNullable(codeToken);
	}

	@SuppressWarnings("rawtypes")
	private void saveToken(String type, OAuth2Authorization.Token auth2Token, OAuth2Authorization authorization) {
		OAuth2Token token = auth2Token.getToken();
		Duration timeout = Duration.between(Objects.requireNonNull(token.getIssuedAt()), token.getExpiresAt());
		String key = keyGenerator.generate(new String[]{ type, token.getTokenValue() });
		redisTemplate.opsForValue().set(key, authorization, timeout);
	}

	@SuppressWarnings("rawtypes")
	private String getTokenKey(String type, OAuth2Authorization.Token auth2Token) {
		OAuth2Token token = auth2Token.getToken();
		return keyGenerator.generate(new String[]{ type, token.getTokenValue() });
	}

}
