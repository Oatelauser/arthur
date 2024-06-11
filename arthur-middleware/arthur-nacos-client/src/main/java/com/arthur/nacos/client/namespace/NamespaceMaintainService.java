package com.arthur.nacos.client.namespace;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.common.lifecycle.Closeable;

import java.util.Properties;

import static com.alibaba.nacos.client.naming.utils.UtilAndComs.DEFAULT_NAMESPACE_ID;

/**
 * 命名空间服务管理器
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-02
 * @since 1.0
 */
@SuppressWarnings("SpellCheckingInspection")
public interface NamespaceMaintainService extends Closeable {

    /**
     * 基于命名空间名获取{@link NamespaceService}
     *
     * @param namespaceId 命名空间ID
     * @return {@link NamespaceService}
     */
    NamespaceService getNamespaceService(String namespaceId);

    /**
     * 添加命名空间服务
     *
     * @param properties Nacos连接配置
     */
    NamespaceService addNamespaceService(Properties properties) throws NacosException;

    /**
     * 删除命名空间服务
     *
     * @param namespaceId 命名空间ID
     */
    void deleteNamespaceService(String namespaceId) throws NacosException;

    /**
     * 获取默认的命名空间
     */
    default NamespaceService getNamespaceService() {
        return this.getNamespaceService(DEFAULT_NAMESPACE_ID);
    }

}
