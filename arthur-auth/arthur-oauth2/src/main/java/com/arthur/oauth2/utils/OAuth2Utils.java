package com.arthur.oauth2.utils;

import com.arthur.oauth2.constant.OAuth2Constants;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;

/**
 * OAuth2工具类
 *
 * @author DearYang
 * @date 2022-08-18
 * @since 1.0
 */
public abstract class OAuth2Utils {

    /**
     * 转换请求头为{@link MultiValueMap}
     *
     * @param request {@link HttpServletRequest}
     * @return 请求头
     */
    public static MultiValueMap<String, String> getParameters(HttpServletRequest request) {
        Map<String, String[]> parameters = request.getParameterMap();
        MultiValueMap<String, String> multiParameters = new LinkedMultiValueMap<>(parameters.size());
        for (Map.Entry<String, String[]> entry : parameters.entrySet()) {
            multiParameters.put(entry.getKey(), List.of(entry.getValue()));
        }

        return multiParameters;
    }

    /**
     * 抛出{@link OAuth2AuthenticationException}异常
     *
     * @param errorCode 异常码
     * @param parameter 参数名
     */
    public static OAuth2AuthenticationException createException(String errorCode, String parameter) {
        OAuth2Error error = new OAuth2Error(errorCode, "OAuth2 Parameter: " + parameter, OAuth2Constants.OAUTH2_INVALID_REQUEST_ERROR_URI);
        return new OAuth2AuthenticationException(error);
    }

    /**
     * 抛出{@link OAuth2AuthenticationException}异常
     *
     * @param errorCode   异常码
     * @param description 异常信息
     * @param url         OAuth2异常信息文档地址
     */
    public static OAuth2AuthenticationException createException(String errorCode, String description, String url) {
        OAuth2Error error = new OAuth2Error(errorCode, description, url);
        return new OAuth2AuthenticationException(error);
    }

	public static OAuth2AuthenticationException createException(String errorCode, String description, String url, Throwable throwable) {
		OAuth2Error error = new OAuth2Error(errorCode, description, url);
		return new OAuth2AuthenticationException(error, throwable);
	}

}
