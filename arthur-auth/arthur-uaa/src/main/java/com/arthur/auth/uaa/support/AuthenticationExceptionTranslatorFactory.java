package com.arthur.auth.uaa.support;

import com.arthur.oauth2.exception.ScopeException;
import com.arthur.oauth2.utils.OAuth2Utils;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static com.arthur.oauth2.constant.OAuth2ErrorCodesExpand.*;
import static org.springframework.security.oauth2.core.OAuth2ErrorCodes.INVALID_SCOPE;

/**
 * {@link AuthenticationExceptionTranslator}工具类
 * <p>
 * 提供各种内置的认证异常处理类
 *
 * @author DearYang
 * @date 2022-08-20
 * @since 1.0
 */
public class AuthenticationExceptionTranslatorFactory {

    public static class DefaultAuthenticationExceptionTranslator implements AuthenticationExceptionTranslator {

        @Override
        public AuthenticationException translateExceptionIfPossible(Authentication authentication, AuthenticationException exception) {
			return OAuth2Utils.createException(UNKNOWN_LOGIN_ERROR, "未知登录异常", "", exception);
        }

    }

    /**
     * 处理{@link UsernameNotFoundException}异常
     *
     * @author DearYang
     * @date 2022-08-20
     * @since 1.0
     */
    public static class UsernameNotFoundExceptionFailureTranslator extends AbstractAuthenticationExceptionTranslator<UsernameNotFoundException> {

        public UsernameNotFoundExceptionFailureTranslator(MessageSourceAccessor messageSource) {
            super(messageSource);
        }

        @Override
        public AuthenticationException translateException(Authentication authentication, UsernameNotFoundException exception) {
            String message = this.messages.getMessage("JdbcDaoImpl.notFound", new Object[]{authentication.getName()}, "Username {0} not found");
            return OAuth2Utils.createException(USERNAME_NOT_FOUND, message, "", exception);
        }
    }

    /**
     * 处理{@link BadCredentialsException}异常
     *
     * @author DearYang
     * @date 2022-08-20
     * @since 1.0
     */
    public static class BadCredentialsExceptionFailureTranslator extends AbstractAuthenticationExceptionTranslator<BadCredentialsException> {

        public BadCredentialsExceptionFailureTranslator(MessageSourceAccessor messageSource) {
            super(messageSource);
        }

        @Override
        public AuthenticationException translateException(Authentication authentication, BadCredentialsException exception) {
            String message = this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials");
            return OAuth2Utils.createException(BAD_CREDENTIALS, message , "", exception);
        }
    }

    /**
     * 处理{@link LockedException}异常
     *
     * @author DearYang
     * @date 2022-08-20
     * @since 1.0
     */
    public static class LockedExceptionFailureTranslator extends AbstractAuthenticationExceptionTranslator<LockedException> {

        public LockedExceptionFailureTranslator(MessageSourceAccessor messageSource) {
            super(messageSource);
        }

        @Override
        public AuthenticationException translateException(Authentication authentication, LockedException exception) {
            String message = this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.locked", "User account is locked");
            return OAuth2Utils.createException(USER_LOCKED, message , "", exception);
        }
    }

    /**
     * 处理{@link DisabledException}异常
     *
     * @author DearYang
     * @date 2022-08-20
     * @since 1.0
     */
    public static class DisabledExceptionFailureTranslator extends AbstractAuthenticationExceptionTranslator<DisabledException> {

        public DisabledExceptionFailureTranslator(MessageSourceAccessor messageSource) {
            super(messageSource);
        }

        @Override
        public AuthenticationException translateException(Authentication authentication, DisabledException exception) {
            String message = this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.disabled", "User account is disabled");
            return OAuth2Utils.createException(USER_DISABLED, message , "", exception);
        }
    }

    /**
     * 处理{@link AccountExpiredException}异常
     *
     * @author DearYang
     * @date 2022-08-20
     * @since 1.0
     */
    public static class AccountExpiredExceptionFailureTranslator extends AbstractAuthenticationExceptionTranslator<AccountExpiredException> {

        public AccountExpiredExceptionFailureTranslator(MessageSourceAccessor messageSource) {
            super(messageSource);
        }

        @Override
        public AuthenticationException translateException(Authentication authentication, AccountExpiredException exception) {
            String message = this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.expired", "User account has expired");
            return OAuth2Utils.createException(USER_EXPIRED, message , "", exception);
        }
    }

    /**
     * 处理{@link CredentialsExpiredException}异常
     *
     * @author DearYang
     * @date 2022-08-20
     * @since 1.0
     */
    public static class CredentialsExpiredExceptionFailureTranslator extends AbstractAuthenticationExceptionTranslator<CredentialsExpiredException> {

        public CredentialsExpiredExceptionFailureTranslator(MessageSourceAccessor messageSource) {
            super(messageSource);
        }

        @Override
        public AuthenticationException translateException(Authentication authentication, CredentialsExpiredException exception) {
            String message = this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.credentialsExpired", "User credentials have expired");
            return OAuth2Utils.createException(CREDENTIALS_EXPIRED, message , "", exception);
        }
    }

    /**
     * 处理{@link ScopeException}异常
     *
     * @author DearYang
     * @date 2022-08-20
     * @since 1.0
     */
    public static class ScopeExceptionFailureTranslator extends AbstractAuthenticationExceptionTranslator<ScopeException> {

        public ScopeExceptionFailureTranslator(MessageSourceAccessor messageSource) {
            super(messageSource);
        }

        @Override
        public AuthenticationException translateException(Authentication authentication, ScopeException exception) {
            String message = this.messages.getMessage("AbstractAccessDecisionManager.accessDenied", "invalid_scope");
            return OAuth2Utils.createException(INVALID_SCOPE, message , "", exception);
        }
    }

}
