package com.arthur.common.response;

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
    FAIL("999999", "Internal exception in server. Please try again later!"),
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
