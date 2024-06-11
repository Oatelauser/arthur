package com.broadtech.arthur.admin.register.domain.service.impl;

import com.broadtech.arthur.admin.register.application.assembler.ServerAssember;
import com.broadtech.arthur.admin.register.application.vo.ServerVo;
import com.broadtech.arthur.admin.register.domain.facade.ServerRegisterFacade;
import com.broadtech.arthur.admin.register.domain.aggregation.entity.Server;
import com.broadtech.arthur.admin.register.domain.repository.ServerRegisterRepo;
import com.broadtech.arthur.admin.register.domain.service.ServerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Machenike
 * date 2022/10/19
 * @version 1.0.0
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ServerServiceImpl implements ServerService {

    private final ServerRegisterRepo   serverRegisterRepo;
    private final ServerRegisterFacade serverRegisterFacade;

    private final ServerAssember serverAssember;

    @Override
    public ServerVo register(Server server) {
        serverRegisterFacade.register(server);

        return serverAssember.po2vo(serverRegisterRepo.register(server));
    }


    @Override
    public ServerVo deregister(Server server) {
        serverRegisterFacade.deregister(server);
        return serverAssember.po2vo(serverRegisterRepo.deregister(server));
    }
}
