package com.arthur.plugin.loadbalance.filter;

import com.arthur.plugin.loadbalance.context.LoadBalancerProcessor;
import com.arthur.plugin.loadbalance.context.ServiceRequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.RequestData;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.client.loadbalancer.ResponseData;
import org.springframework.cloud.gateway.config.GatewayLoadBalancerProperties;
import org.springframework.cloud.gateway.filter.ReactiveLoadBalancerClientFilter;
import org.springframework.cloud.gateway.support.DelegatingServiceInstance;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.web.server.ServerWebExchange;

import java.net.URI;
import java.util.List;
import java.util.Map;

import static com.arthur.plugin.util.GatewayFilterFormatter.obtainsGlobalFilterEnabledKey;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.*;

/**
 * 实现标准的网关负载均衡过滤器
 *
 * @author DearYang
 * @date 2022-09-22
 * @since 1.0
 */
public class GatewayLoadBalancerFilter extends AbstractReactiveLoadBalancerFilter<ResponseData,
		ServiceRequestContext<ServiceInstance>, ServiceInstance> implements EnvironmentPostProcessor {

	private static final Logger LOG = LoggerFactory.getLogger(GatewayLoadBalancerFilter.class);

	public GatewayLoadBalancerFilter(GatewayLoadBalancerProperties properties,
		   LoadBalancerClientFactory clientFactory,
		   ObjectProvider<List<LoadBalancerProcessor<ServiceRequestContext<ServiceInstance>, ServiceInstance>>> provider) {
		super(properties, clientFactory, provider);
	}

	@Override
	protected void handleResponse(URI url, ServerWebExchange exchange, Response<ServiceInstance> response) {
		ServiceInstance retrievedInstance = response.getServer();

		URI uri = exchange.getRequest().getURI();

		// if the `lb:<scheme>` mechanism was used, use `<scheme>` as the default,
		// if the loadbalancer doesn't provide one.
		String overrideScheme = retrievedInstance.isSecure() ? "https" : "http";
		String schemePrefix = exchange.getAttribute(GATEWAY_SCHEME_PREFIX_ATTR);
		if (schemePrefix != null) {
			overrideScheme = url.getScheme();
		}

		DelegatingServiceInstance serviceInstance = new DelegatingServiceInstance(retrievedInstance, overrideScheme);

		URI requestUrl = reconstructURI(serviceInstance, uri);

		if (LOG.isTraceEnabled()) {
			LOG.trace("LoadBalancerClientFilter url chosen: " + requestUrl);
		}
		exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, requestUrl);
		exchange.getAttributes().put(GATEWAY_LOADBALANCER_RESPONSE_ATTR, response);
	}

	@Override
	protected ResponseData buildResponseData(ServerWebExchange exchange) {
		//new ResponseData(exchange.getResponse(), new RequestData(exchange.getRequest()))
		return new ResponseData(exchange.getResponse(), new RequestData(exchange.getRequest()));
	}

	@Override
	public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
		String propertyName = obtainsGlobalFilterEnabledKey(ReactiveLoadBalancerClientFilter.class);
		environment.getPropertySources().addFirst(new MapPropertySource(this.getClass().getSimpleName(),
			Map.of(propertyName, Boolean.FALSE.toString())
		));
	}

}
