package com.arthur.plugin.loadbalance.context;

import com.arthur.plugin.loadbalance.transformer.LoadBalancerDataTransformer;
import org.springframework.cloud.client.loadbalancer.TimedRequestContext;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;

/**
 * 具有负载均衡算法对象的请求上下文
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-01-05
 * @since 1.0
 */
public interface LbRequestContext<T> extends TimedRequestContext {

	/**
	 * 设置负载均衡算法对象
	 *
	 * @param lb {@link ReactorLoadBalancer}
	 */
	void setLb(ReactorLoadBalancer<T> lb);

	/**
	 * 获取负载均衡对象
	 *
	 * @return {@link ReactorLoadBalancer}
	 */
	ReactorLoadBalancer<T> getLb();

	/**
	 * 设置负载均衡数据转换器
	 *
	 * @param transformer 转换器
	 */
	void setTransformer(LoadBalancerDataTransformer<T> transformer);

	/**
	 * 获取负载均衡数据转换器
	 *
	 * @return 转换器
	 */
	LoadBalancerDataTransformer<T> getTransformer();

}
