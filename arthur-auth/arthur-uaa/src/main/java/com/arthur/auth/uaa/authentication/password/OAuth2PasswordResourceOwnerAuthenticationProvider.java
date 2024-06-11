package com.arthur.auth.uaa.authentication.password;

import com.arthur.auth.uaa.authentication.AbstractOAuth2ResourceOwnerAuthenticationProvider;
import com.arthur.auth.uaa.support.AuthenticationExceptionTranslator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;

import java.util.Map;

/**
 * 密码授权模式
 *
 * @author DearYang
 * @date 2022-08-21
 * @since 1.0
 */
public class OAuth2PasswordResourceOwnerAuthenticationProvider extends AbstractOAuth2ResourceOwnerAuthenticationProvider<OAuth2PasswordResourceOwnerAuthenticationToken> {

    private static final Logger LOG = LoggerFactory.getLogger(OAuth2PasswordResourceOwnerAuthenticationProvider.class);

    public OAuth2PasswordResourceOwnerAuthenticationProvider(AuthenticationManager authenticationManager, OAuth2AuthorizationService authorizationService, OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator, AuthenticationExceptionTranslator translator) {
        super(authenticationManager, authorizationService, tokenGenerator, translator);
    }

    @Override
    public boolean supports(RegisteredClient client) {
        if (client == null) {
            return false;
        }

        return client.getAuthorizationGrantTypes().contains(AuthorizationGrantType.PASSWORD);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        if (OAuth2PasswordResourceOwnerAuthenticationToken.class.isAssignableFrom(authentication)) {
            return true;
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Authentication class [{}] must be OAuth2PasswordGrantAuthenticationToken type", authentication);
        }
        return false;
    }

    @Override
    public UsernamePasswordAuthenticationToken buildAuthenticationToken(Map<String, Object> parameters,
			OAuth2PasswordResourceOwnerAuthenticationToken tokenAuthentication) {
        String username = (String) parameters.get(OAuth2ParameterNames.USERNAME);
        String password = (String) parameters.get(OAuth2ParameterNames.PASSWORD);
		Authentication clientPrincipal = tokenAuthentication.getClientPrincipal();
		return new OAuth2UsernamePasswordAuthenticationToken((String) clientPrincipal.getPrincipal(),
			tokenAuthentication.getGrantType().getValue(), username, password);
    }

}
