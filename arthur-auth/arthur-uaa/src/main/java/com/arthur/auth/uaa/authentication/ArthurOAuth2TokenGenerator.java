package com.arthur.auth.uaa.authentication;

import com.arthur.common.utils.NanoIdUtils;
import com.arthur.oauth2.utils.SecurityUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.ClaimAccessor;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.token.*;

import java.io.Serial;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.arthur.auth.uaa.constant.AuthConstants.RANDOM_SIZE;
import static com.arthur.auth.uaa.constant.AuthConstants.TOKEN_VALUE_DELIMITER;
import static org.springframework.security.oauth2.server.authorization.OAuth2TokenType.ACCESS_TOKEN;
import static org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat.REFERENCE;

/**
 * OAuth2令牌生成器
 *
 * @author DearYang
 * @date 2022-08-19
 * @since 1.0
 */
@SuppressWarnings("unused")
public class ArthurOAuth2TokenGenerator implements OAuth2TokenGenerator<OAuth2AccessToken> {

	private OAuth2TokenCustomizer<OAuth2TokenClaimsContext> accessTokenCustomizer;

	@Override
	public OAuth2AccessToken generate(OAuth2TokenContext context) {
		if (!ACCESS_TOKEN.equals(context.getTokenType())) {
			return null;
		}
		RegisteredClient client = context.getRegisteredClient();
		if (!REFERENCE.equals(client.getTokenSettings().getAccessTokenFormat())) {
			return null;
		}

		OAuth2TokenClaimsSet claimsSet = this.getOAuth2TokenClaimsSet(client, context);
		return new OAuth2AccessTokenClaims(TokenType.BEARER, claimsSet, context);
	}

	/**
	 * 构建{@link OAuth2TokenClaimsSet}
	 *
	 * @param client  {@link RegisteredClient}
	 * @param context {@link OAuth2TokenContext}
	 * @return {@link OAuth2TokenClaimsSet}
	 */
	private OAuth2TokenClaimsSet getOAuth2TokenClaimsSet(RegisteredClient client, OAuth2TokenContext context) {
		OAuth2TokenClaimsSet.Builder claimsBuilder = OAuth2TokenClaimsSet.builder();
		// 设置issuer
		if (context.getAuthorizationServerContext() != null) {
			claimsBuilder.issuer(context.getAuthorizationServerContext().getIssuer());
		}
		Instant now = Instant.now();
		claimsBuilder.id(NanoIdUtils.threadRandomNanoId(RANDOM_SIZE))
			.issuedAt(now)
			.notBefore(now)
			.expiresAt(now.plus(client.getTokenSettings().getAccessTokenTimeToLive()))
			.audience(List.of(client.getClientId()))
			.subject(context.getPrincipal().getName());

		this.apply(claimsBuilder, context);
		return claimsBuilder.build();
	}

	/**
	 * 自定义处理{@link OAuth2TokenClaimsContext}
	 *
	 * @param claimsBuilder {@link OAuth2TokenClaimsSet.Builder}
	 * @param context       {@link OAuth2TokenContext}
	 */
	private void apply(OAuth2TokenClaimsSet.Builder claimsBuilder, OAuth2TokenContext context) {
		if (accessTokenCustomizer == null) {
			return;
		}

		OAuth2TokenClaimsContext.Builder claimsContextBuilder = OAuth2TokenClaimsContext.with(claimsBuilder)
			.principal(context.getPrincipal())
			.tokenType(context.getTokenType())
			.authorizationServerContext(context.getAuthorizationServerContext())
			.registeredClient(context.getRegisteredClient())
			.authorizedScopes(context.getAuthorizedScopes())
			.authorizationGrantType(context.getAuthorizationGrantType());
		Optional.ofNullable(context.getAuthorization()).ifPresent(claimsContextBuilder::authorization);
		Optional.ofNullable((Authentication) context.getAuthorizationGrant()).ifPresent(claimsContextBuilder::authorizationGrant);
		OAuth2TokenClaimsContext accessTokenContext = claimsContextBuilder.build();
		accessTokenCustomizer.customize(accessTokenContext);
	}

	public OAuth2TokenCustomizer<OAuth2TokenClaimsContext> getAccessTokenCustomizer() {
		return accessTokenCustomizer;
	}

	public void setAccessTokenCustomizer(OAuth2TokenCustomizer<OAuth2TokenClaimsContext> accessTokenCustomizer) {
		this.accessTokenCustomizer = accessTokenCustomizer;
	}

	/**
	 * 组装令牌key
	 * <p>
	 * client::username::NanoId
	 *
	 * @param context {@link OAuth2TokenContext}
	 * @return 令牌Key
	 */
	public static String getTokenValue(OAuth2TokenContext context) {
		return SecurityUtils.getAuthentication().getPrincipal() + TOKEN_VALUE_DELIMITER
			+ context.getPrincipal().getName() + TOKEN_VALUE_DELIMITER
			+ NanoIdUtils.threadRandomNanoId(RANDOM_SIZE);
	}

	/**
	 * 自定义实现{@link OAuth2AccessToken}整合{@link ClaimAccessor}
	 */
	private static final class OAuth2AccessTokenClaims extends OAuth2AccessToken implements ClaimAccessor {

		@Serial
		private static final long serialVersionUID = 3061391443261715000L;

		private final Map<String, Object> claims;

		public OAuth2AccessTokenClaims(TokenType tokenType, OAuth2TokenClaimsSet claimsSet, OAuth2TokenContext context) {
			super(tokenType, ArthurOAuth2TokenGenerator.getTokenValue(context), claimsSet.getIssuedAt(), claimsSet.getExpiresAt(), context.getAuthorizedScopes());
			this.claims = claimsSet.getClaims();
		}

		@Override
		public Map<String, Object> getClaims() {
			return claims;
		}
	}

}
