package com.arthur.plugin.loadbalance.core;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import reactor.core.publisher.Mono;

/**
 * 负载均衡算法的空实现，主要是用于区分是否主动设置负载均衡算法
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-01-04
 * @since 1.0
 */
public class NoopLoadBalancer implements NamedLoadBalancer, ReactorServiceInstanceLoadBalancer {

	@Override
	public Mono<Response<ServiceInstance>> choose(Request request) {
		throw new UnsupportedOperationException("Unsupported choose method");
	}

	@Override
	public boolean supportsRegistry() {
		return false;
	}

	public static ReactorLoadBalancer<ServiceInstance> getInstance() {
		return Holder.NOOP;
	}

	static class Holder {
		private static final ReactorLoadBalancer<ServiceInstance> NOOP = new NoopLoadBalancer();
	}

}
