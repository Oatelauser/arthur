package com.broadtech.arthur.admin.user.service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/9/14
 */
public interface PermissionService<T> {
    /**
     * 检查令牌非空
     *
     * @param request 请求
     * @return {@link V}
     */
    <V> V checkTokenNotNull(HttpServletRequest request);


    /**
     * 填充背景
     *
     * @param request 请求
     */
    void populateTheContext(HttpServletRequest request);
}
