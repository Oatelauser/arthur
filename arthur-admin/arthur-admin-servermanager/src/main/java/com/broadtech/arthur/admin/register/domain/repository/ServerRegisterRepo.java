package com.broadtech.arthur.admin.register.domain.repository;

import com.broadtech.arthur.admin.register.domain.aggregation.entity.Server;
import com.broadtech.arthur.admin.register.infrastructure.db.po.ServerRegisterPo;
import org.springframework.stereotype.Repository;

/**
 * @author Machenike
 * date 2022/10/19
 * @version 1.0.0
 */
@Repository
public interface ServerRegisterRepo {

    /**
     * 注册
     *
     * @param server 服务器
     * @return {@link Server}
     */
    ServerRegisterPo register(Server server);

    /**
     * 取消
     *
     * @param server 服务器
     * @return int
     */
    ServerRegisterPo deregister(Server server);

}
