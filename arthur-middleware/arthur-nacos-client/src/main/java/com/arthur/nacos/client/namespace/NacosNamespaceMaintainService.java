package com.arthur.nacos.client.namespace;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.client.env.NacosClientProperties;
import com.alibaba.nacos.client.naming.core.ServerListManager;
import com.alibaba.nacos.client.naming.remote.http.NamingHttpClientProxy;
import com.alibaba.nacos.client.naming.utils.InitUtils;
import com.alibaba.nacos.client.security.SecurityProxy;
import com.alibaba.nacos.client.utils.ValidatorUtils;
import com.alibaba.nacos.shaded.javax.annotation.Nullable;
import com.arthur.nacos.client.naming.NamespaceNamingHttpClientProxy;
import com.arthur.nacos.client.security.SecurityProxyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 命名空间服务管理器
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-02
 * @since 1.0
 */
@SuppressWarnings("unused")
public class NacosNamespaceMaintainService implements NamespaceMaintainService {

    private static final Logger LOG = LoggerFactory.getLogger(NacosNamespaceMaintainService.class);

    /**
     * 是否定时缓存所有的命名空间
     *
     * @see NamespaceService#getAllNamespace()
     */
    private boolean scheduledCachingAllNamespaces = false;

    /**
     * 缓存{@link NamespaceService}
     */
    private final Map<String/*命名空间ID*/, NacosNamespaceService> namespaceServices = new ConcurrentHashMap<>();

    @Override
    @Nullable
    public NamespaceService getNamespaceService(String namespaceId) {
        return namespaceServices.get(namespaceId);
    }

    @Override
    public NamespaceService addNamespaceService(Properties properties) throws NacosException {
        NacosClientProperties clientProperties = NacosClientProperties.PROTOTYPE.derive(properties);
        ValidatorUtils.checkInitParam(clientProperties);
        String namespace = InitUtils.initNamespaceForNaming(clientProperties);
        NacosNamespaceService namespaceService = namespaceServices.get(namespace);
        if (namespaceService != null) {
            return namespaceService;
        }

        ServerListManager serverListManager = new ServerListManager(clientProperties, namespace);
        SecurityProxy securityProxy = SecurityProxyFactory.obtainSecurityProxy(properties);

        NamingHttpClientProxy httpClientProxy = new NamespaceNamingHttpClientProxy(namespace, securityProxy,
                serverListManager, clientProperties);
        namespaceService = scheduledCachingAllNamespaces ? new ScheduledNamespaceService(properties, httpClientProxy) :
                new NacosNamespaceService(properties, httpClientProxy);
        namespaceServices.put(namespace, namespaceService);
        return namespaceService;
    }

    @Override
    public void deleteNamespaceService(String namespaceId) throws NacosException {
        namespaceServices.remove(namespaceId).shutdown();
    }

    @Override
    public void shutdown() {
        for (NacosNamespaceService namespaceService : namespaceServices.values()) {
            try {
                namespaceService.shutdown();
            } catch (NacosException e) {
                LOG.warn("NamespaceService close error: {}", e.getErrMsg());
            }
        }
    }

    public boolean getScheduledCachingAllNamespaces() {
        return scheduledCachingAllNamespaces;
    }

    public void setScheduledCachingAllNamespaces(boolean scheduledCachingAllNamespaces) {
        this.scheduledCachingAllNamespaces = scheduledCachingAllNamespaces;
    }

}
