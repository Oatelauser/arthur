package com.broadtech.arthur.admin.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;

import javax.sql.DataSource;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/9/15
 */
@Configuration
public class AuthCodeServerConfig {

    /**
     * 授权代码服务
     * 设置授权码模式的授权码如何存取
     *
     * @param dataSource 数据源
     * @return {@link AuthorizationCodeServices}
     */
    @Bean("codeService")
    public AuthorizationCodeServices codeServices(DataSource dataSource) {
        return new JdbcAuthorizationCodeServices(dataSource);
    }

}
