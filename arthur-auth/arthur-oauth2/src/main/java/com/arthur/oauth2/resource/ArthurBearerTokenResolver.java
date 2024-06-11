package com.arthur.oauth2.resource;

import com.arthur.boot.core.url.UrlPathMather;
import com.arthur.oauth2.autoconfigure.OAuth2ResourceServerProperties;
import com.arthur.oauth2.autoconfigure.OAuth2ResourceServerProperties.OAuth2TokenProperties;
import com.arthur.oauth2.constant.OAuth2Constants;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.server.resource.BearerTokenError;
import org.springframework.security.oauth2.server.resource.BearerTokenErrors;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.regex.Matcher;

/**
 * 令牌解析器
 *
 * @author DearYang
 * @date 2022-08-14
 * @see org.springframework.security.oauth2.server.resource.web.BearerTokenResolver
 * @see org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver
 * @since 1.0
 */
@SuppressWarnings("unused")
public class ArthurBearerTokenResolver implements BearerTokenResolver {

    private final OAuth2ResourceServerProperties resourceServerProperties;
    private final UrlPathMather urlPathMather;

    public ArthurBearerTokenResolver(OAuth2ResourceServerProperties resourceServerProperties,
            UrlPathMather urlPathMather) {
        this.urlPathMather = urlPathMather;
        this.resourceServerProperties = resourceServerProperties;
    }

    @Override
    public String resolve(HttpServletRequest request) {
        if (urlPathMather.anyMatch(resourceServerProperties.getExcludeUrls(), request.getRequestURI())) {
            return null;
        }

        OAuth2TokenProperties tokenProperties = resourceServerProperties.getToken();
        String headerToken = this.resolveFromAuthorizationHeader(tokenProperties, request);
        if (headerToken != null) {
            return headerToken;
        }

        return isParameterTokenSupportedForRequest(request) &&
                isParameterTokenEnabledForRequest(tokenProperties, request) ?
                resolveFromRequestParameters(tokenProperties, request) : null;
    }

    private String resolveFromAuthorizationHeader(OAuth2TokenProperties tokenProperties, HttpServletRequest request) {
        String authorization = request.getHeader(tokenProperties.getHeaderName());
        if (!StringUtils.startsWithIgnoreCase(authorization, OAuth2Constants.AUTHORIZATION_TOKEN_PREFIX)) {
            return null;
        }

        Matcher matcher = OAuth2Constants.AUTHORIZATION_PATTERN.matcher(authorization);
        if (!matcher.matches()) {
            BearerTokenError invalidTokenError = BearerTokenErrors.invalidToken("Bearer token is invalid");
            throw new OAuth2AuthenticationException(invalidTokenError);
        }

        return matcher.group(OAuth2Constants.AUTHORIZATION_PATTERN_GROUP);
    }

    private String resolveFromRequestParameters(OAuth2TokenProperties tokenProperties, HttpServletRequest request) {
        String[] tokens = request.getParameterValues(tokenProperties.getParameterName());
        if (ObjectUtils.isEmpty(tokens)) {
            return null;
        }

        if (tokens.length == 1) {
            return tokens[0];
        }
        BearerTokenError invalidRequestError = BearerTokenErrors.invalidRequest(
                "Found multiple bearer tokens[" + tokens.length + "] in the request");
        throw new OAuth2AuthenticationException(invalidRequestError);
    }

    protected boolean isParameterTokenSupportedForRequest(final HttpServletRequest request) {
        return ((HttpMethod.POST.name().equals(request.getMethod())
                && MediaType.APPLICATION_FORM_URLENCODED_VALUE.equals(request.getContentType()))
                || HttpMethod.GET.name().equals(request.getMethod()));
    }

    protected boolean isParameterTokenEnabledForRequest(OAuth2TokenProperties tokenProperties, HttpServletRequest request) {
        boolean allowFormEncodedBodyParameter = tokenProperties.getAllowFormEncodedBodyParameter();
        boolean allowUriQueryParameter = tokenProperties.getAllowUriQueryParameter();
        return ((allowFormEncodedBodyParameter && HttpMethod.POST.name().equals(request.getMethod())
                && MediaType.APPLICATION_FORM_URLENCODED_VALUE.equals(request.getContentType()))
                || (allowUriQueryParameter && HttpMethod.GET.name().equals(request.getMethod())));
    }

}
