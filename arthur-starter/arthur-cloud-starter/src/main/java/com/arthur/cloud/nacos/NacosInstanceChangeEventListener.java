package com.arthur.cloud.nacos;

import com.alibaba.nacos.client.naming.event.InstancesChangeEvent;
import com.alibaba.nacos.common.notify.Event;
import com.alibaba.nacos.common.notify.listener.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cloud.loadbalancer.cache.LoadBalancerCacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import static org.springframework.cloud.loadbalancer.core.CachingServiceInstanceListSupplier.SERVICE_INSTANCE_CACHE_NAME;

/**
 * 基于Nacos事件通知模型，实现服务无损下线
 *
 * @author DearYang
 * @date 2022-10-20
 * @since 1.0
 */
@SuppressWarnings("SpellCheckingInspection")
public class NacosInstanceChangeEventListener extends Subscriber<InstancesChangeEvent> implements ApplicationContextAware {

	private static final Logger LOG = LoggerFactory.getLogger(NacosInstanceChangeEventListener.class);

	private CacheManager loadBalancerCacheManager;

	@Override
	public void onEvent(InstancesChangeEvent event) {
		Cache serviceInstances = loadBalancerCacheManager.getCache(SERVICE_INSTANCE_CACHE_NAME);
		if (serviceInstances == null) {
			return;
		}

		serviceInstances.evict(event.getServiceName());
		if (LOG.isDebugEnabled()) {
			LOG.debug("Arthur Gateway complete service instance [{}] refresh", event.getServiceName());
		}
	}

	@Override
	public Class<? extends Event> subscribeType() {
		return InstancesChangeEvent.class;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.loadBalancerCacheManager = applicationContext.getBean(LoadBalancerCacheManager.class);
	}

}
