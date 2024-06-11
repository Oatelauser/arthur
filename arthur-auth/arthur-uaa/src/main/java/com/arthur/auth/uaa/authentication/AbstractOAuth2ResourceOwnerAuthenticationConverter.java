package com.arthur.auth.uaa.authentication;

import com.arthur.oauth2.utils.OAuth2Utils;
import com.arthur.oauth2.utils.SecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.security.oauth2.core.OAuth2ErrorCodes.INVALID_CLIENT;
import static org.springframework.security.oauth2.core.OAuth2ErrorCodes.INVALID_REQUEST;

/**
 * 自定义OAuth2授权模式-4.组装认证对象
 *
 * @author DearYang
 * @date 2022-08-18
 * @see AuthenticationConverter
 * @since 1.0
 */
@SuppressWarnings("unused")
public abstract class AbstractOAuth2ResourceOwnerAuthenticationConverter<T extends AbstractOAuth2ResourceOwnerAuthenticationToken> implements AuthenticationConverter {

    @Override
    public Authentication convert(HttpServletRequest request) {
        // 授权类型
        String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
        if (!this.supports(grantType)) {
            return null;
        }

        // scope
        MultiValueMap<String, String> parameters = OAuth2Utils.getParameters(request);
        String scopeParameter = OAuth2ParameterNames.SCOPE;
        String scope = parameters.getFirst(scopeParameter);
        if (StringUtils.hasText(scope) && parameters.get(scopeParameter).size() != 1) {
            throw OAuth2Utils.createException(INVALID_REQUEST, scopeParameter);
        }

        Set<String> requestScopes = null;
        if (StringUtils.hasText(scope)) {
            String[] array = StringUtils.delimitedListToStringArray(scope, " ");
            requestScopes = Stream.of(array).filter(StringUtils::hasText).collect(Collectors.toSet());
        }

        // 个性化校验
        this.additionalRequestChecks(parameters, request);

        // 认证凭证客户端信息
        Authentication clientPrincipal = SecurityUtils.getAuthentication();
        if (clientPrincipal == null) {
            throw OAuth2Utils.createException(INVALID_REQUEST, INVALID_CLIENT);
        }

        // 拓展信息
        Map<String, Object> additionalParameters = getAdditionalParameters(parameters);
        // 构建令牌对象
        return this.buildTokenAuthentication(clientPrincipal, requestScopes, additionalParameters);
    }

    /**
     * 处理拓展信息
     *
     * @param parameters 拓展信息{@link MultiValueMap}
     * @return 拓展信息
     */
    private Map<String, Object> getAdditionalParameters(MultiValueMap<String, String> parameters) {
        HashMap<String, Object> additionalParameters = new HashMap<>(parameters.size());
        for (Map.Entry<String, List<String>> entry : parameters.entrySet()) {
            String key = entry.getKey();
            if (!OAuth2ParameterNames.GRANT_TYPE.equals(key) && !OAuth2ParameterNames.SCOPE.equals(key)) {
                additionalParameters.put(key, entry.getValue().get(0));
            }
        }

        return additionalParameters;
    }

    /**
     * 个性化校验
     *
     * @param parameters 请求参数{@link OAuth2Utils#getParameters(HttpServletRequest)}
     * @param request    {@link HttpServletRequest}
     */
    public void additionalRequestChecks(MultiValueMap<String, String> parameters, HttpServletRequest request) {
    }

    /**
     * 判断是否支持授权类型
     *
     * @param grantType 授权类型
     * @return yes or no
     */
    public abstract boolean supports(String grantType);

    /**
     * 构建令牌认证对象
     *
     * @param clientPrincipal      客户端凭证信息
     * @param requestScopes        scope
     * @param additionalParameters 附加信息
     * @return {@link T}
     */
    public abstract T buildTokenAuthentication(Authentication clientPrincipal,
		Set<String> requestScopes, Map<String, Object> additionalParameters);

}
