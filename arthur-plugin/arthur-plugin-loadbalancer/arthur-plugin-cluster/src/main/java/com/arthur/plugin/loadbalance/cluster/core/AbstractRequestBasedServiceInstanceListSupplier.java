package com.arthur.plugin.loadbalance.cluster.core;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.loadbalancer.core.DelegatingServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * 抽象请求的服务实例提供者
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-15
 * @since 1.0
 */
public abstract class AbstractRequestBasedServiceInstanceListSupplier<C> extends DelegatingServiceInstanceListSupplier {

	public AbstractRequestBasedServiceInstanceListSupplier(ServiceInstanceListSupplier delegate) {
		super(delegate);
	}

	@Override
	public Flux<List<ServiceInstance>> get() {
		return getDelegate().get();
	}

	@Override
	@SuppressWarnings("unchecked")
	public Flux<List<ServiceInstance>> get(Request request) {
		return getDelegate().get(request).map(selectInstances ->
			this.selectInstance((Request<C>) request, selectInstances));
	}

	/**
	 * 筛选服务实例
	 *
	 * @param serviceInstances 服务实例列表
	 * @return 过滤后的服务实例列表
	 */
	protected abstract List<ServiceInstance> selectInstance(Request<C> request,
		List<ServiceInstance> serviceInstances);

}
