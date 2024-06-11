package com.arthur.web.model;

import com.arthur.web.server.ServerStatus;

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
    SUCCESS("000000", "请求成功"),

    /**
     * Fail result enum.
     */
    FAIL("999999", "服务内部异常"),
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
