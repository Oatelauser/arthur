package com.broadtech.arthur.admin.register.infrastructure.db.repository;

import com.broadtech.arthur.admin.register.domain.aggregation.entity.Server;
import com.broadtech.arthur.admin.register.domain.repository.ServerRegisterRepo;
import com.broadtech.arthur.admin.register.infrastructure.db.po.ServerRegisterPo;
import com.broadtech.arthur.admin.register.infrastructure.db.service.ServerRegisterService;
import com.broadtech.arthur.admin.register.infrastructure.enums.ServerState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Machenike
 * date 2022/10/19
 * @version 1.0.0
 */
@Repository
@Slf4j
@RequiredArgsConstructor
public class ServerRegisterRepoImpl implements ServerRegisterRepo {

    private final ServerRegisterService serverRegisterService;


    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public ServerRegisterPo register(Server server) {
        ServerRegisterPo serverRegisterPo = new ServerRegisterPo();
        BeanUtils.copyProperties(server, serverRegisterPo);
        serverRegisterPo.setAction(ServerState.REGISTER.getState());
        serverRegisterService.save(serverRegisterPo);
        return serverRegisterPo;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public ServerRegisterPo deregister(Server server) {
        ServerRegisterPo serverRegisterPo = new ServerRegisterPo();
        BeanUtils.copyProperties(server, serverRegisterPo);
        serverRegisterPo.setAction(ServerState.deregister.getState());
        serverRegisterService.save(serverRegisterPo);
        return serverRegisterPo;
    }
}
