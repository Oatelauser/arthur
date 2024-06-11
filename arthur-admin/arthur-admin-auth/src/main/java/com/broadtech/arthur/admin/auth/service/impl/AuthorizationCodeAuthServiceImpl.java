package com.broadtech.arthur.admin.auth.service.impl;

import com.broadtech.arthur.admin.auth.vo.Oauth2TokenVo;
import com.broadtech.arthur.admin.auth.service.AuthService;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import java.security.Principal;
import java.util.Map;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/9/20
 */
public class AuthorizationCodeAuthServiceImpl implements AuthService {
    @Override
    public Oauth2TokenVo auth(Principal principal, Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {
        return null;
    }
}
