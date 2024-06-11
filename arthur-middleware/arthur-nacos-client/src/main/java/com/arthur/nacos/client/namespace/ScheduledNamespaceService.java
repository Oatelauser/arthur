package com.arthur.nacos.client.namespace;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.client.naming.remote.http.NamingHttpClientProxy;
import com.alibaba.nacos.client.security.SecurityProxy;
import com.alibaba.nacos.common.notify.NotifyCenter;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.alibaba.nacos.common.utils.ThreadUtils;
import com.arthur.nacos.client.constant.ChangeType;
import com.arthur.nacos.client.event.NamespacesChangeEvent;
import com.arthur.nacos.client.model.Namespace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 继承{@link NacosNamespaceService}新增缓存命名空间列表
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-02
 * @since 1.0
 */
@SuppressWarnings("unused")
public class ScheduledNamespaceService extends NacosNamespaceService {

	private static final Logger LOG = LoggerFactory.getLogger(ScheduledNamespaceService.class);

	private int initialDelay = 0;
	private long delayInSeconds = TimeUnit.MINUTES.toSeconds(1);
	private volatile List<Namespace> namespaces;
	private ScheduledExecutorService executorService;

	static {
		NotifyCenter.registerToPublisher(NamespacesChangeEvent.class, 16384);
	}

	public ScheduledNamespaceService(Properties properties, NamingHttpClientProxy httpClientProxy) throws NacosException {
		super(properties, httpClientProxy);
	}

	public ScheduledNamespaceService(Properties properties) throws NacosException {
		super(properties);
	}

	public ScheduledNamespaceService(boolean autoRefresh, Properties properties) throws NacosException {
		super(autoRefresh, properties);
	}

	public ScheduledNamespaceService(boolean autoRefresh, Properties properties, SecurityProxy securityProxy) throws NacosException {
		super(autoRefresh, properties, securityProxy);
	}

	@Override
	public List<Namespace> getAllNamespace() throws NacosException {
		if (namespaces != null) {
			return namespaces;
		}

		return super.getAllNamespace();
	}

	private void initNamespaceService() {
		if (executorService == null) {
			this.executorService = new ScheduledThreadPoolExecutor(1, r -> {
				Thread t = new Thread(r);
				t.setName("com.arthur.nacos.client.namespace.service");
				t.setDaemon(true);
				return t;
			});
		}
		this.executorService.scheduleWithFixedDelay(() -> {
			try {
				List<Namespace> namespaces = this.namespaces;
				this.namespaces = super.getAllNamespace();
				this.notifyChangeNamespace(namespaces, this.namespaces);
			} catch (Exception e) {
				LOG.error("load all namespace has error", e);
			}
		}, this.initialDelay, this.delayInSeconds, TimeUnit.SECONDS);
	}

	protected void notifyChangeNamespace(List<Namespace> target, List<Namespace> source) {
		if (CollectionUtils.isEmpty(target)) {
			return;
		}

		Set<Namespace> eventObject = new HashSet<>();
		Set<Namespace> sourceNamespace = new HashSet<>(source);
		Set<Namespace> targetNamespace = new HashSet<>(target);
		// 判断状态
		for (Namespace namespace : sourceNamespace) {
			if (!targetNamespace.remove(namespace)) {
				namespace.setChangeType(ChangeType.ADD);
				eventObject.add(namespace);
			}
		}
		targetNamespace.forEach(namespace -> namespace.setChangeType(ChangeType.DELETE));
		eventObject.addAll(targetNamespace);

		sourceNamespace.addAll(targetNamespace);
		Namespace[] namespaces = eventObject.toArray(new Namespace[0]);
		NamespacesChangeEvent changeEvent = new NamespacesChangeEvent(namespaces);
		NotifyCenter.publishEvent(changeEvent);
	}

	public int getInitialDelay() {
		return initialDelay;
	}

	public void setInitialDelay(int initialDelay) {
		this.initialDelay = initialDelay;
	}

	public long getDelayInSeconds() {
		return delayInSeconds;
	}

	public void setDelayInSeconds(long delayInSeconds) {
		this.delayInSeconds = delayInSeconds;
	}

	public ScheduledExecutorService getExecutorService() {
		return executorService;
	}

	public void setExecutorService(ScheduledExecutorService executorService) {
		this.executorService = executorService;
	}

	@Override
	public void shutdown() throws NacosException {
		super.shutdown();
		ThreadUtils.shutdownThreadPool(executorService);
	}

}
