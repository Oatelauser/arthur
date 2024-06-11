package com.arthur.nacos.client.security;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.client.security.SecurityProxy;
import com.alibaba.nacos.plugin.auth.api.RequestResource;

import java.util.Map;
import java.util.Properties;

/**
 * 代理{@link SecurityProxy}提供半自动刷新Token机制
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-03
 * @since 1.0
 */
public class DelegatingSecurityProxy extends SecurityProxy {

	private final SecurityProxy delegate;

	public DelegatingSecurityProxy(SecurityProxy delegate) {
		super(null, null);
		this.delegate = delegate;
	}

	@Override
	public void login(Properties properties) {
		delegate.login(properties);
	}

	@Override
	public Map<String, String> getIdentityContext(RequestResource resource) {
		return delegate.getIdentityContext(resource);
	}

	@Override
	public void shutdown() throws NacosException {
		delegate.shutdown();
	}
	
}
