package com.arthur.nacos.client.namespace;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.client.naming.remote.http.NamingHttpClientProxy;
import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import com.alibaba.nacos.client.security.SecurityProxy;
import com.alibaba.nacos.common.utils.StringUtils;
import com.alibaba.nacos.shaded.javax.annotation.Nullable;
import com.arthur.nacos.client.model.Namespace;
import com.arthur.nacos.client.utils.NamingUtils;

import java.util.*;

import static com.alibaba.nacos.api.naming.CommonParams.NAMESPACE_ID;
import static com.alibaba.nacos.client.naming.utils.UtilAndComs.DEFAULT_NAMESPACE_ID;
import static com.arthur.nacos.client.constant.NacosClientConstants.*;

/**
 * Nacos命名空间操作实现类
 * <ul>
 * <li>新增命名空间：检查命名空间的ID和名称是否有重复</oi>
 * <li>删除命名空间：无法删除默认命名空间</oi>
 * <li>更新命名空间</oi>
 * <li>查询指定命名空间</oi>
 * <li>查询所有命名空间</oi>
 * </ul>
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-02
 * @since 1.0
 */
@SuppressWarnings("SpellCheckingInspection")
public class NacosNamespaceService implements NamespaceService {

	private final Properties properties;
	private final NamingHttpClientProxy httpClientProxy;

	public NacosNamespaceService(Properties properties, NamingHttpClientProxy httpClientProxy) {
		this.httpClientProxy = httpClientProxy;
		this.properties = properties;
	}

	public NacosNamespaceService(Properties properties) throws NacosException {
		this(false, properties, null);
	}

	public NacosNamespaceService(boolean autoRefresh, Properties properties) throws NacosException {
		this(autoRefresh, properties, null);
	}

	public NacosNamespaceService(boolean autoRefresh, Properties properties, @Nullable SecurityProxy securityProxy) throws NacosException {
		this(properties, NamingUtils.getHttpClientProxy(autoRefresh, properties, securityProxy));
	}

	@Override
	public String getNamespaceId() {
		return NamingUtils.resolveNamesaceId(properties);
	}

	@Override
	public Namespace getNamespace(String namespaceId) throws NacosException {
		if (StringUtils.isBlank(namespaceId)) {
			namespaceId = DEFAULT_NAMESPACE_ID;
		}

		Map<String, String> params = new HashMap<>(4);
		params.put("show", "all");
		params.put(NAMESPACE_ID, namespaceId);
		String result = httpClientProxy.reqApi(NS_URL, params, "GET");
		JSONObject response = JSON.parseObject(result);
		return response.to(Namespace.class);
	}

	@Override
	public List<Namespace> getAllNamespace() throws NacosException {
		String result = httpClientProxy.reqApi(NS_URL, new HashMap<>(4), "GET");
		JSONObject response = JSON.parseObject(result);
		validateHttpResponse(response);
		JSONArray data = response.getJSONArray("data");
		return CollectionUtils.isEmpty(data) ?
			Collections.emptyList() : data.toJavaList(Namespace.class);
	}

	@Override
	public boolean addNamespace(String namespaceName, String namespaceDesc) throws NacosException {
		return this.addNamespace(null, namespaceName, namespaceDesc);
	}

	@Override
	public boolean addNamespace(String namespaceId, String namespaceName, String namespaceDesc) throws NacosException {
		if (StringUtils.isBlank(namespaceName)) {
			throw new NacosException(NS_INVALID_PARAM, "namespaceName is empty");
		}
		if (!this.validateNamespce(namespaceId, namespaceName)) {
			return false;
		}

		Map<String, String> params = new HashMap<>(8);
		params.put("customNamespaceId", namespaceId == null ? "" : namespaceId);
		params.put("namespaceName", namespaceName);
		params.put("namespaceDesc", namespaceDesc);
		params.put(NAMESPACE_ID, null);
		String result = httpClientProxy.reqApi(NS_URL, params, "POST");
		return Boolean.parseBoolean(result);
	}

	@Override
	public boolean updateNamespace(String namespaceId, String namespaceName, String namespaceDesc) throws NacosException {
		if (StringUtils.isBlank(namespaceId) || DEFAULT_NAMESPACE_ID.equals(namespaceId)) {
			throw new NacosException(NS_INVALID_ID, "Default namespace cannot edit");
		}

		Map<String, String> params = new HashMap<>(8);
		params.put("namespace", namespaceId);
		params.put("namespaceShowName", namespaceName);
		params.put("namespaceDesc", namespaceDesc);
		params.put(NAMESPACE_ID, "");
		String result = httpClientProxy.reqApi(NS_URL, params, "PUT");
		return Boolean.parseBoolean(result);
	}

	@Override
	public boolean deleteNamespace(String namespaceId) throws NacosException {
		if (StringUtils.isBlank(namespaceId) || DEFAULT_NAMESPACE_ID.equals(namespaceId)) {
			throw new NacosException(NS_INVALID_ID, "Default namespace cannot delete");
		}

		Map<String, String> params = new HashMap<>(4);
		params.put(NS_NAMESPACE_ID, namespaceId);
		String result = httpClientProxy.reqApi(NS_URL, params, "DELETE");
		return Boolean.parseBoolean(result);
	}

	private boolean validateNamespce(String namespaceId, String namespaceName) throws NacosException {
		for (Namespace namespace : this.getAllNamespace()) {
			if (namespace.getNamespaceShowName().equals(namespaceName) ||
				StringUtils.hasText(namespaceId) && namespace.getNamespace().equals(namespaceId)) {
				return false;
			}
		}
		return true;
	}

	public Properties getProperties() {
		return properties;
	}

	static void validateHttpResponse(JSONObject response) throws NacosException {
		int code = response.getIntValue("code");
		if (code != 200) {
			throw new NacosException(code, response.getString("message"));
		}
	}

	@Override
	public void shutdown() throws NacosException {
		if (httpClientProxy != null) {
			httpClientProxy.shutdown();
		}
	}

}
