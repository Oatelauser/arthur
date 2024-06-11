package com.arthur.oauth2.autoconfigure;

import com.arthur.common.lifecycle.InitializeListener;
import com.arthur.oauth2.constant.OAuth2Constants;
import com.arthur.oauth2.resource.ArthurBearerTokenResolver;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * OAuth2资源服务配置
 *
 * @author DearYang
 * @date 2022-08-14
 * @since 1.0
 */
@ConfigurationProperties(prefix = "arthur.oauth2.resource-server")
public class OAuth2ResourceServerProperties implements InitializeListener {

    private static final String[] DEFAULT_EXCLUDE_URLS = new String[]{ "/error", "/actuator/**", "/v3/api-docs" };

    /**
     * 排除的URL
     *
     * @see ArthurBearerTokenResolver
     */
    private Set<String> excludeUrls = new HashSet<>();
    private OAuth2TokenProperties token;

    public Set<String> getExcludeUrls() {
        return excludeUrls;
    }

    public void setExcludeUrls(Set<String> excludeUrls) {
        this.excludeUrls = excludeUrls;
    }

    public void addExcludeUrl(String url) {
        if (StringUtils.hasText(url)) {
            excludeUrls.add(url);
        }
    }

    public OAuth2TokenProperties getToken() {
        return token;
    }

    public void setToken(OAuth2TokenProperties token) {
        this.token = token;
    }

    @Override
    public void start() {
        CollectionUtils.mergeArrayIntoCollection(DEFAULT_EXCLUDE_URLS, excludeUrls);
    }

    public static class OAuth2TokenProperties {


        /**
         * 令牌所在的请求头
         */
        private String headerName = HttpHeaders.AUTHORIZATION;

        /**
         * 从URL参数或者POST表单参数中提取令牌的参数名
         */
        private String parameterName = OAuth2Constants.AUTHORIZATION_TOKEN_PARAMETER_NAME;

        /**
         * 是否允许从POST表单参数中提取令牌
         */
        private boolean allowFormEncodedBodyParameter = false;

        /**
         * 是否允许从URL参数中提取令牌
         */
        private boolean allowUriQueryParameter = false;

        public String getHeaderName() {
            return headerName;
        }

        public void setHeaderName(String headerName) {
            this.headerName = headerName;
        }

        public String getParameterName() {
            return parameterName;
        }

        public void setParameterName(String parameterName) {
            this.parameterName = parameterName;
        }

        public boolean getAllowFormEncodedBodyParameter() {
            return allowFormEncodedBodyParameter;
        }

        public void setAllowFormEncodedBodyParameter(boolean allowFormEncodedBodyParameter) {
            this.allowFormEncodedBodyParameter = allowFormEncodedBodyParameter;
        }

        public boolean getAllowUriQueryParameter() {
            return allowUriQueryParameter;
        }

        public void setAllowUriQueryParameter(boolean allowUriQueryParameter) {
            this.allowUriQueryParameter = allowUriQueryParameter;
        }
    }

}
