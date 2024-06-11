package com.arthur.plugin.ssl.filter;

import com.arthur.common.invoke.Lookups;
import io.netty.channel.ChannelOption;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.cloud.gateway.config.HttpClientProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.NettyRoutingFilter;
import org.springframework.cloud.gateway.filter.headers.HttpHeadersFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.arthur.common.invoke.LambdaMetafactorys.createLambda;
import static com.arthur.plugin.ssl.constant.SslConstants.HTTPS_PROTOCOL_ATTR;
import static com.arthur.plugin.ssl.constant.SslConstants.ROUTE_HTTPCLIENT_ATTR;
import static com.arthur.plugin.util.GatewayFilterFormatter.obtainsGlobalFilterEnabledKey;
import static java.lang.invoke.MethodType.methodType;
import static org.springframework.cloud.gateway.support.RouteMetadataUtils.CONNECT_TIMEOUT_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;

/**
 * 适配{@link SslGatewayFilterFactory}设置的{@link HttpClient}
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-03-21
 * @since 1.0
 */
public class SslNettyRoutingFilter extends NettyRoutingFilter implements EnvironmentPostProcessor {

	private final Function<Object, Integer> methodInvoker;

	public SslNettyRoutingFilter(HttpClient httpClient,
			ObjectProvider<List<HttpHeadersFilter>> headersFiltersProvider,
			HttpClientProperties properties) throws Throwable {
		super(httpClient, headersFiltersProvider, properties);
		this.methodInvoker = this.obtainFunctionHandle();
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		if (Boolean.TRUE.equals(exchange.getAttribute(HTTPS_PROTOCOL_ATTR))) {
			URI requestUrl = exchange.getRequiredAttribute(GATEWAY_REQUEST_URL_ATTR);
			exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, requestUrl);
		}
		return super.filter(exchange, chain);
	}

	@Override
	protected HttpClient getHttpClient(Route route, ServerWebExchange exchange) {
		HttpClient sslHttpClient = (HttpClient) exchange.getAttributes().remove(ROUTE_HTTPCLIENT_ATTR);
		if (sslHttpClient == null) {
			return super.getHttpClient(route, exchange);
		}

		// copy by getHttpClient
		Object connectTimeoutAttr = route.getMetadata().get(CONNECT_TIMEOUT_ATTR);
		if (connectTimeoutAttr != null) {
			Integer connectTimeout = methodInvoker.apply(connectTimeoutAttr);
			return sslHttpClient.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeout);
		}
		return sslHttpClient;
	}

	@Override
	public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
		String propertyName = obtainsGlobalFilterEnabledKey(NettyRoutingFilter.class);
		environment.getPropertySources().addFirst(new MapPropertySource(this.getClass().getSimpleName(),
			Map.of(propertyName, Boolean.FALSE.toString())
		));
	}

	@SuppressWarnings("unchecked")
	private Function<Object, Integer> obtainFunctionHandle() throws Throwable {
		MethodHandles.Lookup lookup = Lookups.trustedLookup(NettyRoutingFilter.class);
		return (Function<Object, Integer>) createLambda(Function.class, lookup, lookup.findStatic(
			NettyRoutingFilter.class, "getInteger", methodType(Integer.class, Object.class)))
			.getTarget()
			.invokeExact();
	}

}
