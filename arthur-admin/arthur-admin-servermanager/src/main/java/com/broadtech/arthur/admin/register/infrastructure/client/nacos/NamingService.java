package com.broadtech.arthur.admin.register.infrastructure.client.nacos;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.listener.EventListener;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.api.naming.pojo.ListView;
import com.alibaba.nacos.api.naming.pojo.ServiceInfo;
import com.alibaba.nacos.api.selector.AbstractSelector;

import java.util.List;

/**
 * @author Machenike
 * date 2022/10/18
 * @version 1.0.0
 */
public interface NamingService {

    /**
     * register a instance to service.
     *
     * @param serviceName name of service
     * @param ip          instance ip
     * @param port        instance port
     * @throws NacosException nacos exception
     */
    void registerInstance(String serviceName, String ip, int port) throws NacosException;

    /**
     * register a instance to service.
     *
     * @param serviceName name of service
     * @param groupName   group of service
     * @param ip          instance ip
     * @param port        instance port
     * @throws NacosException nacos exception
     */
    void registerInstance(String serviceName, String groupName, String ip, int port) throws NacosException;

    /**
     * register a instance to service with specified cluster name.
     *
     * @param serviceName name of service
     * @param ip          instance ip
     * @param port        instance port
     * @param clusterName instance cluster name
     * @throws NacosException nacos exception
     */
    void registerInstance(String serviceName, String ip, int port, String clusterName) throws NacosException;

    /**
     * register a instance to service with specified cluster name.
     *
     * @param serviceName name of service
     * @param groupName   group of service
     * @param ip          instance ip
     * @param port        instance port
     * @param clusterName instance cluster name
     * @throws NacosException nacos exception
     */
    void registerInstance(String serviceName, String groupName, String ip, int port, String clusterName)
            throws NacosException;


    /**
     * deregister instance from a service.
     *
     * @param serviceName name of service
     * @param ip          instance ip
     * @param port        instance port
     * @throws NacosException nacos exception
     */
    void deregisterInstance(String serviceName, String ip, int port) throws NacosException;

    /**
     * deregister instance from a service.
     *
     * @param serviceName name of service
     * @param groupName   group of service
     * @param ip          instance ip
     * @param port        instance port
     * @throws NacosException nacos exception
     */
    void deregisterInstance(String serviceName, String groupName, String ip, int port) throws NacosException;

    /**
     * deregister instance with specified cluster name from a service.
     *
     * @param serviceName name of service
     * @param ip          instance ip
     * @param port        instance port
     * @param clusterName instance cluster name
     * @throws NacosException nacos exception
     */
    void deregisterInstance(String serviceName, String ip, int port, String clusterName) throws NacosException;

    /**
     * deregister instance with specified cluster name from a service.
     *
     * @param serviceName name of service
     * @param groupName   group of service
     * @param ip          instance ip
     * @param port        instance port
     * @param clusterName instance cluster name
     * @throws NacosException nacos exception
     */
    void deregisterInstance(String serviceName, String groupName, String ip, int port, String clusterName)
            throws NacosException;

    /**
     * Get all instances within specified clusters of a service.
     *
     * @param serviceName name of service
     * @param groupName   group of service
     * @param clusters    list of cluster
     * @return A list of qualified instance
     * @throws NacosException nacos exception
     */
    List<Instance> getAllInstances(String serviceName, String groupName, List<String> clusters) throws NacosException;


    /**
     * Get qualified instances within specified clusters of service.
     *
     * @param serviceName name of service
     * @param groupName   group of service
     * @param clusters    list of cluster
     * @param healthy     a flag to indicate returning healthy or unhealthy instances
     * @param subscribe   if subscribe the service
     * @return A qualified list of instance
     * @throws NacosException nacos exception
     */
    List<Instance> selectInstances(
            String serviceName, String groupName, List<String> clusters, boolean healthy,
            boolean subscribe) throws NacosException;

    /**
     * Select one healthy instance of service using predefined load balance strategy.
     *
     * @param serviceName name of service
     * @param groupName   group of service
     * @param clusters    a list of clusters should the instance belongs to
     * @param subscribe   if subscribe the service
     * @return qualified instance
     * @throws NacosException nacos exception
     */
    Instance selectOneHealthyInstance(String serviceName, String groupName, List<String> clusters, boolean subscribe)
            throws NacosException;


    /**
     * Unsubscribe event listener of service.
     *
     * @param serviceName name of service
     * @param groupName   group of service
     * @param clusters    list of cluster
     * @param listener    event listener
     * @throws NacosException nacos exception
     */
    void unsubscribe(String serviceName, String groupName, List<String> clusters, EventListener listener)
            throws NacosException;

    /**
     * Get all service names from server with selector.
     *
     * @param pageNo    page index
     * @param pageSize  page size
     * @param groupName group name
     * @param selector  selector to filter the resource
     * @return list of service names
     * @throws NacosException nacos exception
     */
    ListView<String> getServicesOfServer(int pageNo, int pageSize, String groupName, AbstractSelector selector)
            throws NacosException;

    /**
     * Get all subscribed services of current client.
     *
     * @return subscribed services
     * @throws NacosException nacos exception
     */
    List<ServiceInfo> getSubscribeServices() throws NacosException;

    /**
     * get server health status.
     *
     * @return is server healthy
     */
    String getServerStatus();

    /**
     * Shutdown the resource service.
     *
     * @throws NacosException exception.
     */
    void shutDown() throws NacosException;


}
