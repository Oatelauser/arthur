package com.arthur.auth.uaa.handler;

import com.arthur.oauth2.utils.WebUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static com.arthur.auth.uaa.constant.AuthConstants.FORM_LOGIN_FAILURE_REDIRECT_URL;

/**
 * 表单登录认证失败处理器
 *
 * @author DearYang
 * @date 2022-08-24
 * @since 1.0
 */
public class FormLoginAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private static final Logger LOG = LoggerFactory.getLogger(FormLoginAuthenticationFailureHandler.class);

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("form login fail: {}", exception.getLocalizedMessage());
        }

        Optional<HttpServletResponse> optional = WebUtils.getResponse();
        if (optional.isEmpty()) {
            return;
        }

		// /token/login?error=
		String redirect = FORM_LOGIN_FAILURE_REDIRECT_URL + exception.getMessage();
        optional.get().sendRedirect(UriUtils.encodeQueryParam(redirect, StandardCharsets.UTF_8));
    }

}
