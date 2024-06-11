package com.arthur.web.server;

import com.arthur.web.model.ArthurStatusEnum;

/**
 * 统一接口响应响应码响应信息的抽象接口
 *
 * @author DearYang
 * @date 2022-07-14
 * @see ArthurStatusEnum
 * @since 1.0
 */
public interface ServerStatus {

    /**
     * response code
     *
     * @return code
     */
    String getCode();

    /**
     * response message
     *
     * @return message
     */
    String getMsg();

}
