package com.broadtech.arthur.admin.auth.config;

import com.broadtech.arthur.admin.auth.bo.SecurityUserBo;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/9/16
 */
@Component
public class JwtTokenEnhancer implements TokenEnhancer {
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {

        Object principal = authentication.getPrincipal();
        if (principal instanceof SecurityUserBo){
            SecurityUserBo securityUserBo = (SecurityUserBo) principal;
            Map<String, Object> info = new HashMap<>(8);
            //把用户ID设置到JWT中
            info.put("id", securityUserBo.getId());
            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);
        }
        return accessToken;
    }
}
