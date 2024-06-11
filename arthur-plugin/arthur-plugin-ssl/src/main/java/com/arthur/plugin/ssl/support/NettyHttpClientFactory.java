package com.arthur.plugin.ssl.support;

import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.cloud.gateway.config.HttpClientCustomizer;
import org.springframework.cloud.gateway.config.HttpClientFactory;
import org.springframework.cloud.gateway.config.HttpClientProperties;
import org.springframework.cloud.gateway.config.HttpClientSslConfigurer;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.util.List;

/**
 * 用于创建共享连接池信息的{@link HttpClient}
 * <p>
 * 1.获取公共的连接池{@link ConnectionProvider}
 * 2.替换{@link HttpClientSslConfigurer}用于设置定制的SSL
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-03-22
 * @since 1.0
 */
public class NettyHttpClientFactory extends HttpClientFactory {

	private ConnectionProvider connectionProvider;

	public NettyHttpClientFactory(HttpClientProperties properties, ServerProperties serverProperties,
			List<HttpClientCustomizer> customizers) {
		super(properties, serverProperties, customizers);
	}

	public NettyHttpClientFactory(HttpClientProperties properties, ServerProperties serverProperties,
			HttpClientSslConfigurer sslConfigurer, List<HttpClientCustomizer> customizers) {
		super(properties, serverProperties, sslConfigurer, customizers);
	}

	@Override
	protected ConnectionProvider buildConnectionProvider(HttpClientProperties properties) {
		if (this.connectionProvider == null) {
			this.connectionProvider = super.buildConnectionProvider(properties);
		}
		return this.connectionProvider;
	}

	@Override
	public HttpClient createInstance() {
		return super.createInstance();
	}

	public NettyHttpClientFactory newSslInstance(HttpClientSslConfigurer sslConfigurer) {
		NettyHttpClientFactory httpClientFactory = new NettyHttpClientFactory(properties,
			serverProperties, sslConfigurer, customizers);
		httpClientFactory.connectionProvider = this.connectionProvider;
		return httpClientFactory;
	}

}
