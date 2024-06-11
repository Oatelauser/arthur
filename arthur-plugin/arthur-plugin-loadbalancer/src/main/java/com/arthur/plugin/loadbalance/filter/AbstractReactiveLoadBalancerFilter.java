package com.arthur.plugin.loadbalance.filter;

import com.arthur.boot.utils.BeanUtils;
import com.arthur.plugin.loadbalance.constant.LoadBalancerConstants;
import com.arthur.plugin.loadbalance.context.LbRequestContext;
import com.arthur.plugin.loadbalance.context.LoadBalancerProcessor;
import com.arthur.plugin.loadbalance.core.NoopLoadBalancer;
import com.arthur.plugin.loadbalance.support.LoadBalanceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.loadbalancer.*;
import org.springframework.cloud.gateway.config.GatewayLoadBalancerProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.ReactiveLoadBalancerClientFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.core.ResolvableType;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.*;

/**
 * 抽象通用的负载均衡过滤器
 * <p>
 * 抽象{@link ReactiveLoadBalancerClientFilter}中映射规则，单独提出来
 *
 * @param <T> 响应内容泛型对象
 * @param <C> 请求对象{@link Request}的内容泛型
 * @param <R> 响应对象{@link Response}的内容泛型
 * @author DearYang
 * @date 2022-09-19
 * @since 1.0
 */
