package com.arthur.oauth2.exception;

import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;

import java.io.Serial;

/**
 * OAuthClientException
 *
 * @author DearYang
 * @date 2022-08-14
 * @since 1.0
 */
@SuppressWarnings("unused")
public class OAuthClientException extends OAuth2AuthenticationException {

    @Serial
	private static final long serialVersionUID = 1311483025878442194L;

    /**
     * Constructs a <code>OAuthClientException</code> with the specified message.
     *
     * @param msg the detail message.
     */
    public OAuthClientException(String msg) {
        super(new OAuth2Error(msg), msg);
    }

    /**
     * Constructs a {@code OAuthClientException} with the specified message and root cause.
     *
     * @param msg       the detail message.
     * @param throwable root cause
     */
    public OAuthClientException(String msg, Throwable throwable) {
        super(new OAuth2Error(msg), throwable);
    }


}
