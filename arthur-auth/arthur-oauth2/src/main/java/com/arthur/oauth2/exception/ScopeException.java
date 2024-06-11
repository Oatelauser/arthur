package com.arthur.oauth2.exception;

import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;

import java.io.Serial;

/**
 * 异常信息
 *
 * @author DearYang
 * @date 2022-08-14
 * @since 1.0
 */
@SuppressWarnings("unused")
public class ScopeException extends OAuth2AuthenticationException {

    @Serial
	private static final long serialVersionUID = 1311483025878442194L;

    /**
     * Constructs a <code>ScopeException</code> with the specified message.
     *
     * @param message the detail message.
     */
    public ScopeException(String message) {
        super(new OAuth2Error(message), message);
    }

    /**
     * Constructs a {@code ScopeException} with the specified message and root cause.
     *
     * @param message   the detail message.
     * @param throwable root cause
     */
    public ScopeException(String message, Throwable throwable) {
        super(new OAuth2Error(message), throwable);
    }

}
