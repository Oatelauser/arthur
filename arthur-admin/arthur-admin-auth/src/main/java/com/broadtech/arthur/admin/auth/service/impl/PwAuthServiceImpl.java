package com.broadtech.arthur.admin.auth.service.impl;

import com.broadtech.arthur.admin.auth.vo.Oauth2TokenVo;
import com.broadtech.arthur.admin.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.stereotype.Service;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import java.security.Principal;
import java.util.Map;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/9/20
 */
@Service

public class PwAuthServiceImpl implements AuthService {

    private  TokenEndpoint tokenEndpoint;

    @Autowired
    public void setTokenEndpoint(TokenEndpoint tokenEndpoint) {
        this.tokenEndpoint = tokenEndpoint;
    }

    @Override
    public Oauth2TokenVo auth(Principal principal, Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {
        OAuth2AccessToken oAuth2AccessToken = tokenEndpoint.postAccessToken(principal, parameters).getBody();
        return parseToken(oAuth2AccessToken);
    }
    private Oauth2TokenVo parseToken(OAuth2AccessToken oAuth2AccessToken){
        return Oauth2TokenVo.builder()
                .token(oAuth2AccessToken.getValue())
                .refreshToken(oAuth2AccessToken.getRefreshToken().getValue())
                .expiresIn(oAuth2AccessToken.getExpiresIn())
                .tokenHead("Bearer ").build();
    }
}
