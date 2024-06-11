package com.arthur.plugin.loadbalance.context;

import com.arthur.plugin.loadbalance.transformer.LoadBalancerDataTransformer;
import org.springframework.cloud.client.loadbalancer.RequestData;
import org.springframework.cloud.client.loadbalancer.RequestDataContext;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;

/**
 * 拓展负载均衡请求数据下文
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2022-12-27
 * @see RequestDataContext
 * @since 1.0
 */
public class ServiceRequestContext<T> extends RequestDataContext implements LbRequestContext<T> {

	/**
	 * 负载均衡算法
	 */
	private ReactorLoadBalancer<T> lb;

	/**
	 * 负载均衡数据上下文转换器
	 */
	private LoadBalancerDataTransformer<T> transformer;

	public ServiceRequestContext(RequestData requestData) {
		super(requestData);
	}

	public ServiceRequestContext(String hint, RequestData requestData) {
		super(requestData, hint);
	}

	@Override
	public ReactorLoadBalancer<T> getLb() {
		return lb;
	}

	@Override
	public void setLb(ReactorLoadBalancer<T> lb) {
		this.lb = lb;
	}

	@Override
	public void setTransformer(LoadBalancerDataTransformer<T> transformer) {
		this.transformer = transformer;
	}

	@Override
	public LoadBalancerDataTransformer<T> getTransformer() {
		return transformer;
	}

}
