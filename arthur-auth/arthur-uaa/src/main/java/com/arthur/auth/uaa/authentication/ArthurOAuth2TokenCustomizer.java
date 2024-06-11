package com.arthur.auth.uaa.authentication;

import com.arthur.oauth2.constant.SecurityConstants;
import com.arthur.oauth2.resource.OAuth2User;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimsContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimsSet;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

/**
 * 自定义增强Token
 *
 * @author DearYang
 * @date 2022-08-23
 * @since 1.0
 */
public class ArthurOAuth2TokenCustomizer implements OAuth2TokenCustomizer<OAuth2TokenClaimsContext> {

    @Override
    public void customize(OAuth2TokenClaimsContext context) {
        OAuth2TokenClaimsSet.Builder claims = context.getClaims();
        claims.claim(SecurityConstants.CLIENT_ID, context.getAuthorizationGrant().getName());
        if (SecurityConstants.CLIENT_CREDENTIALS.equals(context.getAuthorizationGrantType().getValue())) {
            return;
        }

        OAuth2User principal = (OAuth2User) context.getPrincipal().getPrincipal();
        claims.claim(SecurityConstants.PRINCIPAL_USER, principal);
    }

}
