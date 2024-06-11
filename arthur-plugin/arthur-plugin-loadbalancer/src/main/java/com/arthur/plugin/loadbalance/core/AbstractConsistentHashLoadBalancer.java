package com.arthur.plugin.loadbalance.core;

import com.arthur.common.utils.NumberUtils;
import com.arthur.plugin.loadbalance.constant.LoadBalancerConstants;
import com.arthur.plugin.loadbalance.utils.ServiceInstanceUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 抽象一致性Hash负载均衡策略
 *
 * @author DearYang
 * @date 2022-08-05
 * @since 1.0
 */
@SuppressWarnings({ "rawtypes", "unused" })
public abstract class AbstractConsistentHashLoadBalancer extends AbstractServiceInstanceLoadBalancer {

	static final MessageDigest MD5;

	static {
		try {
			MD5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * 虚拟节点数
	 */
	private int virtualNodeNumber = LoadBalancerConstants.VIRTUAL_NODE_NUMBER;
	private volatile ConsistentHashSelector selector;

	public AbstractConsistentHashLoadBalancer(String serviceId,
			ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider) {
		super(serviceId, serviceInstanceListSupplierProvider);
	}

	@Override
	protected ServiceInstance selectInstance(Request request, List<ServiceInstance> instances) {
		String serviceId = this.getServiceId();
		Map<String, ServiceInstance> instanceKeys = instances.stream()
			.collect(Collectors.toMap(ServiceInstanceUtils::getServiceInstanceKey, Function.identity()));

		int identityHashCode = 0;
		for (String instanceKey : instanceKeys.keySet()) {
			identityHashCode += instanceKey.hashCode();
		}
		if (identityHashCode < 0) {
			identityHashCode = NumberUtils.abs(identityHashCode);
		}

		if (selector == null || identityHashCode != selector.identityHashCode) {
			selector = new ConsistentHashSelector(virtualNodeNumber, identityHashCode, instanceKeys);
		}

		String hashValue = this.getHashValue(request);
		return StringUtils.hasText(hashValue) ? selector.select(hashValue) :
				ServiceInstanceUtils.random(instances);
	}

	/**
	 * 获取需要哈希计算的字符串
	 *
	 * @param request 请求对象
	 */
	protected abstract String getHashValue(Request request);

	public int getVirtualNodeNumber() {
		return virtualNodeNumber;
	}

	public void setVirtualNodeNumber(int virtualNodeNumber) {
		this.virtualNodeNumber = virtualNodeNumber;
	}

	private static final class ConsistentHashSelector {

		private final long identityHashCode;
		private final NavigableMap<Long, ServiceInstance> virtualNodes;

		private ConsistentHashSelector(int replicaNumber, long identityHashCode,
			Map<String, ServiceInstance> instanceKeys) {
			this.virtualNodes = new TreeMap<>();
			this.identityHashCode = identityHashCode;
			for (Map.Entry<String, ServiceInstance> entry : instanceKeys.entrySet()) {
				for (int i = 0; i < replicaNumber / 4; i++) {
					byte[] digest = MD5.digest((entry.getKey() + i).getBytes());
					for (int j = 0; j < 4; j++) {
						long hash = this.hash(digest, j);
						virtualNodes.put(hash, entry.getValue());
					}
				}
			}
		}

		public ServiceInstance select(String hashValue) {
			byte[] digest = MD5.digest(hashValue.getBytes(StandardCharsets.UTF_8));
			return this.select(this.hash(digest, 0));
		}

		public ServiceInstance select(long hash) {
			Map.Entry<Long, ServiceInstance> entry = virtualNodes.ceilingEntry(hash);
			if (entry == null) {
				entry = virtualNodes.firstEntry();
			}
			return entry.getValue();
		}

		private long hash(byte[] digest, int number) {
			return (((long) (digest[3 + number * 4] & 0xFF) << 24)
					| ((long) (digest[2 + number * 4] & 0xFF) << 16)
					| ((long) (digest[1 + number * 4] & 0xFF) << 8)
					| (digest[number * 4] & 0xFF))
					& 0xFFFFFFFFL;
		}

	}

}
