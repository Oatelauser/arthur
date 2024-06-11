package com.broadtech.arthur.admin.register.application.service.cmd.impl;

import com.broadtech.arthur.admin.register.application.assembler.ServerAssember;
import com.broadtech.arthur.admin.register.application.dto.ServerDto;
import com.broadtech.arthur.admin.register.application.service.cmd.RegisterServerService;
import com.broadtech.arthur.admin.register.application.vo.ServerVo;
import com.broadtech.arthur.admin.register.domain.aggregation.entity.Server;
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
public class RegisterServerServiceImpl implements RegisterServerService {


    private final ServerService serverService;
    private final ServerAssember serverAssember;


    @Override
    public ServerVo register(ServerDto serverDto) {
        Server server = serverAssember.dto2Entity(serverDto);
        return serverService.register(server);
    }

    @Override
    public ServerVo deregister(ServerDto serverDto) {
        Server server = serverAssember.dto2Entity(serverDto);
        return serverService.register(server);
    }
}