@SuppressWarnings({"unused", "rawtypes", "unchecked"})
public abstract class AbstractReactiveLoadBalancerFilter<T, C extends LbRequestContext<R>, R> extends ReactiveLoadBalancerClientFilter {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractReactiveLoadBalancerFilter.class);
	private final LoadBalancerClientFactory clientFactory;
	private final GatewayLoadBalancerProperties properties;
	private final Map<String, LoadBalancerProcessor<C, R>> requestHandlers;
	private Class<T> t;
	private Class<C> c;
	private Class<R> r;
	private ResolvableType registrarType;
	private volatile ReactorLoadBalancer<R> defaultLoadBalancer;

	public AbstractReactiveLoadBalancerFilter(GatewayLoadBalancerProperties properties,
			  LoadBalancerClientFactory clientFactory,
			  ObjectProvider<List<LoadBalancerProcessor<C, R>>> provider) {
		super(clientFactory, properties);
		this.properties = properties;
		this.clientFactory = clientFactory;
		this.requestHandlers = this.loadLoadBalancerProcessor(provider);
		this.resolveSuperGenerics();
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		URI url = exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR);
		String schemePrefix = exchange.getAttribute(GATEWAY_SCHEME_PREFIX_ATTR);

		LoadBalancerProcessor handler;
		if (url == null || (handler = getSupportedLoadBalancerProcessor(url.getScheme(), schemePrefix)) == null) {
			return chain.filter(exchange);
		}
		// preserve the original url
		addOriginalRequestUrl(exchange, url);

		if (LOG.isTraceEnabled()) {
			LOG.trace(ReactiveLoadBalancerClientFilter.class.getSimpleName() + " url before: " + url);
		}

		URI requestUri = exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR);
		String serviceId = Objects.requireNonNull(requestUri, GATEWAY_REQUEST_URL_ATTR).getHost();
		Set<LoadBalancerLifecycle> supportedLifecycleProcessors = LoadBalancerLifecycleValidator
			.getSupportedLifecycleProcessors(clientFactory.getInstances(serviceId, LoadBalancerLifecycle.class), c, t, r);

		// 网关路由
		Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
		if (route == null) {
			throw new IllegalArgumentException("ServerWebExchange cannot get attribute ["
                    + ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR + "]");
		}

		Request<C> lbRequest = handler.createRequest(serviceId, route, exchange, this.getLoadBalancer(serviceId, route));
		return choose(lbRequest, serviceId, supportedLifecycleProcessors).doOnNext(response -> {
			if (!response.hasServer()) {
				supportedLifecycleProcessors.forEach(lifecycle -> lifecycle
					.onComplete(new CompletionContext<>(CompletionContext.Status.DISCARD, lbRequest, response)));
				throw NotFoundException.create(properties.isUse404(), "Unable to find instance for " + url.getHost());
			}

			this.handleResponse(url, exchange, response);
			supportedLifecycleProcessors.forEach(lifecycle -> lifecycle.onStartRequest(lbRequest, response));
		}).then(chain.filter(exchange))
			.doOnError(throwable -> supportedLifecycleProcessors.forEach(lifecycle ->
				lifecycle.onComplete(new CompletionContext<T, R, C>(CompletionContext.Status.FAILED, throwable, lbRequest,
					exchange.getAttribute(GATEWAY_LOADBALANCER_RESPONSE_ATTR)))))
			.doOnSuccess(aVoid -> supportedLifecycleProcessors.forEach(lifecycle ->
				lifecycle.onComplete(new CompletionContext<T, R, C>(CompletionContext.Status.SUCCESS, lbRequest,
					exchange.getAttribute(GATEWAY_LOADBALANCER_RESPONSE_ATTR),
					buildResponseData(exchange)))));
	}

	protected Mono<Response<R>> choose(Request<C> lbRequest, String serviceId,
		   Set<LoadBalancerLifecycle> supportedLifecycleProcessors) {
		ReactorLoadBalancer<R> loadBalancer = lbRequest.getContext().getLb();
		supportedLifecycleProcessors.forEach(lifecycle -> lifecycle.onStart(lbRequest));
		return loadBalancer.choose(lbRequest);
	}

	private LoadBalancerProcessor getSupportedLoadBalancerProcessor(String scheme, String schemePrefix) {
		LoadBalancerProcessor lbProcessor = requestHandlers.get(scheme);
		return lbProcessor != null ? lbProcessor : requestHandlers.get(schemePrefix);
	}

	private Map<String, LoadBalancerProcessor<C, R>> loadLoadBalancerProcessor(
            ObjectProvider<List<LoadBalancerProcessor<C, R>>> provider) {
		if (provider == null) {
			return Collections.emptyMap();
		}
		List<LoadBalancerProcessor<C, R>> loadBalancerProcessors = provider.getIfAvailable();
		if (loadBalancerProcessors == null) {
			return Collections.emptyMap();
		}

		return BeanUtils.sort(loadBalancerProcessors).stream()
			.collect(Collectors.toMap(LoadBalancerProcessor::name, Function.identity()));
	}

	/**
	 * 获取负载均衡算法对象
	 *
	 * @param serviceId 服务ID
	 * @param route     路由对象
	 * @return 负载均衡对象
	 */
	protected ReactorLoadBalancer<R> getLoadBalancer(String serviceId, Route route) {
		// 负载均衡定位器
		ReactorLoadBalancer<R> loadBalancer = this.clientFactory.getInstance(serviceId, ReactorLoadBalancer.class, r);
		if (loadBalancer == null) {
			throw new NotFoundException("No loadbalancer available for " + serviceId);
		}
		// 判断是否是主动设置负载均衡算法
		if (!(loadBalancer instanceof NoopLoadBalancer)) {
			return loadBalancer;
		}

		// 新增负载均衡算法选择
		Object lbName;
		Map<String, Object> metadata = route.getMetadata();
		if (CollectionUtils.isEmpty(metadata) || (lbName = metadata.get(LoadBalancerConstants.LB_ATTR)) == null) {
			return this.getDefaultLoadBalancer(serviceId);
		}

		// 负载均衡算法定位器
		LoadBalanceLocator<ReactorLoadBalancer<R>> lbLocator = this.clientFactory.getInstance(serviceId, this.registrarType);
		if (lbLocator == null) {
			return this.getDefaultLoadBalancer(serviceId);
		}

		loadBalancer = lbLocator.getLoadBalancer(lbName.toString());
		return loadBalancer == null ? this.getDefaultLoadBalancer(serviceId) : loadBalancer;
	}

	private ReactorLoadBalancer<R> getDefaultLoadBalancer(String serviceId) {
		if (this.defaultLoadBalancer != null) {
			return this.defaultLoadBalancer;
		}

		this.defaultLoadBalancer = (ReactorLoadBalancer<R>) clientFactory.getInstance(serviceId);
		return Optional.ofNullable(this.defaultLoadBalancer).orElseThrow(() ->
				new NotFoundException("No loadbalancer available for " + serviceId));
	}

	public LoadBalancerClientFactory getClientFactory() {
		return clientFactory;
	}

	public GatewayLoadBalancerProperties getProperties() {
		return properties;
	}

	/**
	 * 解析类上泛型T、C、R
	 */
	private void resolveSuperGenerics() {
		ResolvableType resolvableType = ResolvableType.forClass(AbstractReactiveLoadBalancerFilter.class, this.getClass());
		Class<?>[] superGenericClasses = resolvableType.resolveGenerics();
		Assert.state(superGenericClasses.length == 3, "Can not resolve super class " +
				"generics type for [" + this.getClass().getSimpleName() + "] class");

		this.t = (Class<T>) superGenericClasses[0];
		this.c = (Class<C>) superGenericClasses[1];
		this.r = (Class<R>) superGenericClasses[2];
		this.registrarType = ResolvableType.forClassWithGenerics(LoadBalanceLocator.class,
				ResolvableType.forClassWithGenerics(ReactorLoadBalancer.class, r));
	}

	/**
	 * 处理请求对象
	 *
	 * @param url      url
	 * @param exchange {@link ServerWebExchange}
	 * @param response {@link Response}
	 */
	protected abstract void handleResponse(URI url, ServerWebExchange exchange, Response<R> response);

	/**
	 * 构建响应数据
	 *
	 * @param exchange {@link ServerWebExchange}
	 * @return 响应数据
	 */
	protected abstract T buildResponseData(ServerWebExchange exchange);

}
