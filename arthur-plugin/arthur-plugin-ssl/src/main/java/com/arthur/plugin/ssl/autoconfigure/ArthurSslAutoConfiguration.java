package com.arthur.plugin.ssl.autoconfigure;

import com.arthur.plugin.ssl.filter.SslGatewayFilterFactory;
import com.arthur.plugin.ssl.filter.SslNettyRoutingFilter;
import com.arthur.plugin.ssl.support.NettyHttpClientFactory;
import com.arthur.web.reactive.server.WebServerResponse;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.cloud.gateway.config.*;
import org.springframework.cloud.gateway.config.conditional.ConditionalOnEnabledFilter;
import org.springframework.cloud.gateway.config.conditional.ConditionalOnEnabledGlobalFilter;
import org.springframework.cloud.gateway.filter.NettyWriteResponseFilter;
import org.springframework.cloud.gateway.filter.headers.HttpHeadersFilter;
import org.springframework.context.annotation.Bean;
import reactor.netty.http.client.HttpClient;

import java.util.List;

/**
 * SSL/TSL自动配置类
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-03-21
 * @since 1.0
 */
@AutoConfiguration(beforeName = "org.springframework.cloud.gateway.config.GatewayAutoConfiguration")
public class ArthurSslAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean({ HttpClient.class, HttpClientFactory.class })
	public NettyHttpClientFactory nettyHttpClientFactory(HttpClientProperties properties,
			ServerProperties serverProperties, List<HttpClientCustomizer> customizers,
			HttpClientSslConfigurer sslConfigurer) {
		return new NettyHttpClientFactory(properties, serverProperties, sslConfigurer, customizers);
	}

	@Bean
	@ConditionalOnEnabledFilter
	public SslGatewayFilterFactory sslGatewayFilterFactory(NettyHttpClientFactory httpClientFactory,
			WebServerResponse serverResponse) {
		return new SslGatewayFilterFactory(httpClientFactory, serverResponse);
	}

	@Bean
	@ConditionalOnEnabledGlobalFilter
	public SslNettyRoutingFilter routingFilter(HttpClient httpClient,
			ObjectProvider<List<HttpHeadersFilter>> headersFilters,
			HttpClientProperties properties) throws Throwable {
		return new SslNettyRoutingFilter(httpClient, headersFilters, properties);
	}

	@Bean
	@ConditionalOnEnabledGlobalFilter(SslNettyRoutingFilter.class)
	public NettyWriteResponseFilter nettyWriteResponseFilter(GatewayProperties properties) {
		return new NettyWriteResponseFilter(properties.getStreamingMediaTypes());
	}

}
