package com.arthur.boot.file.constants;

import com.arthur.web.server.ServerStatus;

/**
 * 响应枚举类
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-04-20
 * @since 1.0
 */
public enum ServerStatusEnum implements ServerStatus {

    EMPTY_DATA_FILE("AFL000", "数据为空不允许生成文件"),
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
