package com.arthur.plugin.ssl.filter;

import com.arthur.cloud.gateway.AbstractSmartGatewayFilterFactory;
import com.arthur.common.utils.StringUtils;
import com.arthur.plugin.ssl.support.NettyHttpClientFactory;
import com.arthur.plugin.ssl.support.SslHttpClientSslConfigurer;
import com.arthur.web.model.ServerResponse;
import com.arthur.web.reactive.server.WebServerResponse;
import org.springframework.cloud.gateway.config.HttpClientProperties;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.cert.Certificate;
import java.security.cert.X509CRL;

import static com.arthur.plugin.ssl.constant.ServerStatusEnum.REVOKED_CERT;
import static com.arthur.plugin.ssl.constant.SslConstants.ROUTE_HTTPCLIENT_ATTR;
import static com.arthur.plugin.ssl.support.SslHttpClientSslConfigurer.createSslConfigurer;

/**
 * 网关路由 SSL/TSL 过滤器
 * <p>
 * 用于保证网关和后端服务基于HTTPS交互的安全，基于路由的方式设置不同证书
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-03-20
 * @since 1.0
 */
public class SslGatewayFilterFactory extends AbstractSmartGatewayFilterFactory<SslGatewayFilterFactory.Config> {

	private final WebServerResponse serverResponse;
	private final NettyHttpClientFactory httpClientFactory;

	public SslGatewayFilterFactory(NettyHttpClientFactory httpClientFactory,
			WebServerResponse serverResponse) {
		this.serverResponse = serverResponse;
		this.httpClientFactory = httpClientFactory;
	}

	@Override
	public GatewayFilter apply(Config config) {
		config.setUseInsecureTrustManager(true);
		SslHttpClientSslConfigurer sslConfigurer = createSslConfigurer(config);
		HttpClient httpClient = this.httpClientFactory.newSslInstance(sslConfigurer).createInstance();

		// 吊销证书
		X509CRL crl = sslConfigurer.getX509CRL();
		Certificate cert = this.resolveCertificate(crl, config, sslConfigurer);
		return (exchange, chain) -> Mono.just(crl != null && crl.isRevoked(cert))
			.filter(r -> r)
			.flatMap(r -> handleRevokedCert(crl, cert, exchange))
			.switchIfEmpty(Mono.defer(() -> {
				exchange.getAttributes().put(ROUTE_HTTPCLIENT_ATTR, httpClient);
				return chain.filter(exchange);
			}));
	}

	protected Mono<Void> handleRevokedCert(X509CRL crl, Certificate cert, ServerWebExchange exchange) {
		return serverResponse.writeTo(ServerResponse.ofError(REVOKED_CERT), exchange);
	}

	private Certificate resolveCertificate(X509CRL crl, Config config, SslHttpClientSslConfigurer sslConfigurer) {
		String keyAlias = config.getKeyAlias();
		if (crl == null) {
			return null;
		}
		if (!StringUtils.hasText(keyAlias)) {
			throw new RuntimeException("使用吊销证书必须配置KeyStore条目名 [key-alias]");
		}

		KeyStore keyStore = sslConfigurer.createKeyStore();
		try {
			return keyStore.getCertificate(keyAlias);
		} catch (KeyStoreException e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unused")
	public static class Config extends HttpClientProperties.Ssl {

		private boolean http2Enabled = false;
		private String algorithm;

		/**
		 * 存储keystore文件二进制数据的Base64字符串
		 */
		private String base64KeyStore;

		/**
		 * keystore文件中的别名
		 */
		private String keyAlias;

		/**
		 * 配置吊销证书
		 */
		private String crl;

		/**
		 * 配置吊销证书二进制数据的Base64字符串内容
		 */
		private String base64Crl;

		public boolean getHttp2Enabled() {
			return http2Enabled;
		}

		public void setHttp2Enabled(boolean http2Enabled) {
			this.http2Enabled = http2Enabled;
		}

		public String getAlgorithm() {
			return algorithm;
		}

		public void setAlgorithm(String algorithm) {
			this.algorithm = algorithm;
		}

		public String getBase64KeyStore() {
			return base64KeyStore;
		}

		public void setBase64KeyStore(String base64KeyStore) {
			this.base64KeyStore = base64KeyStore;
		}

		public String getKeyAlias() {
			return keyAlias;
		}

		public void setKeyAlias(String keyStoreAlias) {
			this.keyAlias = keyStoreAlias;
		}

		public String getCrl() {
			return crl;
		}

		public void setCrl(String crl) {
			this.crl = crl;
		}

		public String getBase64Crl() {
			return base64Crl;
		}

		public void setBase64Crl(String base64Crl) {
			this.base64Crl = base64Crl;
		}

	}

}
