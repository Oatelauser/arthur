package com.broadtech.arthur.admin.register.domain.facade.impl;

import com.alibaba.nacos.api.exception.NacosException;
import com.broadtech.arthur.admin.register.domain.facade.ServerRegisterFacade;
import com.broadtech.arthur.admin.register.domain.aggregation.entity.Server;
import com.broadtech.arthur.admin.register.infrastructure.client.nacos.NacosNamingTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Machenike
 * date 2022/10/19
 * @version 1.0.0
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class NacosServerRegisterFacade implements ServerRegisterFacade {

    private final NacosNamingTemplate namingTemplate;

    @Override
    public void register(Server server) {
        try {
             namingTemplate.registerInstance(server);
        } catch (NacosException e) {
            log.error(e.getErrMsg(), e);
            throw new RuntimeException(e);
        }

    }

    @Override
    public void deregister(Server server) {
        try {
            namingTemplate.deregisterInstance(server);
        } catch (NacosException e) {
            log.error(e.getErrMsg(), e);
            throw new RuntimeException(e);
        }

    }
}
