package com.arthur.oauth2.authentication;

import com.arthur.common.response.ServerResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 认证异常处理
 *
 * @author DearYang
 * @date 2022-09-05
 * @since 1.0
 */
public class ResourceExceptionAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private final ObjectMapper objectMapper;
	private final MessageSourceAccessor messageSourceAccessor;

	public ResourceExceptionAuthenticationEntryPoint(ObjectMapper objectMapper,
			MessageSourceAccessor messageSourceAccessor) {
		this.objectMapper = objectMapper;
		this.messageSourceAccessor = messageSourceAccessor;
	}

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException {
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);

		ServerResponse serverResponse = ServerResponse.ofError();
		if (authException instanceof InvalidBearerTokenException
				|| authException instanceof InsufficientAuthenticationException) {
			int status = HttpStatus.FAILED_DEPENDENCY.value();
			response.setStatus(status);
			serverResponse.setCode(String.valueOf(status));
			String message = this.messageSourceAccessor.getMessage("OAuth2ResourceOwnerBaseAuthenticationProvider.tokenExpired", "token expire");
			serverResponse.setMsg(message);
		} else if (authException != null) {
			int status = HttpStatus.UNAUTHORIZED.value();
			response.setStatus(status);
			serverResponse.setCode(String.valueOf(status));
			serverResponse.setMsg(authException.getMessage());
		}

		response.getWriter().append(objectMapper.writeValueAsString(serverResponse));
	}

}
