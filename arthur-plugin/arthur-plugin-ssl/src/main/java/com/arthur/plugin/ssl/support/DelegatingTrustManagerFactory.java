package com.arthur.plugin.ssl.support;

import io.netty.handler.ssl.util.SimpleTrustManagerFactory;

import javax.net.ssl.ManagerFactoryParameters;
import javax.net.ssl.TrustManager;
import java.security.KeyStore;

/**
 * 适配{@link javax.net.ssl.TrustManagerFactory}
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-03-28
 * @since 1.0
 */
@SuppressWarnings("unused")
public class DelegatingTrustManagerFactory extends SimpleTrustManagerFactory {

	private final TrustManager[] trustManagers;

	public DelegatingTrustManagerFactory(TrustManager... trustManagers) {
		this.trustManagers = trustManagers;
	}

	@Override
	protected void engineInit(KeyStore keyStore) {
	}

	@Override
	protected void engineInit(ManagerFactoryParameters managerFactoryParameters) {
	}

	@Override
	protected TrustManager[] engineGetTrustManagers() {
		return trustManagers;
	}

}
