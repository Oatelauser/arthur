package com.arthur.plugin.loadbalance.support;

import com.arthur.common.lifecycle.InitializeListener;
import com.arthur.plugin.loadbalance.autoconfigure.LoadBalancerProperties;
import com.arthur.plugin.loadbalance.context.LoadBalancerProcessor;
import io.netty.channel.ChannelOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.loadbalancer.reactive.DeferringLoadBalancerExchangeFilterFunction;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.net.URI;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.arthur.plugin.loadbalance.constant.LoadBalancerConstants.LB_URL_ATTR_NAME;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.containsEncodedParts;


/**
 * 负载均衡初始化预热器启动器，防止懒加载带来的请求抖动
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-01-05
 * @see org.springframework.cloud.gateway.filter.RouteToRequestUrlFilter
 * @since 1.0
 */
@SuppressWarnings("rawtypes")
public class LoadBalanceContextStarter implements InitializeListener {

	private static final Logger LOG = LoggerFactory.getLogger(LoadBalanceContextStarter.class);
	private static final Pattern SCHEME_PATTERN = Pattern.compile("[a-zA-Z]([a-zA-Z]|\\d|\\+|\\.|-)*:.*");

	private final Set<String> lbSchemes;
	private final GatewayProperties properties;
	private final LoadBalancerProperties lbProperties;
	private final DeferringLoadBalancerExchangeFilterFunction exchangeFilterFunction;

	public LoadBalanceContextStarter(GatewayProperties properties,
			 LoadBalancerProperties lbProperties,
			 List<LoadBalancerProcessor> lbHandlers,
			 DeferringLoadBalancerExchangeFilterFunction exchangeFilterFunction) {
		this.properties = properties;
		this.lbProperties = lbProperties;
		this.lbSchemes = lbHandlers.stream().map(LoadBalancerProcessor::name)
			.collect(Collectors.toSet());
		this.exchangeFilterFunction = exchangeFilterFunction;
	}

	@Override
	public void start() {
		List<RouteDefinition> routes = this.properties.getRoutes();
		if (CollectionUtils.isEmpty(routes)) {
			return;
		}

		Set<String> serviceIds = new HashSet<>();
		routes.forEach(route -> this.collectServiceId(route, serviceIds));
		if (CollectionUtils.isEmpty(serviceIds)) {
			return;
		}

		this.initializeClientContext(serviceIds);
	}

	/**
	 * 基于{@link Flux}并发请求URL初始化LB上下文
	 *
	 * @param serviceIds 服务ID集合
	 */
	private void initializeClientContext(Set<String> serviceIds) {
		// 设置请求超时时间和连接超时时间，防止一直阻塞
		HttpClient httpClient = HttpClient.create()
			.responseTimeout(Duration.ofMillis(500))
			.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 200);
		WebClient.Builder webClientBuilder = WebClient.builder().filter(exchangeFilterFunction)
			.clientConnector(new ReactorClientHttpConnector(httpClient));

		// 并发执行
		Flux.fromIterable(serviceIds)
			.flatMap(serviceId -> this.sendInitializationRequest(serviceId, webClientBuilder.clone()))
			.doOnError(error -> {})
			.blockLast();
	}

	/**
	 * 收集路由中的服务ID，部分代码参考{@code RouteToRequestUrlFilter#filter}
	 *
	 * @param route 路由定义
	 * @see org.springframework.cloud.gateway.filter.RouteToRequestUrlFilter#filter(ServerWebExchange, GatewayFilterChain)
	 */
	private void collectServiceId(RouteDefinition route, Set<String> serviceIds) {
		URI routeUri = route.getUri();
		boolean encodedParts = containsEncodedParts(routeUri);
		if (hasAnotherScheme(routeUri)) {
			routeUri = URI.create(routeUri.getSchemeSpecificPart());
		}

		String scheme = routeUri.getScheme();
		if ((lbSchemes.contains(scheme) || lbSchemes.contains(scheme.toUpperCase()))
			&& routeUri.getHost() == null) {
			return;
		}

		URI mergedUrl = UriComponentsBuilder.fromUri(routeUri).scheme(scheme)
			.host(routeUri.getHost())
			.port(routeUri.getPort())
			.build(encodedParts)
			.toUri();

		String serviceId = mergedUrl.getHost();
		if (StringUtils.hasText(serviceId)) {
			serviceIds.add(serviceId);
		}
	}

	protected Mono<Void> sendInitializationRequest(String serviceId, WebClient.Builder webClientBuilder) {
		String lbInitializeUrl = lbProperties.getLbInitializeUrls()
			.getOrDefault(serviceId, lbProperties.getDefaultLbInitializeUrl());
		return webClientBuilder.build().get().uri(lbInitializeUrl, Map.of(LB_URL_ATTR_NAME, serviceId))
			.retrieve()
			.bodyToMono(Void.class)
			.doOnSuccess(response -> {
				if (LOG.isDebugEnabled()) {
					LOG.debug("Initialize the loading service [{}], request address [{}]", serviceId, lbInitializeUrl);
				}
			})
			.doOnError(t -> {
				if (LOG.isDebugEnabled()) {
					LOG.debug("Initialize the loading service [" + serviceId + "] error", t);
				}
			});
	}

	/**
	 * 参考{@code org.springframework.cloud.gateway.filter.RouteToRequestUrlFilter#hasAnotherScheme(URI)}
	 */
	static boolean hasAnotherScheme(URI uri) {
		return SCHEME_PATTERN.matcher(uri.getSchemeSpecificPart()).matches()
			&& uri.getHost() == null
			&& uri.getRawPath() == null;
	}

}
