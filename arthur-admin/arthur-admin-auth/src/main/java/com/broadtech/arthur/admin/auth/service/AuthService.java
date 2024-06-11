package com.broadtech.arthur.admin.auth.service;

import com.broadtech.arthur.admin.auth.vo.Oauth2TokenVo;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import java.security.Principal;
import java.util.Map;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/9/20
 */
public interface AuthService {


    /**
     * 身份验证
     *
     * @param principal  主要
     * @param parameters 参数
     * @return {@link Oauth2TokenVo}
     * @throws HttpRequestMethodNotSupportedException http请求方法不支持异常
     */
    Oauth2TokenVo auth(Principal principal, Map<String, String> parameters) throws HttpRequestMethodNotSupportedException;

}
