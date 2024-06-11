package com.arthur.auth.uaa.authentication;

import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import java.io.Serial;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 自定义OAuth2授权模式-5.登录认证对象
 *
 * @author DearYang
 * @date 2022-08-18
 * @since 1.0
 */
@SuppressWarnings("unused")
public abstract class AbstractOAuth2ResourceOwnerAuthenticationToken extends AbstractAuthenticationToken {

	@Serial
	private static final long serialVersionUID = -3931652216372348710L;

	private final Set<String> scopes;
	private final AuthorizationGrantType grantType;
	private final Authentication clientPrincipal;
	private final Map<String, Object> additionalParameters;

	public AbstractOAuth2ResourceOwnerAuthenticationToken(@Nullable Set<String> scopes,
			AuthorizationGrantType grantType,
			Authentication clientPrincipal,
			@Nullable Map<String, Object> additionalParameters) {
		super(Collections.emptyList());
		this.grantType = Objects.requireNonNull(grantType);
		this.clientPrincipal = Objects.requireNonNull(clientPrincipal);
		this.scopes = scopes == null ? Collections.emptySet() : Collections.unmodifiableSet(scopes);
		this.additionalParameters = additionalParameters == null ? Collections.emptyMap() :
			Collections.unmodifiableMap(additionalParameters);
	}

	@Override
	public Object getCredentials() {
		return "";
	}

	/**
	 * 获取用户名
	 *
	 * @return 用户名
	 */
	@Override
	public Object getPrincipal() {
		return clientPrincipal;
	}

	public Set<String> getScopes() {
		return scopes;
	}

	public AuthorizationGrantType getGrantType() {
		return grantType;
	}

	public Authentication getClientPrincipal() {
		return clientPrincipal;
	}

	public Map<String, Object> getAdditionalParameters() {
		return additionalParameters;
	}

}
