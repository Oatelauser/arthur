package com.arthur.nacos.client.naming;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.client.env.NacosClientProperties;
import com.alibaba.nacos.client.naming.core.ServerListManager;
import com.alibaba.nacos.client.naming.remote.http.NamingHttpClientProxy;
import com.alibaba.nacos.client.security.SecurityProxy;
import com.alibaba.nacos.common.utils.StringUtils;

import java.util.Map;

import static com.alibaba.nacos.api.naming.CommonParams.NAMESPACE_ID;
import static com.arthur.nacos.client.constant.NacosClientConstants.NS_NAMESPACE_ID;

/**
 * 代理默认的{@link NamingHttpClientProxy}
 * <ul>防止设置的namespace被{@link #getNamespaceId()}覆盖
 * <li>具体查看 {@link com.arthur.nacos.client.namespace.NamespaceService#deleteNamespace(String)}</li>
 * </ul>
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-03
 * @since 1.0
 */
public class NamespaceNamingHttpClientProxy extends NamingHttpClientProxy {

	public NamespaceNamingHttpClientProxy(String namespaceId, SecurityProxy securityProxy,
			ServerListManager serverListManager, NacosClientProperties properties) {
		super(namespaceId, securityProxy, serverListManager, properties);
	}

	/**
	 * 防止namespace冲突，被{@link #getNamespaceId()}覆盖
	 */
	@Override
	public String callServer(String api, Map<String, String> params, Map<String, String> body,
		String curServer, String method) throws NacosException {
		String namespaceId = params.get(NS_NAMESPACE_ID);
		if (StringUtils.hasText(namespaceId)) {
			params.put(NAMESPACE_ID, namespaceId);
		}
		return super.callServer(api, params, body, curServer, method);
	}

}
