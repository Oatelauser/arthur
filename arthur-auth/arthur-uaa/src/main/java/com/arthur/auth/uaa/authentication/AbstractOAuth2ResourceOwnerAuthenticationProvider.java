package com.arthur.auth.uaa.authentication;

import com.arthur.auth.uaa.support.AuthenticationExceptionTranslator;
import com.arthur.oauth2.exception.ScopeException;
import com.arthur.oauth2.utils.OAuth2Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization.Token;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientCredentialsAuthenticationProvider;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.util.CollectionUtils;

import java.security.Principal;
import java.util.*;

import static com.arthur.oauth2.constant.OAuth2Constants.OAUTH2_SERVER_ERROR_URI;
import static com.arthur.oauth2.constant.OAuth2ErrorCodesExpand.SCOPES_IS_EMPTY;
import static org.springframework.security.oauth2.core.OAuth2ErrorCodes.*;

/**
 * 自定义OAuth2授权模式-6.授权认证调用主逻辑
 *
 * @author DearYang
 * @date 2022-08-18
 * @see AuthenticationProvider
 * @since 1.0
 */
public abstract class AbstractOAuth2ResourceOwnerAuthenticationProvider
		<T extends AbstractOAuth2ResourceOwnerAuthenticationToken> implements AuthenticationProvider {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractOAuth2ResourceOwnerAuthenticationProvider.class);

    private final AuthenticationManager authenticationManager;
    private final OAuth2AuthorizationService authorizationService;
    private final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;
    private final AuthenticationExceptionTranslator authenticationExceptionTranslator;

    public AbstractOAuth2ResourceOwnerAuthenticationProvider(AuthenticationManager authenticationManager,
			OAuth2AuthorizationService authorizationService,
			OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator,
			AuthenticationExceptionTranslator translator) {
        this.authenticationManager = authenticationManager;
        this.authorizationService = Objects.requireNonNull(authorizationService);
        this.tokenGenerator = Objects.requireNonNull(tokenGenerator);
        this.authenticationExceptionTranslator = translator;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        T tokenAuthentication = (T) authentication;
        OAuth2ClientAuthenticationToken clientPrincipal = getOAuth2ClientAuthenticationToken(tokenAuthentication);
        // 验证注册客户端
        RegisteredClient registeredClient = Objects.requireNonNull(clientPrincipal.getRegisteredClient());
        if (!this.supports(registeredClient)) {
            throw new OAuth2AuthenticationException(UNAUTHORIZED_CLIENT);
        }

        // 获取scopes
        Set<String> authorizeScopes = this.getAuthorizeScopes(tokenAuthentication, clientPrincipal.getRegisteredClient());

        try {
            return this.doAuthenticate(authorizeScopes, registeredClient, clientPrincipal, tokenAuthentication);
        } catch (Exception e) {
			AuthenticationException authenticationException = null;
			if (e instanceof AuthenticationException ex) {
				authenticationException = this.authenticationExceptionTranslator
					.translateExceptionIfPossible(authentication, ex);
			}
            if (authenticationException == null) {
                authenticationException = new OAuth2AuthenticationException(new OAuth2Error(SERVER_ERROR),
					e.getLocalizedMessage(), e);
			}
            throw authenticationException;
        }
    }

	/**
	 * 处理OAuth2核心认证逻辑
	 * <p>
	 * copy by {@link OAuth2ClientCredentialsAuthenticationProvider#authenticate(Authentication)}
	 *
	 * @param authorizeScopes     scope
	 * @param registeredClient    {@link RegisteredClient}
	 * @param clientPrincipal     {@link OAuth2ClientAuthenticationToken}
	 * @param tokenAuthentication {@link T}
	 * @return {@link Authentication}
	 */
    private Authentication doAuthenticate(Set<String> authorizeScopes, RegisteredClient registeredClient, OAuth2ClientAuthenticationToken clientPrincipal, T tokenAuthentication) {
        Authentication usernamePasswordAuthentication = this.doAuthenticate(tokenAuthentication);
        DefaultOAuth2TokenContext.Builder tokenContextBuilder = DefaultOAuth2TokenContext.builder()
                .registeredClient(registeredClient)
                .principal(usernamePasswordAuthentication)
                .authorizationServerContext(AuthorizationServerContextHolder.getContext())
                .authorizedScopes(authorizeScopes)
                .authorizationGrantType(AuthorizationGrantType.PASSWORD)
                .authorizationGrant(tokenAuthentication);

        // Access Token
        OAuth2TokenContext tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.ACCESS_TOKEN).build();
        OAuth2Token generatedAccessToken = this.tokenGenerator.generate(tokenContext);
        if (generatedAccessToken == null) {
            throw OAuth2Utils.createException(SERVER_ERROR, "The token generator failed to generate the access token.", OAUTH2_SERVER_ERROR_URI);
        }
		OAuth2AccessToken accessToken = new OAuth2AccessToken(TokenType.BEARER,
			generatedAccessToken.getTokenValue(), generatedAccessToken.getIssuedAt(),
			generatedAccessToken.getExpiresAt(), tokenContext.getAuthorizedScopes());

        // 授权
		OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization.withRegisteredClient(registeredClient)
			.principalName(clientPrincipal.getName())
			.authorizationGrantType(AuthorizationGrantType.PASSWORD)
			.authorizedScopes(authorizeScopes)
			.id(accessToken.getTokenValue());
        if (generatedAccessToken instanceof ClaimAccessor claimAccessor) {
			authorizationBuilder.token(accessToken, metadata -> metadata.put(Token.CLAIMS_METADATA_NAME, claimAccessor.getClaims()))
				.authorizedScopes(authorizeScopes)
				.attribute(Principal.class.getName(), usernamePasswordAuthentication);
        } else {
            authorizationBuilder.accessToken(accessToken);
        }

        // Refresh Token
        OAuth2RefreshToken refreshToken = this.getOAuth2RefreshToken(registeredClient, clientPrincipal, tokenContextBuilder);
        Optional.ofNullable(refreshToken).ifPresent(authorizationBuilder::refreshToken);
        OAuth2Authorization authorization = authorizationBuilder.build();
        // 令牌存储
        this.authorizationService.save(authorization);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Ready to return OAuth2AccessTokenAuthenticationToken");
        }

        Map<String, Object> claimsSet = Objects.requireNonNull(authorization.getAccessToken().getClaims());
        return new OAuth2AccessTokenAuthenticationToken(registeredClient, clientPrincipal, accessToken, refreshToken, claimsSet);
    }

    /**
     * 获取客户端凭证
     *
     * @param authentication {@link Authentication}
     * @return {@link OAuth2ClientAuthenticationToken}
     */
    private OAuth2ClientAuthenticationToken getOAuth2ClientAuthenticationToken(Authentication authentication) {
        OAuth2ClientAuthenticationToken clientPrincipal = null;
        if (authentication.getPrincipal() instanceof OAuth2ClientAuthenticationToken authenticationToken) {
            clientPrincipal = authenticationToken;
        }
        // 验证凭证
        if (clientPrincipal == null || !clientPrincipal.isAuthenticated()) {
            throw new OAuth2AuthenticationException(INVALID_CLIENT);
        }
        return clientPrincipal;
    }

    public Set<String> getAuthorizeScopes(T tokenAuthentication, RegisteredClient client) {
        Set<String> authorizeScopes = tokenAuthentication.getScopes();
        if (CollectionUtils.isEmpty(authorizeScopes)) {
            throw new ScopeException(SCOPES_IS_EMPTY);
        }

        Set<String> clientScopes = client.getScopes();
        for (String authorizeScope : authorizeScopes) {
            if (!clientScopes.contains(authorizeScope)) {
                throw new OAuth2AuthenticationException(INVALID_SCOPE);
            }
        }

        return new LinkedHashSet<>(authorizeScopes);
    }

    /**
     * 认证管理器执行认证
     *
     * @param tokenAuthentication 认证对象
     */
    private Authentication doAuthenticate(T tokenAuthentication) {
        Map<String, Object> additionalParameters = tokenAuthentication.getAdditionalParameters();
        UsernamePasswordAuthenticationToken usernamePasswordAuthentication = this.buildAuthenticationToken(additionalParameters, tokenAuthentication);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Create UsernamePasswordAuthenticationToken object [{}]", usernamePasswordAuthentication);
        }

        // 交给Spring Security进行认证
        return authenticationManager.authenticate(usernamePasswordAuthentication);
    }

    /**
     * 创建刷新令牌
     *
     * @param client              {@link RegisteredClient}
     * @param clientPrincipal     {@link OAuth2ClientAuthenticationToken}
     * @param tokenContextBuilder {@link DefaultOAuth2TokenContext.Builder}
     * @return {@link OAuth2RefreshToken}
     */
    private OAuth2RefreshToken getOAuth2RefreshToken(RegisteredClient client, OAuth2ClientAuthenticationToken clientPrincipal,
			 DefaultOAuth2TokenContext.Builder tokenContextBuilder) {
        if (!client.getAuthorizationGrantTypes().contains(AuthorizationGrantType.REFRESH_TOKEN)) {
            return null;
        }
        // 不要向公共客户端发出刷新令牌
        if (clientPrincipal.getClientAuthenticationMethod().equals(ClientAuthenticationMethod.NONE)) {
            return null;
        }
        OAuth2TokenContext tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.REFRESH_TOKEN).build();
        OAuth2Token refreshToken = this.tokenGenerator.generate(tokenContext);
        if (!(refreshToken instanceof OAuth2RefreshToken)) {
            throw OAuth2Utils.createException(SERVER_ERROR, "The token generator failed to generate refresh token", OAUTH2_SERVER_ERROR_URI);
        }

        return (OAuth2RefreshToken) refreshToken;
    }

    /**
     * 授权模式是否支持该请求客户端
     *
     * @param client {@link RegisteredClient}
     * @return yes or no
     */
    protected abstract boolean supports(RegisteredClient client);

    /**
     * 构建认证对象
     *
     * @param parameters 请求参数
     * @return {@link UsernamePasswordAuthenticationToken}
     */
	protected abstract UsernamePasswordAuthenticationToken buildAuthenticationToken(Map<String, Object> parameters, T tokenAuthentication);

}
