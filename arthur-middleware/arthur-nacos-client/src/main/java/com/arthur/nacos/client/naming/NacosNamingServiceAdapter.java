package com.arthur.nacos.client.naming;

import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.client.env.NacosClientProperties;
import com.alibaba.nacos.client.naming.NacosNamingService;
import com.alibaba.nacos.client.naming.core.ServerListManager;
import com.alibaba.nacos.client.naming.remote.NamingClientProxy;
import com.alibaba.nacos.client.naming.remote.http.NamingHttpClientProxy;
import com.alibaba.nacos.client.naming.utils.InitUtils;
import com.alibaba.nacos.client.security.SecurityProxy;
import com.alibaba.nacos.client.utils.ValidatorUtils;
import com.alibaba.nacos.shaded.javax.annotation.Nullable;
import com.arthur.nacos.client.security.SecurityProxyFactory;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.Properties;

/**
 * 适配{@link NacosNamingService}，提供需要资源更少的{@link com.alibaba.nacos.api.naming.NamingService}
 *
 * <ul>资源优化情况
 *     <li>减少临时注册的gRPC心跳带来的性能损耗</li>
 *     <li>减少{@link SecurityProxy}定时更新令牌的性能损耗，该类只需要一个公共即可</li>
 * </ul>
 * 	使用该类存在一个问题，订阅相关的API无法使用，具体查看{@link NamingHttpClientProxy} API定义
 * 	<p>
 * 	一般情况下也不会订阅服务相关的信息
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-03
 * @since 1.0
 */
@SuppressWarnings({ "unused", "SpellCheckingInspection" })
public class NacosNamingServiceAdapter extends NacosNamingService {

	private static final VarHandle clientProxyVarHandle;

	static {
		try {
			clientProxyVarHandle = MethodHandles.privateLookupIn(NacosNamingService.class, MethodHandles.lookup())
				.findVarHandle(NacosNamingService.class, "clientProxy", NamingClientProxy.class);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	public NacosNamingServiceAdapter(String serverList) throws NacosException {
		this(serverList, null);
	}

	public NacosNamingServiceAdapter(String serverList, @Nullable SecurityProxy securityProxy) throws NacosException {
		super(serverList);
		Properties properties = new Properties();
		properties.setProperty(PropertyKeyConst.SERVER_ADDR, serverList);
		this.afterProperties(properties, securityProxy);
	}

	public NacosNamingServiceAdapter(Properties properties) throws NacosException {
		this(properties, null);
	}

	public NacosNamingServiceAdapter(Properties properties, @Nullable SecurityProxy securityProxy) throws NacosException {
		super(properties);
		this.afterProperties(properties, securityProxy);
	}

	protected void afterProperties(Properties properties, @Nullable SecurityProxy securityProxy) throws NacosException {
		this.shutDown();

		if (securityProxy == null) {
			securityProxy = SecurityProxyFactory.obtainSecurityProxy(properties);
		}

		NacosClientProperties clientProperties = NacosClientProperties.PROTOTYPE.derive(properties);
		ValidatorUtils.checkInitParam(clientProperties);
		String namespace = InitUtils.initNamespaceForNaming(clientProperties);
		ServerListManager serverListManager = new ServerListManager(clientProperties, namespace);
		NamingHttpClientProxy httpClientProxy = new NamingHttpClientProxy(namespace, securityProxy,
			serverListManager, clientProperties);
		clientProxyVarHandle.set(this, httpClientProxy);
	}

}
