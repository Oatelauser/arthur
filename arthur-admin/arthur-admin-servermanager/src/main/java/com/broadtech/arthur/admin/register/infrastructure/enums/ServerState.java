package com.broadtech.arthur.admin.register.infrastructure.enums;

import lombok.Getter;

/**
 * @author Machenike
 * date 2022/10/19
 * @version 1.0.0
 */
public enum ServerState {


    /**
     * 注册
     */
    REGISTER(0),
    /**
     * 取消
     */
    deregister(1);


    @Getter
    Integer state;

  ServerState(int state) {
        this.state = state;
    }
}
