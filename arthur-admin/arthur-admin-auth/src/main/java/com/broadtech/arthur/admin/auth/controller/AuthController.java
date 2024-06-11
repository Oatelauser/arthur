package com.broadtech.arthur.admin.auth.controller;

import com.broadtech.arthur.admin.auth.respones.ResponseVo;
import com.broadtech.arthur.admin.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/9/16
 */
@RestController
@RequestMapping("/oauth")
public class AuthController {

    private AuthService authService;

    @Autowired
    public void setAuthService(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Oauth2登录认证
     */
    @PostMapping(value = "/token")
    public Object postAccessToken(Principal principal, @RequestParam Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {
        return ResponseVo.SUCCESS(authService.auth(principal, parameters));
    }
}
