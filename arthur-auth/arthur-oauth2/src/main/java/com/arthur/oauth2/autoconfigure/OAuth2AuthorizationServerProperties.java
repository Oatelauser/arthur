package com.arthur.oauth2.autoconfigure;

import com.arthur.oauth2.constant.OAuth2Constants;
import com.arthur.oauth2.server.RedisOAuth2AuthorizationConsentService;
import com.arthur.oauth2.server.RedisOAuth2AuthorizationService;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * 授权服务器配置类
 *
 * @author DearYang
 * @date 2022-08-18
 * @since 1.0
 */
@ConfigurationProperties(prefix = "arthur.oauth2.authorization-server")
public class OAuth2AuthorizationServerProperties {

    /**
     * 请求令牌有效期
     */
    private Duration tokenLiveTime = OAuth2Constants.TOKE_LIVE_TIME_SECONDS;

    /**
     * 令牌刷新有效时间
     */
    private Duration refreshTokenLiveTime = OAuth2Constants.REFRESH_TOKEN_LIVE_TIME_SECONDS;

    /**
     * @see RedisOAuth2AuthorizationService
     */
    private Duration authorizationTimeout = OAuth2Constants.DEFAULT_TIMEOUT;

    /**
     * @see RedisOAuth2AuthorizationConsentService
     */
    private Duration authorizationConsentTimeout = OAuth2Constants.DEFAULT_TIMEOUT;

    public Duration getTokenLiveTime() {
        return tokenLiveTime;
    }

    public void setTokenLiveTime(Duration tokenLiveTime) {
        this.tokenLiveTime = tokenLiveTime;
    }

    public Duration getRefreshTokenLiveTime() {
        return refreshTokenLiveTime;
    }

    public void setRefreshTokenLiveTime(Duration refreshTokenLiveTime) {
        this.refreshTokenLiveTime = refreshTokenLiveTime;
    }

    public Duration getAuthorizationTimeout() {
        return authorizationTimeout;
    }

    public void setAuthorizationTimeout(Duration authorizationTimeout) {
        this.authorizationTimeout = authorizationTimeout;
    }

    public Duration getAuthorizationConsentTimeout() {
        return authorizationConsentTimeout;
    }

    public void setAuthorizationConsentTimeout(Duration authorizationConsentTimeout) {
        this.authorizationConsentTimeout = authorizationConsentTimeout;
    }

}
