package com.arthur.auth.uaa.authentication.password;

import com.arthur.auth.uaa.authentication.AbstractOAuth2ResourceOwnerAuthenticationConverter;
import com.arthur.oauth2.utils.OAuth2Utils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.arthur.oauth2.constant.OAuth2Constants.OAUTH2_INVALID_REQUEST_ERROR_URI;
import static org.springframework.security.oauth2.core.OAuth2ErrorCodes.INVALID_REQUEST;

/**
 * 密码授权模式的转换器
 *
 * @author DearYang
 * @date 2022-08-21
 * @since 1.0
 */
public class OAuth2PasswordResourceOwnerAuthenticationConverter extends AbstractOAuth2ResourceOwnerAuthenticationConverter<OAuth2PasswordResourceOwnerAuthenticationToken> {

    @Override
    public boolean supports(String grantType) {
        return AuthorizationGrantType.PASSWORD.getValue().equals(grantType);
    }

    @Override
    public void additionalRequestChecks(MultiValueMap<String, String> parameters, HttpServletRequest request) {
        // 校验用户名
        List<String> usernames = parameters.get(OAuth2ParameterNames.USERNAME);
        if (CollectionUtils.isEmpty(usernames) || usernames.size() != 1) {
            throw OAuth2Utils.createException(INVALID_REQUEST, OAuth2ParameterNames.USERNAME, OAUTH2_INVALID_REQUEST_ERROR_URI);
        }

        // 校验密码
        List<String> passwords = parameters.get(OAuth2ParameterNames.PASSWORD);
        if (CollectionUtils.isEmpty(passwords) || passwords.size() != 1) {
            throw OAuth2Utils.createException(INVALID_REQUEST, OAuth2ParameterNames.PASSWORD, OAUTH2_INVALID_REQUEST_ERROR_URI);
        }
    }

    @Override
    public OAuth2PasswordResourceOwnerAuthenticationToken buildTokenAuthentication(Authentication clientPrincipal,
			Set<String> requestScopes, Map<String, Object> additionalParameters) {
        return new OAuth2PasswordResourceOwnerAuthenticationToken(requestScopes,
			AuthorizationGrantType.PASSWORD, clientPrincipal, additionalParameters);
    }

}
