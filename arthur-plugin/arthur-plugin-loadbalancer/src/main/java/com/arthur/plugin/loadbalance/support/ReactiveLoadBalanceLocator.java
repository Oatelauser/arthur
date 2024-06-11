package com.arthur.plugin.loadbalance.support;

import com.arthur.plugin.loadbalance.core.NamedLoadBalancer;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Reactor实现的负载均衡注册器
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-01-03
 * @since 1.0
 */
public class ReactiveLoadBalanceLocator<R> implements LoadBalanceLocator<ReactorLoadBalancer<R>> {

    private final Map<String, ReactorLoadBalancer<R>> lbs;

    public ReactiveLoadBalanceLocator(ObjectProvider<List<ReactorLoadBalancer<R>>> provider) {
		List<ReactorLoadBalancer<R>> loadBalancers = provider.getIfAvailable(Collections::emptyList);
		this.lbs = this.getLoadBalancers(loadBalancers);
    }

    @Override
    public ReactorLoadBalancer<R> getLoadBalancer(String lbName) {
        return this.lbs.get(lbName);
    }

	private Map<String, ReactorLoadBalancer<R>> getLoadBalancers(
		List<ReactorLoadBalancer<R>> loadBalancers) {
		Map<String, ReactorLoadBalancer<R>> lbs = new HashMap<>(loadBalancers.size());
		for (ReactorLoadBalancer<R> lb : loadBalancers) {
            String lbName = NamedLoadBalancer.resolveName(lb.getClass());
			if (lb instanceof NamedLoadBalancer loadBalancer) {
				if (!loadBalancer.supportsRegistry()) {
					continue;
				}
				lbName = loadBalancer.name();
			}
			lbs.put(lbName, lb);
        }

		return lbs;
    }

}
