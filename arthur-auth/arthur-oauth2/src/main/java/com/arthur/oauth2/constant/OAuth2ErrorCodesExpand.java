package com.arthur.oauth2.constant;

/**
 * OAuth2异常码拓展
 *
 * @author DearYang
 * @date 2022-08-14
 * @see org.springframework.security.oauth2.core.OAuth2ErrorCodes
 * @since 1.0
 */
public interface OAuth2ErrorCodesExpand {

    /**
     * 用户名未找到
     */
    String USERNAME_NOT_FOUND = "username_not_found";

    /**
     * 错误的凭证
     */
    String BAD_CREDENTIALS = "bad_credentials";

    /**
     * 用户被锁
     */
    String USER_LOCKED = "user_locked";

    /**
     * 用户被禁用
     */
    String USER_DISABLED = "user_disabled";

    /**
     * 用户过期
     */
    String USER_EXPIRED = "user_expired";

    /**
     * 用户凭证过期
     */
    String CREDENTIALS_EXPIRED = "credentials_expired";

    /**
     * Scope为空
     */
    String SCOPES_IS_EMPTY = "scopes_is_empty";

    /**
     * 未知的登录异常
     */
    String UNKNOWN_LOGIN_ERROR = "unknown_login_error";

}
