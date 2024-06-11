package com.arthur.plugin.loadbalance.utils;

import org.springframework.cloud.client.ServiceInstance;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static com.arthur.common.constant.BaseConstants.COLON;

/**
 * {@link ServiceInstance}工具类
 *
 * @author DearYang
 * @date 2022-08-05
 * @since 1.0
 */
public class ServiceInstanceUtils {

    /**
     * 获取服务实例的key
     *
     * @param serviceInstance {@link ServiceInstance}
     * @return key
     */
    public static String getServiceInstanceKey(ServiceInstance serviceInstance) {
        return serviceInstance.getHost() + COLON + serviceInstance.getPort();
    }

	/**
	 * 获取服务实例列表的哈希值总值
	 *
	 * @param serviceInstances 服务实例列表
	 * @return 哈希值总值
	 */
    public static int getAllServiceHashCode(List<ServiceInstance> serviceInstances) {
        return serviceInstances.stream().map(ServiceInstanceUtils::getServiceInstanceKey)
                .mapToInt(System::identityHashCode)
                .sum();
    }

    /**
     * 随机选择服务实例
     *
     * @param serviceInstances 服务实例列表
     * @return 选择的服务实例
     */
    public static ServiceInstance random(List<ServiceInstance> serviceInstances) {
        return serviceInstances.get(ThreadLocalRandom.current().nextInt(serviceInstances.size()));
    }

}
