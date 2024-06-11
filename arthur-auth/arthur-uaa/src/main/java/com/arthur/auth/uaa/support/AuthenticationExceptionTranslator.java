package com.arthur.auth.uaa.support;

import com.arthur.auth.uaa.authentication.AbstractOAuth2ResourceOwnerAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * 认证异常翻译器
 *
 * @author DearYang
 * @date 2022-08-19
 * @see AbstractOAuth2ResourceOwnerAuthenticationProvider
 * @since 1.0
 */
public interface AuthenticationExceptionTranslator {

    /**
     * 异常处理
     * <p>
     * 统一抛出{@link org.springframework.security.oauth2.core.OAuth2AuthenticationException}异常
     *
     * @param exception 认证异常
     */
    AuthenticationException translateExceptionIfPossible(Authentication authentication, AuthenticationException exception);

}
