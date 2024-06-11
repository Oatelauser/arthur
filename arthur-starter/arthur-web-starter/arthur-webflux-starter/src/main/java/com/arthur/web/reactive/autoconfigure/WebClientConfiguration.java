package com.arthur.web.reactive.autoconfigure;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.reactive.function.client.ClientHttpConnectorAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.function.client.ReactorNettyHttpClientMapper;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.client.reactive.ReactorResourceFactory;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.Http2AllocationStrategy;
import reactor.netty.resources.ConnectionProvider;

import java.nio.charset.StandardCharsets;

/**
 * WebClient自动配置类
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-01-07
 * @since 1.0
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(WebClientProperties.class)
@ConditionalOnProperty(value = "spring.webflux.web-client.enabled", havingValue = "true")
public class WebClientConfiguration {

	private final WebClientProperties properties;

	public WebClientConfiguration(WebClientProperties properties) {
		this.properties = properties;
	}

	@Bean
	public WebClient.Builder webClientBuilder(ClientHttpConnector clientConnector, ObjectProvider<WebClientCustomizer> provider) {
		WebClient.Builder builder = WebClient.builder();
		builder.clientConnector(clientConnector)
			.defaultRequest(request -> request.acceptCharset(StandardCharsets.UTF_8))
			.codecs(codecs -> codecs.defaultCodecs()
				.maxInMemorySize(properties.getCodecsMaxMemorySize()));
		provider.orderedStream().forEach(customizer -> customizer.customize(builder));
		return builder;
	}

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnBean(ClientHttpConnector.class)
	public ReactorNettyHttpClientMapper httpClientMapper() {
		return httpClient -> httpClient.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, properties.getConnectTimeout())
			.doOnConnected(connection ->
				connection.addHandlerLast(new ReadTimeoutHandler(properties.getReadTimeout()))
					.addHandlerLast(new WriteTimeoutHandler(properties.getWriteTimeout())));
	}

	@Bean
	public ReactorResourceFactory clientResourceFactory() {
		ReactorResourceFactory reactorResourceFactory = new ReactorResourceFactory();
		if (!properties.getPoolUseGlobalResources()) {
			reactorResourceFactory.setUseGlobalResources(false);
			reactorResourceFactory.setConnectionProviderSupplier(() -> {
				ConnectionProvider.Builder builder = ConnectionProvider.builder("Webflux");
				this.configureConnectionPool(builder);
				return builder.build();
			});
		}
		return reactorResourceFactory;
	}

	/**
	 * copy by {@code org.springframework.boot.autoconfigure.web.reactive.function.client.ClientHttpConnectorConfiguration.ReactorNetty#reactorClientHttpConnector(ReactorResourceFactory, ObjectProvider)}
	 */
	@Bean
	@Lazy
	@ConditionalOnMissingBean(ClientHttpConnectorAutoConfiguration.class)
	public ClientHttpConnector clientHttpConnector(ReactorResourceFactory reactorResourceFactory,
	   		ObjectProvider<ReactorNettyHttpClientMapper> mapperProvider) {
		ReactorNettyHttpClientMapper mapper = mapperProvider.orderedStream()
			.reduce((before, after) -> (client) -> after.configure(before.configure(client)))
			.orElse((client) -> client);
		return new ReactorClientHttpConnector(reactorResourceFactory, mapper::configure);
	}

	private void configureConnectionPool(ConnectionProvider.Builder connectionBuilder) {
		connectionBuilder.fifo();
		if (properties.getPoolMaxConnects() > 0) {
			connectionBuilder.maxConnections(properties.getPoolMaxConnects());
		}
		if (properties.getPoolMaxIdleTime() != null) {
			connectionBuilder.maxIdleTime(properties.getPoolMaxIdleTime());
		}
		if (properties.getPoolMaxLifeTime() != null) {
			connectionBuilder.maxLifeTime(properties.getPoolMaxLifeTime());
		}
		if (properties.getPoolPendingAcquireMaxCount() != -1) {
			connectionBuilder.pendingAcquireMaxCount(properties.getPoolPendingAcquireMaxCount());
		}
		if (properties.getPoolObtainsConnectionTimout() != null) {
			connectionBuilder.pendingAcquireTimeout(properties.getPoolObtainsConnectionTimout());
		}
		if (properties.getPoolEvictionInterval() != null) {
			connectionBuilder.evictInBackground(properties.getPoolEvictionInterval());
		}
		Http2AllocationStrategy strategy = this.allocationStrategy(properties.getPoolMinActiveConnects(),
			properties.getPoolMaxActiveConnects());
		if (strategy != null) {
			connectionBuilder.allocationStrategy(strategy);
		}
	}

	private Http2AllocationStrategy allocationStrategy(int minConnections, int maxConnections) {
		if (minConnections < 0 && maxConnections < 0) {
			return null;
		}
		Http2AllocationStrategy.Builder builder = Http2AllocationStrategy.builder();
		if (minConnections > 0) {
			builder.minConnections(minConnections);
		}
		if (maxConnections > 0) {
			builder.maxConnections(maxConnections);
		}

		return builder.build();
	}

}
