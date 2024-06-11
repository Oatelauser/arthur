package com.arthur.plugin.constant;

import java.security.MessageDigest;

/**
 * 概要描述
 * <p>
 * 详细描述
 *
 * @author DearYang
 * @date 2022-07-14
 * @since 1.0
 */
public interface Constant {


    /**
     * 排除的URL pattern，不走过滤器直接请求的URL
     */
    String LOCAL_DISPATCHER_PATH = "/arthur/**";

    /**
     * 本地加密算法MD5，校验{@link Constant#LOCAL_DISPATCHER_PATH}合法
     * 该参数是调用{@link MessageDigest#getInstance(String)}的参数
     */
    String LOCAL_ENCRYPTION_ALGORITHM = "MD5";

    String CLIENT_RESPONSE_ATTR = "webHandlerClientResponse";

    /**
     * local key
     */
    String LOCAL_KEY = "localKey";

    /**
     * fallback url
     */
    String FALLBACK_SENTINEL_URL = "/fallback/sentinel";

    /**
     * Forwarded-For header name.
     */
    String X_FORWARDED_FOR = "X-Forwarded-For";

    String SLEUTH_TRACE_ID = "traceId";
    String SLEUTH_SPAN_ID = "spanId";
    String SLEUTH_PARENT_SPAN_ID = "parentSpanId";
}
