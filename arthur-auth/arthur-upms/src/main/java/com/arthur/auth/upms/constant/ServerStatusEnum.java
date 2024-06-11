package com.arthur.auth.upms.constant;

import com.arthur.web.server.ServerStatus;

/**
 * 响应枚举类
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-04-19
 * @since 1.0
 */
public enum ServerStatusEnum implements ServerStatus {

	UPLOAD_EMPTY_DATA("UMP0000", "上传的数据为空")
    ;

    final String code;
    final String msg;

    ServerStatusEnum(String code, String msg) {
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
