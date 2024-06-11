package com.arthur.oauth2.constant;

import com.arthur.oauth2.utils.SecurityUtils;

/**
 * Spring Security相关的常量
 *
 * @author DearYang
 * @date 2022-08-14
 * @since 1.0
 */
public interface SecurityConstants {

    /**
     * 手机号登录
     */
    String APP = "app";

    /**
     * 角色前缀
     *
     * @see SecurityUtils
     */
    String ROLE = "ROLE_";

    /**
     * 客户端ID
     */
    String CLIENT_ID = "clientId";

    /**
     * 客户端模式
     */
    String CLIENT_CREDENTIALS = "client_credentials";

    /**
     * 认证用户
     */
    String PRINCIPAL_USER = "principal_user";

    /**
     * 授权码模式confirm
     */
    String CONSENT_PAGE_URI = "/token/confirmAccess";

	/**
	 * 授权服务仓库的Bean名称
	 */
	String AUTHORIZATION_SERVER_REPOSITORY = "arthur.oauth2.authorization-service.repository-type";

	String MESSAGE_SOURCE_BEAN_NAME = "authenticationMessageSource";

}
