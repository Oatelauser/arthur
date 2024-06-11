package com.arthur.cloud.nacos.discovery;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.NacosServiceManager;
import com.alibaba.cloud.nacos.registry.NacosRegistration;
import com.alibaba.cloud.nacos.registry.NacosServiceRegistry;
import com.alibaba.nacos.api.exception.NacosException;
import com.arthur.boot.utils.BeanCopierUtils;
import com.arthur.common.exception.ArthurException;
import com.arthur.common.lifecycle.SmartLifecycle;
import com.arthur.nacos.client.utils.NamespaceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static com.arthur.cloud.nacos.constant.NacosApplicationContext.EMPTY_ENVIRONMENT;

/**
 * 基于命名空间管理多个Nacos服务注册的对象
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-01
 * @see NacosServiceContext
 * @since 1.0
 */
@SuppressWarnings({ "unused", "SpellCheckingInspection" })
public class NacosContextManager implements SmartLifecycle, ApplicationEventPublisherAware {

	private static final Logger LOG = LoggerFactory.getLogger(NacosContextManager.class);

	private ApplicationEventPublisher applicationEventPublisher;
	private final NacosDiscoveryProperties discoveryProperties;
	private final NacosRegistrationFactory nacosRegistrationFactory;
	private final NacosServiceContext defaultNacosServiceContext;

	/**
	 * 缓存Nacos注册信息
	 */
	private final Map<String/*namespace.service*/, /*注册上下文*/NacosServiceContext> serviceManagers = new ConcurrentHashMap<>();

	public NacosContextManager(NacosDiscoveryProperties discoveryProperties,
			NacosServiceManager serviceManager,
			NacosRegistration registration,
			NacosServiceRegistry serviceRegistry,
			NacosRegistrationFactory nacosRegistrationFactory) {
		this.nacosRegistrationFactory = nacosRegistrationFactory;
		this.discoveryProperties = discoveryProperties;
		this.defaultNacosServiceContext = serviceManager != null && registration != null && serviceRegistry != null ?
			new NacosServiceContext(discoveryProperties, registration, serviceManager, serviceRegistry) : null;
	}

	/**
	 * 获取当前运行服务的{@link NacosServiceManager}
	 */
	@Nullable
	public NacosServiceContext obtainsNacosContext() {
		return defaultNacosServiceContext;
	}

	/**
	 * 通过命名空间获取{@link NacosServiceManager}
	 *
	 * @param namespace 命名空间
	 * @return {@link NacosServiceManager}
	 */
	@Nullable
	public NacosServiceContext obtainsNacosContext(String namespace, String serviceName) {
		namespace = NamespaceUtils.resolveNamespace(namespace);
		return serviceManagers.get(getContextKey(namespace, serviceName));
	}

	/**
	 * 创建并获取{@link NacosServiceContext}
	 *
	 * @param discoveryProperties {@link NacosDiscoveryProperties}
	 * @return {@link NacosServiceContext}
	 */
	public NacosServiceContext obtainsNacosContext(NacosDiscoveryProperties discoveryProperties) {
		String namespace = NamespaceUtils.resolveNamespace(discoveryProperties.getNamespace());
		String contextKey = getContextKey(namespace, discoveryProperties.getService());
		return serviceManagers.computeIfAbsent(contextKey, key ->
			this.doRegister(discoveryProperties));
	}

	/**
	 * 创建切换Nacoso上下文
	 *
	 * @param namespace   命名空间ID
	 * @param serviceName 服务名
	 * @return {@link NacosServiceContext}
	 */
	public NacosServiceContext switchNacosContext(String namespace, String serviceName) {
		boolean hasNamespace = StringUtils.hasText(namespace);
		boolean hasServiceName = StringUtils.hasText(serviceName);
		if (!hasNamespace && !hasServiceName) {
			return defaultNacosServiceContext;
		}
		if (CollectionUtils.isEmpty(this.serviceManagers)) {
			throw new ArthurException("There are nothing available NacosServiceContext");
		}

		NacosServiceContext serviceContext;
		if (hasNamespace && hasServiceName) {
			namespace = NamespaceUtils.resolveNamespace(namespace);
			String contextKey = getContextKey(namespace, serviceName);
			serviceContext = serviceManagers.get(contextKey);
			if (serviceContext != null) {
				return serviceContext;
			}
		}

		serviceContext = Optional.ofNullable(defaultNacosServiceContext)
			.orElseGet(() -> new ArrayList<>(serviceManagers.values()).get(0));
		NacosDiscoveryProperties properties = serviceContext.discoveryProperties();
		NacosDiscoveryProperties discoveryProperties = new NacosDiscoveryProperties();
		BeanCopierUtils.copy(properties, discoveryProperties);
		if (hasNamespace) {
			discoveryProperties.setNamespace(namespace);
		}
		if (hasServiceName) {
			discoveryProperties.setService(serviceName);
		}
		return this.obtainsNacosContext(discoveryProperties);
	}

