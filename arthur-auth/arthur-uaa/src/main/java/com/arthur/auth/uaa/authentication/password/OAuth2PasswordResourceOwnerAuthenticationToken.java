package com.arthur.auth.uaa.authentication.password;

import com.arthur.auth.uaa.authentication.AbstractOAuth2ResourceOwnerAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import java.io.Serial;
import java.util.Map;
import java.util.Set;

/**
 * 密码授权模式
 *
 * @author DearYang
 * @date 2022-08-21
 * @since 1.0
 */
public class OAuth2PasswordResourceOwnerAuthenticationToken extends AbstractOAuth2ResourceOwnerAuthenticationToken {

    @Serial
	private static final long serialVersionUID = -2871823452236840523L;

    public OAuth2PasswordResourceOwnerAuthenticationToken(Set<String> scopes, AuthorizationGrantType grantType,
			Authentication clientPrincipal, Map<String, Object> additionalParameters) {
        super(scopes, grantType, clientPrincipal, additionalParameters);
    }

}
