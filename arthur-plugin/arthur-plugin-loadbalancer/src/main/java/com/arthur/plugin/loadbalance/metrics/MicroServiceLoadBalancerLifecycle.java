package com.arthur.plugin.loadbalance.metrics;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.flow.PriorityWaitException;
import com.alibaba.csp.sentinel.util.TimeUtil;
import com.arthur.plugin.loadbalance.context.ServiceRequestContext;
import com.arthur.plugin.loadbalance.filter.AbstractReactiveLoadBalancerFilter;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.*;

import static com.alibaba.csp.sentinel.ResourceTypeConstants.COMMON_API_GATEWAY;

/**
 * 服务实例的负载均衡数据指标统计
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2022-12-27
 * @see AbstractReactiveLoadBalancerFilter
 * @see com.alibaba.csp.sentinel.slots.statistic.StatisticSlot
 * @since 1.0
 */
public class MicroServiceLoadBalancerLifecycle extends ServiceInstanceMetricsAdapter<ClusterNodeAdapter>
	implements LoadBalancerLifecycle<ServiceRequestContext<ServiceInstance>, ResponseData, ServiceInstance> {

	@Override
	public boolean supports(Class requestContextClass, Class responseClass, Class serverTypeClass) {
		return responseClass == ResponseData.class
			&& serverTypeClass == ServiceInstance.class
			&& requestContextClass == ServiceRequestContext.class;
	}

	@Override
	public void onStart(Request<ServiceRequestContext<ServiceInstance>> request) {
	}

	@Override
	public void onStartRequest(Request<ServiceRequestContext<ServiceInstance>> request, Response<ServiceInstance> lbResponse) {
		ServiceRequestContext<ServiceInstance> dataContext = request.getContext();
		dataContext.setRequestStartTime(TimeUtil.currentTimeMillis());
		if (!lbResponse.hasServer()) {
			return;
		}

		ServiceInstance serviceInstance = lbResponse.getServer();
		String instanceKey = this.getInstanceKey(serviceInstance);
		ClusterNodeAdapter statisticNode = this.computeIfAbsent(instanceKey,
			key -> new ClusterNodeAdapter(instanceKey, COMMON_API_GATEWAY));
		statisticNode.lock = true;

		statisticNode.increaseThreadNum();
		statisticNode.addPassRequest(1);
	}

	@Override
	@SuppressWarnings("ConstantConditions")
	public void onComplete(CompletionContext<ResponseData, ServiceInstance, ServiceRequestContext<ServiceInstance>> completionContext) {
		// 没有服务实例，直接返回
		CompletionContext.Status status = completionContext.status();
		if (CompletionContext.Status.DISCARD.equals(status)) {
			return;
		}
		// 没有负载均衡返回的服务实例响应，直接返回
		Response<ServiceInstance> lbResponse = completionContext.getLoadBalancerResponse();
		if (CompletionContext.Status.FAILED.equals(status) && lbResponse == null) {
			return;
		}

		Request<ServiceRequestContext<ServiceInstance>> request = completionContext.getLoadBalancerRequest();
		ServiceRequestContext<ServiceInstance> dataContext = request.getContext();
		ServiceInstance serviceInstance = lbResponse.getServer();

		ClusterNodeAdapter statisticNode = this.getMetrics(serviceInstance);
		long currentTimeMillis = TimeUtil.currentTimeMillis();
		statisticNode.lastUpdateTime = currentTimeMillis;
		statisticNode.lock = false;

		long rt = currentTimeMillis - dataContext.getRequestStartTime();
		if (CompletionContext.Status.FAILED.equals(status)) {
			Throwable throwable = completionContext.getThrowable();
			if (throwable instanceof BlockException) {
				statisticNode.increaseBlockQps(1);
				return;
			}

			if (throwable instanceof PriorityWaitException) {
				statisticNode.increaseThreadNum();
			} else {
				statisticNode.increaseExceptionQps(1);
			}
		}

		statisticNode.addRtAndSuccess(rt, 1);
		statisticNode.decreaseThreadNum();
	}

}
