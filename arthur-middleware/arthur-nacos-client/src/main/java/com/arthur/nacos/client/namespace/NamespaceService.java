package com.arthur.nacos.client.namespace;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.common.lifecycle.Closeable;
import com.arthur.nacos.client.model.Namespace;

import java.util.List;

/**
 * 命名空间操作API
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-02
 * @since 1.0
 */
public interface NamespaceService extends Closeable {

	/**
	 * 命名空间的名称
	 */
	String getNamespaceId();

	/**
	 * 获取默认命名空间的详情
	 *
	 * @return {@link Namespace}
	 */
	default Namespace getNamespace() throws NacosException {
		return this.getNamespace(null);
	}

	/**
	 * 获取命名空间的详情
	 *
	 * @param namespaceId 命名空间ID
	 * @return {@link Namespace}
	 */
	Namespace getNamespace(String namespaceId) throws NacosException;

	/**
	 * 获取Nacos所有的命名空间信息
	 *
	 * @return {@link Namespace}
	 */
	List<Namespace> getAllNamespace() throws NacosException;

	/**
	 * 创建命名空间
	 *
	 * @param namespaceName 命名空间名称
	 * @param namespaceDesc 命名空间描述
	 * @return 是否创建成功
	 */
	boolean addNamespace(String namespaceName, String namespaceDesc) throws NacosException;

	/**
	 * 创建命名空间
	 *
	 * @param namespaceId   命名空间ID
	 * @param namespaceName 命名空间名称
	 * @param namespaceDesc 命名空间描述
	 * @return 是否创建成功
	 */
	boolean addNamespace(String namespaceId, String namespaceName, String namespaceDesc) throws NacosException;

	/**
	 * 更新命名空间
	 *
	 * @param namespaceId   命名空间ID
	 * @param namespaceName 命名空间名称
	 * @param namespaceDesc 命名空间描述
	 * @return 是否更新成功
	 */
	boolean updateNamespace(String namespaceId, String namespaceName, String namespaceDesc) throws NacosException;

	/**
	 * 删除命名空间
	 * <p>
	 * 支持该功能必须使用{@link com.arthur.nacos.client.constant.NacosClientConstants#NS_NAMESPACE_ID}设置命名空间ID参数名
	 *
	 * @param namespaceId 命名空间ID
	 * @return 是否删除成功
	 */
	boolean deleteNamespace(String namespaceId) throws NacosException;

}
