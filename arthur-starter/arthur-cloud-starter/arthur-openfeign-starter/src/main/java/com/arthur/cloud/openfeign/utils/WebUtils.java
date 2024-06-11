package com.arthur.cloud.openfeign.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

/**
 * copy by {@code com.broadtech.arthur.spring.web.utils.WebUtils}
 *
 * @author DearYang
 * @date 2022-08-14
 * @since 1.0
 */
public abstract class WebUtils {

    /**
     * 获取当前的HttpServletRequest对象
     *
     * @return {@link HttpServletRequest}
     */
    public static Optional<HttpServletRequest> getRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return Optional.empty();
        }

        return Optional.of(attributes.getRequest());
    }

    /**
     * 获取当前的HttpServletRequest对象
     *
     * @return {@link HttpServletRequest}
     */
    public static Optional<HttpServletResponse> getResponse() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(attributes.getResponse());
    }

}
