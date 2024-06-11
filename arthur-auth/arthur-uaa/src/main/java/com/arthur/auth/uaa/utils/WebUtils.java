package com.arthur.auth.uaa.utils;

import com.arthur.common.constant.BaseConstants;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.ServletRequestUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * {@link HttpServletRequest}工具类
 *
 * @author DearYang
 * @date 2022-08-26
 * @since 1.0
 */
@SuppressWarnings("unused")
public abstract class WebUtils {

    /**
     * 获取所有的请求参数
     * <p>
     * 存在多个value的时候，逗号拼接
     *
     * @param request {@link HttpServletRequest}
     * @return K-参数名，V-参数值
     */
    public static Map<String, String> getAllParameters(HttpServletRequest request) {
        HashMap<String, String> parameters = new HashMap<>(request.getParameterMap().size());
        for (String parameter : request.getParameterMap().keySet()) {
            parameters.put(parameter, getParameter(parameter, request));
        }

        return parameters;
    }

    /**
     * 获取单个参数的值
     * <p>
     * 存在多个value的时候，逗号拼接
     *
     * @param parameter 请求参数名
     * @param request   {@link HttpServletRequest}
     * @return 参数值
     */
    public static String getParameter(String parameter, HttpServletRequest request) {
        String[] parameters = ServletRequestUtils.getStringParameters(request, parameter);
        if (ObjectUtils.isEmpty(parameters)) {
            return null;
        }

        return String.join(BaseConstants.COMMA, parameter);
    }

}
