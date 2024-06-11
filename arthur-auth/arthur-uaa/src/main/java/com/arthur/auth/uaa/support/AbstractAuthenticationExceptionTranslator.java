package com.arthur.auth.uaa.support;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public abstract class AbstractAuthenticationExceptionTranslator<T extends AuthenticationException> implements AuthenticationExceptionTranslator {

    /**
     * 国际化
     */
    protected final MessageSourceAccessor messages;

    public AbstractAuthenticationExceptionTranslator(MessageSourceAccessor messageSource) {
        this.messages = messageSource;
    }

    @Override
    @SuppressWarnings("unchecked")
    public AuthenticationException translateExceptionIfPossible(Authentication authentication, AuthenticationException exception) {
        return translateException(authentication, (T) exception);
    }

    /**
     * 处理认证具体的异常
     *
     * @param authentication {@link Authentication}
     * @param exception      {@link T}
     */
    public abstract AuthenticationException translateException(Authentication authentication, T exception);

}
