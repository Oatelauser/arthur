package com.arthur.auth.uaa.authentication.password;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serial;
import java.util.Collection;

/**
 * 拓展{@link UsernamePasswordAuthenticationToken}
 *
 * <ul>新增字段
 * 	<li>{@link #clientId}：客户端ID
 * 	<li>{@link #grantType}：OAuth2类型
 * </ul>
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-01-29
 * @since 1.0
 */
public class OAuth2UsernamePasswordAuthenticationToken extends UsernamePasswordAuthenticationToken {

	@Serial
	private static final long serialVersionUID = -2245683704727716115L;

	private final String clientId;
	private final String grantType;

	public OAuth2UsernamePasswordAuthenticationToken(String clientId, String grantType,
			Object principal, Object credentials) {
		super(principal, credentials);
		this.clientId = clientId;
		this.grantType = grantType;
	}

	public OAuth2UsernamePasswordAuthenticationToken(String clientId, String grantType,
			Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
		super(principal, credentials, authorities);
		this.clientId = clientId;
		this.grantType = grantType;
	}

	public String getGrantType() {
		return grantType;
	}

	public String getClientId() {
		return clientId;
	}

}
