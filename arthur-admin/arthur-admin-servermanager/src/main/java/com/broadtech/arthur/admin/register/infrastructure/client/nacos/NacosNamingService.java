package com.broadtech.arthur.admin.register.infrastructure.client.nacos;

import com.alibaba.nacos.api.common.Constants;
import com.alibaba.nacos.api.exception.NacosException;
import org.springframework.stereotype.Component;

/**
 * @author Machenike
 * date 2022/10/18
 * @version 1.0.0
 */
@Component
public class NacosNamingService extends AbstractNamingService {

    @Override
    public void registerInstance(String serviceName, String ip, int port) throws NacosException {
        registerInstance(serviceName, Constants.DEFAULT_GROUP, ip, port, Constants.DEFAULT_CLUSTER_NAME);

    }

    @Override
    public void registerInstance(String serviceName, String groupName, String ip, int port) throws NacosException {
        registerInstance(serviceName, groupName, ip, port, Constants.DEFAULT_CLUSTER_NAME);
    }

    @Override
    public void registerInstance(String serviceName, String ip, int port, String clusterName) throws NacosException {
        registerInstance(serviceName, Constants.DEFAULT_GROUP, ip, port, Constants.DEFAULT_CLUSTER_NAME);
    }


    @Override
    public void deregisterInstance(String serviceName, String ip, int port) throws NacosException {
        deregisterInstance(serviceName, Constants.DEFAULT_GROUP, ip, port, Constants.DEFAULT_CLUSTER_NAME);
    }

    @Override
    public void deregisterInstance(String serviceName, String groupName, String ip, int port) throws NacosException {
        deregisterInstance(serviceName, groupName, ip, port, Constants.DEFAULT_CLUSTER_NAME);

    }

    @Override
    public void deregisterInstance(String serviceName, String ip, int port, String clusterName) throws NacosException {
        deregisterInstance(serviceName, Constants.DEFAULT_GROUP, ip, port, clusterName);
    }














}
