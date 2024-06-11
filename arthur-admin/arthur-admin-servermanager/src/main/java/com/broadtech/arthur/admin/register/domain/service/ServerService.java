package com.broadtech.arthur.admin.register.domain.service;

import com.broadtech.arthur.admin.register.application.vo.ServerVo;
import com.broadtech.arthur.admin.register.domain.aggregation.entity.Server;

/**
 * @author Machenike
 * date 2022/10/19
 * @version 1.0.0
 */
public interface ServerService {
    /**
     * 注册
     *
     * @param server 服务器
     * @return {@link ServerVo}
     */
    ServerVo register(Server server);

    /**
     * 取消
     *
     * @param server 服务器
     * @return {@link Server}
     */
    ServerVo deregister(Server server);
}
