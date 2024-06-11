package com.broadtech.arthur.admin.register.infrastructure.client.nacos;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.listener.EventListener;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.api.naming.pojo.ListView;
import com.alibaba.nacos.api.naming.pojo.ServiceInfo;
import com.alibaba.nacos.api.selector.AbstractSelector;
import com.alibaba.nacos.client.naming.NacosNamingService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author Machenike
 * date 2022/10/18
 * @version 1.0.0
 */
public abstract class AbstractNamingService implements NamingService {

    @Autowired
    com.alibaba.nacos.api.naming.NamingService namingService;

    @Override
    public void registerInstance(String serviceName, String groupName, String ip, int port, String clusterName) throws NacosException {
        namingService.registerInstance(serviceName, groupName, ip, port, clusterName);
    }

    @Override
    public void deregisterInstance(String serviceName, String groupName, String ip, int port, String clusterName) throws NacosException {
        namingService.deregisterInstance(serviceName, groupName, ip, port, clusterName);
    }

    @Override
    public List<Instance> getAllInstances(String serviceName, String groupName, List<String> clusters) throws NacosException {
        return namingService.getAllInstances(serviceName, groupName, clusters);
    }

    @Override
    public List<Instance> selectInstances(String serviceName, String groupName, List<String> clusters, boolean healthy, boolean subscribe) throws NacosException {
        return namingService.selectInstances(serviceName, groupName, clusters, healthy, subscribe);
    }

    @Override
    public Instance selectOneHealthyInstance(String serviceName, String groupName, List<String> clusters, boolean subscribe) throws NacosException {
        return namingService.selectOneHealthyInstance(serviceName, groupName, clusters, subscribe);
    }


    @Override
    public void unsubscribe(String serviceName, String groupName, List<String> clusters, EventListener listener) throws NacosException {
        namingService.unsubscribe(serviceName, groupName, clusters, listener);
    }

    @Override
    public ListView<String> getServicesOfServer(int pageNo, int pageSize, String groupName, AbstractSelector selector) throws NacosException {
        return namingService.getServicesOfServer(pageNo, pageSize, groupName, selector);
    }

    @Override
    public List<ServiceInfo> getSubscribeServices() throws NacosException {
        return namingService.getSubscribeServices();
    }


    @Override
    public String getServerStatus() {
        return namingService.getServerStatus();
    }

    @Override
    public void shutDown() throws NacosException {
        namingService.shutDown();

    }




}
