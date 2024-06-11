package com.broadtech.arthur.admin.register.infrastructure.client.nacos;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.ServiceInfo;
import com.broadtech.arthur.admin.register.domain.aggregation.entity.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import javax.validation.Valid;
import java.util.List;

/**
 * @author Machenike
 * date 2022/10/18
 * @version 1.0.0
 */
@Component
@RequiredArgsConstructor
public class NacosNamingTemplate {

    private final NamingService namingService;

    public void registerInstance(Server server) throws NacosException {
        namingService.registerInstance(server.getServerName(), server.getGroupName(), server.getIp(), server.getPort(), server.getClusterName());
    }

    public void deregisterInstance(Server server) throws NacosException {
        namingService.registerInstance(server.getServerName(), server.getGroupName(), server.getIp(), server.getPort(), server.getClusterName());
    }

    public List<ServiceInfo> getSubscribeServices() throws NacosException {
        return namingService.getSubscribeServices();
    }

    public String getServerStatus() {
        return namingService.getServerStatus();
    }

    public void shutDown() throws NacosException {
        namingService.shutDown();
    }

}
