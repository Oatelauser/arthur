package com.arthur.auth.uaa.handler;

import com.arthur.oauth2.constant.SecurityConstants;
import com.arthur.oauth2.resource.OAuth2User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;

/**
 * 认证成功处理器
 *
 * @author DearYang
 * @date 2022-08-24
 * @since 1.0
 */
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger LOG = LoggerFactory.getLogger(OAuth2AuthenticationFailureHandler.class);

    private final HttpMessageConverter<OAuth2AccessTokenResponse> accessTokenHttpResponseConverter =
		new OAuth2AccessTokenResponseHttpMessageConverter();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException {
        OAuth2AccessTokenAuthenticationToken accessTokenAuthentication = (OAuth2AccessTokenAuthenticationToken) authentication;
        Map<String, Object> additionalParameters = accessTokenAuthentication.getAdditionalParameters();
        if (!CollectionUtils.isEmpty(additionalParameters)) {
			OAuth2User user = (OAuth2User) additionalParameters.get(SecurityConstants.PRINCIPAL_USER);
			if (LOG.isDebugEnabled()) {
				LOG.debug("user login success: {}", user);
			}
		}

        //SecurityContextHolder.getContext().setAuthentication(accessTokenAuthentication);
		this.sendAccessTokenResponse(request, response, accessTokenAuthentication, additionalParameters);
    }

	/**
	 * copy by {@code OAuth2TokenEndpointFilter#sendAccessTokenResponse(HttpServletRequest, HttpServletResponse, Authentication)}
	 */
    @SuppressWarnings("unused")
    private void sendAccessTokenResponse(HttpServletRequest request, HttpServletResponse response,
			OAuth2AccessTokenAuthenticationToken accessTokenAuthentication,
			Map<String, Object> additionalParameters) throws IOException {
        OAuth2AccessToken accessToken = accessTokenAuthentication.getAccessToken();
        OAuth2RefreshToken refreshToken = accessTokenAuthentication.getRefreshToken();
        OAuth2AccessTokenResponse.Builder builder = OAuth2AccessTokenResponse.withToken(accessToken.getTokenValue())
                .tokenType(accessToken.getTokenType()).scopes(accessToken.getScopes());

        // 设置过期时间
        Instant issuedAt = accessToken.getIssuedAt();
        Instant expiresAt = accessToken.getExpiresAt();
        if (issuedAt != null && expiresAt != null) {
            builder.expiresIn(ChronoUnit.SECONDS.between(issuedAt, expiresAt));
        }

		if (refreshToken != null) {
			builder.refreshToken(refreshToken.getTokenValue());
		}
		if (!CollectionUtils.isEmpty(additionalParameters)) {
			builder.additionalParameters(additionalParameters);
		}

        // 构建响应
        OAuth2AccessTokenResponse accessTokenResponse = builder.build();
        ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
        // 无状态清空上下文信息
        //SecurityContextHolder.clearContext();
        this.accessTokenHttpResponseConverter.write(accessTokenResponse, null, httpResponse);
    }

}
