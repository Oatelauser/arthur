package com.arthur.nacos.client.utils;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.client.env.NacosClientProperties;
import com.alibaba.nacos.client.naming.core.ServerListManager;
import com.alibaba.nacos.client.naming.remote.NamingClientProxyDelegate;
import com.alibaba.nacos.client.naming.remote.http.NamingHttpClientManager;
import com.alibaba.nacos.client.naming.remote.http.NamingHttpClientProxy;
import com.alibaba.nacos.client.naming.utils.InitUtils;
import com.alibaba.nacos.client.security.SecurityProxy;
import com.alibaba.nacos.client.utils.ValidatorUtils;
import com.alibaba.nacos.common.http.client.NacosRestTemplate;
import com.alibaba.nacos.shaded.javax.annotation.Nullable;
import com.arthur.nacos.client.security.ScheduledSecurityProxy;

import java.util.Properties;

/**
 * Nacos命名模块的工具类
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-02
 * @since 1.0
 */
@SuppressWarnings("SpellCheckingInspection")
public abstract class NamingUtils {

	/**
	 * @see #getHttpClientProxy(boolean, Properties, SecurityProxy)
	 */
	public static NamingHttpClientProxy getHttpClientProxy(Properties properties,
			SecurityProxy securityProxy) throws NacosException {
		return getHttpClientProxy(false, properties, securityProxy);
	}

	/**
	 * 参考{@link NamingClientProxyDelegate}实现
	 *
	 * @param autoRefresh   是否使用自动刷新Naocs令牌的{@link ScheduledSecurityProxy}
	 * @param properties    Nacos连接配置信息
	 * @param securityProxy 信息安全连接对象
	 * @return {@link NamingHttpClientProxy}
	 * @throws NacosException 连接配置异常
	 */
	public static NamingHttpClientProxy getHttpClientProxy(boolean autoRefresh, Properties properties,
			@Nullable SecurityProxy securityProxy) throws NacosException {
		NacosClientProperties clientProperties = NacosClientProperties.PROTOTYPE.derive(properties);
		ValidatorUtils.checkInitParam(clientProperties);
		String namespace = InitUtils.initNamespaceForNaming(clientProperties);
		ServerListManager serverListManager = new ServerListManager(clientProperties, namespace);
		if (securityProxy == null) {
			NacosRestTemplate restTemplate = NamingHttpClientManager.getInstance().getNacosRestTemplate();
			securityProxy = new SecurityProxy(serverListManager.getServerList(), restTemplate);
			securityProxy = autoRefresh ? new ScheduledSecurityProxy(securityProxy) : securityProxy;
		}
		return new NamingHttpClientProxy(namespace, securityProxy, serverListManager, clientProperties);
	}

	/**
	 * 创建{@link SecurityProxy}
	 *
	 * @param autoRefresh 是否使用自动刷新Naocs令牌的{@link ScheduledSecurityProxy}
	 * @param properties  Nacos连接配置信息
	 * @return {@link SecurityProxy}
	 */
	public static SecurityProxy getSecurityProxy(boolean autoRefresh, Properties properties) throws NacosException {
		NacosClientProperties clientProperties = NacosClientProperties.PROTOTYPE.derive(properties);
		ValidatorUtils.checkInitParam(clientProperties);
		String namespace = InitUtils.initNamespaceForNaming(clientProperties);
		ServerListManager serverListManager = new ServerListManager(clientProperties, namespace);
		NacosRestTemplate restTemplate = NamingHttpClientManager.getInstance().getNacosRestTemplate();
		SecurityProxy securityProxy = new SecurityProxy(serverListManager.getServerList(), restTemplate);
		return autoRefresh ? new ScheduledSecurityProxy(securityProxy) : securityProxy;
	}

	/**
	 * 解析namesapce名称
	 *
	 * @param properties nacos连接配置信息
	 * @return 命名空间名称
	 */
	public static String resolveNamesaceId(Properties properties) {
		NacosClientProperties clientProperties = NacosClientProperties.PROTOTYPE.derive(properties);
		return InitUtils.initNamespaceForNaming(clientProperties);
	}

}