	private NacosServiceContext doRegister(NacosDiscoveryProperties discoveryProperties) {
		Assert.notNull(discoveryProperties, "discoveryProperties");

		NacosServiceManager serviceManager = this.createNacosServiceManager(discoveryProperties);
		serviceManager.getNamingMaintainService(discoveryProperties.getNacosProperties());
		NacosRegistration registration = this.nacosRegistrationFactory.obtainsRegistration(discoveryProperties);
		NacosServiceRegistry serviceRegistry = new NacosServiceRegistry(serviceManager, discoveryProperties);
		return new NacosServiceContext(discoveryProperties, registration, serviceManager, serviceRegistry);
	}

	private NacosServiceManager createNacosServiceManager(NacosDiscoveryProperties discoveryProperties) {
		NacosServiceManager serviceManager = new NacosServiceManager();

		Field eventPublisherField = ReflectionUtils.findField(NacosDiscoveryProperties.class, "applicationEventPublisher");
		ReflectionUtils.makeAccessible(Objects.requireNonNull(eventPublisherField, "applicationEventPublisher"));
		ReflectionUtils.setField(eventPublisherField, discoveryProperties, applicationEventPublisher);

		Field serviceManagerField = ReflectionUtils.findField(NacosDiscoveryProperties.class, "nacosServiceManager");
		ReflectionUtils.makeAccessible(Objects.requireNonNull(serviceManagerField, "nacosServiceManager"));
		ReflectionUtils.setField(serviceManagerField, discoveryProperties, serviceManager);

		Field environmentField = ReflectionUtils.findField(NacosDiscoveryProperties.class, "environment");
		ReflectionUtils.makeAccessible(Objects.requireNonNull(environmentField, "environment"));
		ReflectionUtils.setField(environmentField, discoveryProperties, EMPTY_ENVIRONMENT);

		Assert.isTrue(StringUtils.hasText(discoveryProperties.getIp()), "ip is empty");
		Assert.isTrue(discoveryProperties.getPort() > 0, "port > 0");

		try {
			discoveryProperties.init();
			return serviceManager;
		} catch (Exception e) {
			throw new ArthurException(e);
		}
	}

	@Override
	public void start() {
		if (this.discoveryProperties == null) {
			return;
		}

		NacosServiceContext serviceContext = this.obtainsNacosContext();
		if (serviceContext == null) {
			return;
		}
		// 初始化
		NacosServiceManager serviceManager = serviceContext.serviceManager();
		if (serviceManager != null) {
			serviceManager.getNamingMaintainService(this.discoveryProperties.getNacosProperties());
		}
		String namespace = discoveryProperties.getNamespace();
		namespace = NamespaceUtils.resolveNamespace(namespace);
		serviceManagers.put(getContextKey(namespace, this.discoveryProperties.getService()), serviceContext);
	}

	@Override
	public void shutdown() {
		if (CollectionUtils.isEmpty(serviceManagers)) {
			return;
		}

		for (NacosServiceContext serviceContext : serviceManagers.values()) {
			try {
				serviceContext.serviceManager().nacosServiceShutDown();
			} catch (NacosException e) {
				LOG.error("Nacos namingService shutDown failed", e);
			}
		}
	}

	@Override
	public void setApplicationEventPublisher(@NonNull ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher = applicationEventPublisher;
	}

	@Override
	public int getOrder() {
		return MINIMUM_ORDER;
	}

	/**
	 * 组装缓存的key
	 *
	 * @param namespaceId 命名空间名称
	 * @param serviceName 服务名称
	 * @return key
	 */
	public static String getContextKey(String namespaceId, String serviceName) {
		return namespaceId + "." + serviceName;
	}

}
