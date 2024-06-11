package com.broadtech.arthur.admin.register.domain.facade;

import com.broadtech.arthur.admin.register.domain.aggregation.entity.Server;

/**
 * @author Machenike
 * date 2022/10/19
 * @version 1.0.0
 */
public interface ServerRegisterFacade {


    /**
     * 注册
     *
     * @param server 服务器
     */
    void register(Server server);

    /**
     * 取消
     *
     * @param server 服务器
     */
    void deregister(Server server);

}
