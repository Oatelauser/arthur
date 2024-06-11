package com.arthur.auth.uaa.constant;

import com.arthur.auth.uaa.authentication.ArthurOAuth2TokenGenerator;
import com.arthur.common.utils.NanoIdUtils;

/**
 * 认证授权中心常量
 *
 * @author DearYang
 * @date 2022-08-19
 * @since 1.0
 */
public interface AuthConstants {

    /**
     * 随机数的位数
     *
     * @see NanoIdUtils
     */
    int RANDOM_SIZE = 32;

    /**
     * 令牌key的连接符
     *
     * @see ArthurOAuth2TokenGenerator
     */
    String TOKEN_VALUE_DELIMITER = "::";

    /**
     * 重定向URL请求参数名
     */
    String REDIRECT_PARAMETER = "redirect_url";

    /**
     * 登录页面URL
     */
    String FORM_LOGIN_URL = "/token/login";

    /**
     * 验证凭证URL
     */
    String CREDENTIALS_VALIDATION_URL = "/token/form";

    /**
     * 表单登录异常重定向地址URL
     */
    String FORM_LOGIN_FAILURE_REDIRECT_URL = FORM_LOGIN_URL + "?error=";

}
