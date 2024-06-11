package com.arthur.auth.uaa.autoconfigure;

import com.arthur.auth.uaa.constant.AuthConstants;
import com.arthur.auth.uaa.handler.FormLoginAuthenticationFailureHandler;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

/**
 * 基于授权模式统一认证表单登录
 *
 * @author DearYang
 * @date 2022-08-24
 * @see OAuth2AuthorizationServerAutoConfiguration
 * @since 1.0
 */
public class FormIdentityLoginConfigurer extends AbstractHttpConfigurer<FormIdentityLoginConfigurer, HttpSecurity> {

    @Override
    public void init(HttpSecurity security) throws Exception {
        // 登录处理
        security.formLogin()
                .loginPage(AuthConstants.FORM_LOGIN_URL)
                .loginProcessingUrl(AuthConstants.CREDENTIALS_VALIDATION_URL)
                .failureHandler(new FormLoginAuthenticationFailureHandler());

        // 登出成功处理
        SimpleUrlLogoutSuccessHandler successHandler = new SimpleUrlLogoutSuccessHandler();
        successHandler.setUseReferer(true);
        successHandler.setTargetUrlParameter(AuthConstants.REDIRECT_PARAMETER);

        security.logout()
                .logoutSuccessHandler(successHandler)
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .and()
                .csrf().disable();
    }

}
