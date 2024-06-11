package com.arthur.auth.uaa.handler;

import com.arthur.common.response.ServerResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

/**
 * 认证失败处理器
 *
 * @author DearYang
 * @date 2022-08-24
 * @since 1.0
 */
public class OAuth2AuthenticationFailureHandler implements AuthenticationFailureHandler {

    private static final Logger LOG = LoggerFactory.getLogger(OAuth2AuthenticationFailureHandler.class);

    private final MappingJackson2HttpMessageConverter httpMessageConverter;

    public OAuth2AuthenticationFailureHandler(MappingJackson2HttpMessageConverter httpMessageConverter) {
        this.httpMessageConverter = httpMessageConverter;
    }

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException {
		LOG.error("user [" + request.getParameter(OAuth2ParameterNames.USERNAME) + "] login fail",
			NestedExceptionUtils.getRootCause(exception));

		ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
		httpResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
		String code = String.valueOf(HttpStatus.UNAUTHORIZED.value());
		String message = exception.getLocalizedMessage();

		if (exception instanceof OAuth2AuthenticationException authenticationException) {
			message = authenticationException.getError().getDescription();
		}

		httpMessageConverter.write(ServerResponse.ofError(code, message), MediaType.APPLICATION_JSON, httpResponse);
	}

}
