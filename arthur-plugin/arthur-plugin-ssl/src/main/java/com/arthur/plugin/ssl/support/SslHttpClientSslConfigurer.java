package com.arthur.plugin.ssl.support;

import com.arthur.plugin.ssl.filter.SslGatewayFilterFactory;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.cloud.gateway.config.HttpClientSslConfigurer;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import javax.net.ssl.KeyManagerFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509CRL;
import java.util.Base64;

/**
 * 修改{@link HttpClientSslConfigurer}适配自定义的SSL
 * <p>
 * TODO：等待HttpClient支持调用X509ExtendedKeyManager#chooseEngineClientAlias方法
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-03-22
 * @since 1.0
 */
public class SslHttpClientSslConfigurer extends HttpClientSslConfigurer {

	private static final String EMPTY = "NONE";

	private SslHttpClientSslConfigurer(SslGatewayFilterFactory.Config sslProperties,
		ServerProperties serverProperties) {
		super(sslProperties, serverProperties);
		// 设置默认值，允许通过 configureSsl 方法
		if (StringUtils.hasText(sslProperties.getBase64KeyStore())) {
			sslProperties.setKeyStore(EMPTY);
		}
	}

	@Override
	protected KeyManagerFactory getKeyManagerFactory() {
		SslGatewayFilterFactory.Config ssl = (SslGatewayFilterFactory.Config) getSslProperties();
		String algorithm = ssl.getAlgorithm();
		String keyAlias = ssl.getKeyAlias();
		algorithm = StringUtils.hasText(algorithm) ? algorithm : KeyManagerFactory.getDefaultAlgorithm();
		try {
			if (ssl.getKeyStore() != null && ssl.getKeyStore().length() > 0) {
				KeyManagerFactory keyManagerFactory = KeyManagerFactoryProvider.getInstance(keyAlias, algorithm);
				char[] keyPassword = ssl.getKeyPassword() != null ? ssl.getKeyPassword().toCharArray() : null;

				if (keyPassword == null && ssl.getKeyStorePassword() != null) {
					keyPassword = ssl.getKeyStorePassword().toCharArray();
				}

				keyManagerFactory.init(this.createKeyStore(), keyPassword);
				return keyManagerFactory;
			}
			return null;
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public KeyStore createKeyStore() {
		SslGatewayFilterFactory.Config ssl = (SslGatewayFilterFactory.Config) getSslProperties();

		try {
			KeyStore store = ssl.getKeyStoreProvider() != null
				? KeyStore.getInstance(ssl.getKeyStoreType(), ssl.getKeyStoreProvider())
				: KeyStore.getInstance(ssl.getKeyStoreType());
			try {
				InputStream is;
				if (StringUtils.hasText(ssl.getBase64KeyStore())) {
					byte[] keyStore = Base64.getDecoder().decode(ssl.getBase64KeyStore()
						.getBytes(StandardCharsets.UTF_8));
					is = new ByteArrayInputStream(keyStore);
				} else {
					URL url = ResourceUtils.getURL(ssl.getKeyStore());
					is = url.openStream();
				}
				store.load(is, ssl.getKeyStorePassword() != null ? ssl.getKeyStorePassword().toCharArray() : null);
			} catch (Exception e) {
				throw new RuntimeException("Could not load key store ' " + ssl.getKeyStore() + "'", e);
			}

			return store;
		} catch (KeyStoreException | NoSuchProviderException e) {
			throw new RuntimeException("Could not load KeyStore for given type and provider", e);
		}
	}

	//@Override
	//protected void setTrustManager(SslContextBuilder sslContextBuilder, X509Certificate... certs) {
	//	TrustManagerFactory factory = InsecureTrustManagerFactory.INSTANCE;
	//	TrustManager[] trustManagers = factory.getTrustManagers();
	//	X509CRL crl = this.getX509CRL();
	//	if (crl != null) {
	//		for (int i = 0; i < trustManagers.length; i++) {
	//			TrustManager trustManager = trustManagers[i];
	//			if (!(trustManager instanceof CrlTrustManager)) {
	//				trustManagers[i] = new CrlTrustManager(getX509CRL(), certs, (X509TrustManager) trustManager);
	//			}
	//		}
	//	}
	//
	//	super.setTrustManager(sslContextBuilder, new DelegatingTrustManagerFactory(trustManagers));
	//}
	//
	//@Override
	//protected void setTrustManager(SslContextBuilder sslContextBuilder, TrustManagerFactory factory) {
	//	TrustManager[] trustManagers = factory.getTrustManagers();
	//	X509CRL crl = this.getX509CRL();
	//	if (crl != null) {
	//		for (int i = 0; i < trustManagers.length; i++) {
	//			TrustManager trustManager = trustManagers[i];
	//			if (!(trustManager instanceof CrlTrustManager)) {
	//				trustManagers[i] = new CrlTrustManager(getX509CRL(), (X509TrustManager) trustManager);
	//			}
	//		}
	//	}
	//
	//	super.setTrustManager(sslContextBuilder, new DelegatingTrustManagerFactory(trustManagers));
	//}

	/**
	 * 加载吊销证书CRL
	 *
	 * @return {@link X509CRL}
	 */
	public X509CRL getX509CRL() {
		SslGatewayFilterFactory.Config ssl = (SslGatewayFilterFactory.Config) getSslProperties();
		String crl = ssl.getCrl();
		String base64Crl = ssl.getBase64Crl();
		if (!StringUtils.hasText(crl) && !StringUtils.hasText(base64Crl)) {
			return null;
		}

		CertificateFactory certificateFactory;
		try {
			certificateFactory = CertificateFactory.getInstance("X.509");
		} catch (CertificateException e) {
			throw new RuntimeException("Could not load CertificateFactory X.509", e);
		}

		InputStream is;
		try {
			if (StringUtils.hasText(base64Crl)) {
				is = new ByteArrayInputStream(Base64.getDecoder().decode(ssl.getBase64Crl()
					.getBytes(StandardCharsets.UTF_8)));
			} else {
				URL url = ResourceUtils.getURL(ssl.getCrl());
				is = url.openStream();
			}

			return (X509CRL) certificateFactory.generateCRL(is);
		} catch (Exception e) {
			throw new RuntimeException("Could not load X509CRL for given crl info", e);
		}
	}

	public static SslHttpClientSslConfigurer createSslConfigurer(SslGatewayFilterFactory.Config config) {
		ServerProperties serverProperties = new ServerProperties();
		serverProperties.getHttp2().setEnabled(config.getHttp2Enabled());
		return new SslHttpClientSslConfigurer(config, serverProperties);
	}

}
