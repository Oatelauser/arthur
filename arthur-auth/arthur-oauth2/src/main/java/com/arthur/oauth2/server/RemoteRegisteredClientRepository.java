package com.arthur.oauth2.server;

import com.arthur.auth.user.api.RemoteClientDetailService;
import com.arthur.auth.user.domain.entity.OAuth2ClientDetail;
import com.arthur.common.utils.DurationUnit;
import com.arthur.oauth2.autoconfigure.OAuth2AuthorizationServerProperties;
import com.arthur.oauth2.exception.HttpStatusException;
import com.arthur.oauth2.exception.OAuthClientException;
import com.arthur.web.model.GenericServerResponse;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.Optional;
import java.util.stream.Stream;

import static com.arthur.common.utils.StringUtils.split;
import static com.arthur.security.constant.SecurityConstants.NOOP_PASSWORD_PREFIX;
import static org.springframework.security.oauth2.core.ClientAuthenticationMethod.CLIENT_SECRET_BASIC;
import static org.springframework.security.oauth2.core.ClientAuthenticationMethod.CLIENT_SECRET_POST;

/**
 * OAuth2注册客户端存储库
 * <p>
 * 构建{@link RegisteredClient}对象
 *
 * @author DearYang
 * @date 2022-08-17
 * @since 1.0
 */
@SuppressWarnings("unused")
public class RemoteRegisteredClientRepository implements RegisteredClientRepository {

    private final OAuth2AuthorizationServerProperties properties;
    private final RemoteClientDetailService clientDetailService;

    public RemoteRegisteredClientRepository(OAuth2AuthorizationServerProperties properties,
            RemoteClientDetailService clientDetailService) {
        this.properties = properties;
        this.clientDetailService = clientDetailService;
    }

    /**
     * 通过“,”切分字符串，过滤出有效字符串
     *
     * @param str 字符串
     * @return {@link Stream}
     */
    public static Stream<String> trimSpilt(String str) {
        return split(str, ",").stream().filter(StringUtils::hasText);
    }

    @Override
    public void save(RegisteredClient registeredClient) {
        throw new UnsupportedOperationException();
    }

    @Override
    public RegisteredClient findById(String id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
		// TODO:开发环境测试数据，生产环境删除
		OAuth2ClientDetail clientDetail = new OAuth2ClientDetail();
		clientDetail.setClientId(clientId);
		clientDetail.setClientSecret("{noop}12345");
		clientDetail.setScope("server");
		clientDetail.setGrantTypes("password,app,refresh_token");
		clientDetail.setAutoApprove(true);
        //OAuth2ClientDetail clientDetail = this.getClientDetail(clientId);
        RegisteredClient.Builder clientBuilder = this.getRegisteredClientBuilder(clientDetail);
        return clientBuilder.tokenSettings(TokenSettings.builder().accessTokenFormat(OAuth2TokenFormat.REFERENCE)
                        .accessTokenTimeToLive(getTokenLiveTime(clientDetail.getTokenLiveTime(), properties.getTokenLiveTime()))
                        .refreshTokenTimeToLive(getTokenLiveTime(clientDetail.getRefreshTokenLiveTime(), properties.getRefreshTokenLiveTime())).build())
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(clientDetail.getAutoApprove()).build())
                .build();
    }

    /**
     * 获取客户端详情信息
     *
     * @param clientId 客户端ID
     * @return {@link OAuth2ClientDetail}
     */
    private OAuth2ClientDetail getClientDetail(String clientId) {
        GenericServerResponse<OAuth2ClientDetail> response = clientDetailService
                .getClientDetailById(clientId);
        Assert.notNull(response, "client detail response is null");
        if (!response.isSuccess()) {
            throw new HttpStatusException(response.getCode());
        }

        return response.orElseThrow(() -> new OAuthClientException("clientId is invalid"));
    }

    /**
     * 构建RegisteredClient Builder对象
     * <p>
     * 设置builder中对应得参数信息
     *
     * @param clientDetail {@link OAuth2ClientDetail}
     * @return {@link RegisteredClient.Builder}
     */
    private RegisteredClient.Builder getRegisteredClientBuilder(OAuth2ClientDetail clientDetail) {
        // clientId、clientSecret、clientAuthenticationMethod
		RegisteredClient.Builder clientBuilder = RegisteredClient.withId(clientDetail.getClientId())
			.clientId(clientDetail.getClientId())
			.clientSecret(NOOP_PASSWORD_PREFIX + clientDetail.getClientSecret())
			.clientAuthenticationMethod(CLIENT_SECRET_POST)
			.clientAuthenticationMethods(methods -> {
				methods.add(CLIENT_SECRET_POST);
				methods.add(CLIENT_SECRET_BASIC);
			});

        // Scope
        Optional.ofNullable(clientDetail.getScope()).ifPresent(scope ->
                trimSpilt(scope).forEach(clientBuilder::scope));
        // 授权方式
        Optional.ofNullable(clientDetail.getGrantTypes()).ifPresent(grants ->
                trimSpilt(grants).map(AuthorizationGrantType::new).forEach(clientBuilder::authorizationGrantType));
        // 回调地址
        Optional.ofNullable(clientDetail.getRedirectUri()).ifPresent(urls ->
                trimSpilt(urls).forEach(clientBuilder::redirectUri));

        return clientBuilder;
    }

    private Duration getTokenLiveTime(String clientTokenLiveTime, Duration tokenLiveTime) {
        if (!StringUtils.hasText(clientTokenLiveTime)) {
            return tokenLiveTime;
        }

        try {
            return DurationUnit.getDuration(clientTokenLiveTime);
        } catch (Exception e) {
            throw new OAuthClientException("clientTokenLiveTime [" + clientTokenLiveTime + "] is invalid", e);
        }
    }

}
