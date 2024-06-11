package com.arthur.plugin.result;

import com.arthur.common.response.ServerStatus;

/**
 * 实现{@link ServerStatus}接口，定义常用的接口状态返回
 *
 * @author DearYang
 * @date 2022-07-14
 * @since 1.0
 */
public enum ArthurStatusEnum implements ServerStatus {

    /**
     * Success result enum.
     */
    SUCCESS("200", "access to success!"),

    /**
     * Fail result enum.
     */
    FAIL("999999", "Internal exception in gateway. Please try again later!"),

    /**
     * Payload too large result enum.
     */
    PAYLOAD_TOO_LARGE("403", "Payload too large!"),

    /**
     * Hystrix Fallback, due to a circuit break.
     */
    FALLBACK_HYSTRIX("429", "HystrixPlugin fallback success, please check your service status!"),

    /**
     * Resilience4J Fallback, due to a circuit break.
     */
    FALLBACK_RESILIENCE4J("429", "Resilience4JPlugin fallback success, please check your service status!"),

    /**
     * the default Fallback, due to a circuit break.
     */
    FALLBACK_DEFAULT("429", "the fallback success, please check your service status!"),

    RATE_LIMIT_DENY_EMPTY("430", "the rate limit fail, deny empty key, please check your KeyResolver")
    ;

    final String code;
    final String msg;

    ArthurStatusEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }

}
