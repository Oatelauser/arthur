package com.arthur.nacos.client.naming;

import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.EventListener;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.api.naming.pojo.ListView;
import com.alibaba.nacos.api.naming.pojo.ServiceInfo;
import com.alibaba.nacos.api.selector.AbstractSelector;
import com.alibaba.nacos.client.env.NacosClientProperties;
import com.alibaba.nacos.client.naming.event.InstancesChangeEvent;
import com.alibaba.nacos.client.naming.event.InstancesChangeNotifier;
import com.alibaba.nacos.client.naming.utils.InitUtils;
import com.alibaba.nacos.client.utils.ValidatorUtils;
import com.alibaba.nacos.common.notify.NotifyCenter;

import java.util.List;
import java.util.Properties;
import java.util.UUID;

/**
 * 过期类替代查看{@link NacosNamingServiceAdapter}
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-03
 * @since 1.0
 */
@Deprecated
@SuppressWarnings("SpellCheckingInspection")
public class NamingServiceAdapter implements NamingService {

	//private static final ObjectInstantiator<NacosNamingService> instantiator;
	//
	//static {
	//	Objenesis objenesis = new ObjenesisStd();
	//	instantiator = objenesis.getInstantiatorOf(NacosNamingService.class);
	//}

	//private final NamingService namingService = instantiator.newInstance();
	private NamingService namingService;

	public NamingServiceAdapter(Properties properties) throws NacosException {
		this.initializeProperties(properties);
	}

	public NamingServiceAdapter(String serverList) throws NacosException {
		Properties properties = new Properties();
		properties.setProperty(PropertyKeyConst.SERVER_ADDR, serverList);
		this.initializeProperties(properties);
	}

	/**
	 * 参考{@code NacosNamingService#init(Properties)}
	 */
	private void initializeProperties(Properties properties) throws NacosException {
		final NacosClientProperties nacosClientProperties = NacosClientProperties.PROTOTYPE.derive(properties);

		ValidatorUtils.checkInitParam(nacosClientProperties);
		String namespace = InitUtils.initNamespaceForNaming(nacosClientProperties);
		InitUtils.initSerialization();
		InitUtils.initWebRootContext(nacosClientProperties);

		String notifierEventScope = UUID.randomUUID().toString();
		InstancesChangeNotifier changeNotifier = new InstancesChangeNotifier(notifierEventScope);
		NotifyCenter.registerToPublisher(InstancesChangeEvent.class, 16384);
		NotifyCenter.registerSubscriber(changeNotifier);
	}

	@Override
	public void registerInstance(String serviceName, String ip, int port) throws NacosException {
		namingService.registerInstance(serviceName, ip, port);
	}

	@Override
	public void registerInstance(String serviceName, String groupName, String ip, int port) throws NacosException {
		namingService.registerInstance(serviceName, groupName, ip, port);
	}

	@Override
	public void registerInstance(String serviceName, String ip, int port, String clusterName) throws NacosException {
		namingService.registerInstance(serviceName, ip, port, clusterName);
	}

	@Override
	public void registerInstance(String serviceName, String groupName, String ip, int port, String clusterName) throws NacosException {
		namingService.registerInstance(serviceName, groupName, ip, port, clusterName);
	}

	@Override
	public void registerInstance(String serviceName, Instance instance) throws NacosException {
		namingService.registerInstance(serviceName, instance);
	}

	@Override
	public void registerInstance(String serviceName, String groupName, Instance instance) throws NacosException {
		namingService.registerInstance(serviceName, groupName, instance);
	}

	@Override
	public void batchRegisterInstance(String serviceName, String groupName, List<Instance> instances) throws NacosException {
		namingService.batchRegisterInstance(serviceName, groupName, instances);
	}

	@Override
	public void batchDeregisterInstance(String serviceName, String groupName, List<Instance> instances) throws NacosException {
		namingService.batchDeregisterInstance(serviceName, groupName, instances);
	}

	@Override
	public void deregisterInstance(String serviceName, String ip, int port) throws NacosException {
		namingService.deregisterInstance(serviceName, ip, port);
	}

	@Override
	public void deregisterInstance(String serviceName, String groupName, String ip, int port) throws NacosException {
		namingService.deregisterInstance(serviceName, groupName, ip, port);
	}

	@Override
	public void deregisterInstance(String serviceName, String ip, int port, String clusterName) throws NacosException {
		namingService.deregisterInstance(serviceName, ip, port, clusterName);
	}

	@Override
	public void deregisterInstance(String serviceName, String groupName, String ip, int port, String clusterName) throws NacosException {
		namingService.deregisterInstance(serviceName, groupName, ip, port, clusterName);
	}

	@Override
	public void deregisterInstance(String serviceName, Instance instance) throws NacosException {
		namingService.deregisterInstance(serviceName, instance);
	}

	@Override
	public void deregisterInstance(String serviceName, String groupName, Instance instance) throws NacosException {
		namingService.deregisterInstance(serviceName, groupName, instance);
	}

	@Override
	public List<Instance> getAllInstances(String serviceName) throws NacosException {
		return namingService.getAllInstances(serviceName);
	}

	@Override
	public List<Instance> getAllInstances(String serviceName, String groupName) throws NacosException {
		return namingService.getAllInstances(serviceName, groupName);
	}

	@Override
	public List<Instance> getAllInstances(String serviceName, boolean subscribe) throws NacosException {
		return namingService.getAllInstances(serviceName, subscribe);
	}

	@Override
	public List<Instance> getAllInstances(String serviceName, String groupName, boolean subscribe) throws NacosException {
		return namingService.getAllInstances(serviceName, groupName, subscribe);
	}

	@Override
	public List<Instance> getAllInstances(String serviceName, List<String> clusters) throws NacosException {
		return namingService.getAllInstances(serviceName, clusters);
	}

	@Override
	public List<Instance> getAllInstances(String serviceName, String groupName, List<String> clusters) throws NacosException {
		return namingService.getAllInstances(serviceName, groupName, clusters);
	}

	@Override
	public List<Instance> getAllInstances(String serviceName, List<String> clusters, boolean subscribe) throws NacosException {
		return namingService.getAllInstances(serviceName, clusters, subscribe);
	}

	@Override
	public List<Instance> getAllInstances(String serviceName, String groupName, List<String> clusters, boolean subscribe) throws NacosException {
		return namingService.getAllInstances(serviceName, groupName, clusters, subscribe);
	}

	@Override
	public List<Instance> selectInstances(String serviceName, boolean healthy) throws NacosException {
		return namingService.selectInstances(serviceName, healthy);
	}

	@Override
	public List<Instance> selectInstances(String serviceName, String groupName, boolean healthy) throws NacosException {
		return namingService.selectInstances(serviceName, groupName, healthy);
	}

	@Override
	public List<Instance> selectInstances(String serviceName, boolean healthy, boolean subscribe) throws NacosException {
		return namingService.selectInstances(serviceName, healthy, subscribe);
	}

	@Override
	public List<Instance> selectInstances(String serviceName, String groupName, boolean healthy, boolean subscribe) throws NacosException {
		return namingService.selectInstances(serviceName, groupName, healthy, subscribe);
	}

	@Override
	public List<Instance> selectInstances(String serviceName, List<String> clusters, boolean healthy) throws NacosException {
		return namingService.selectInstances(serviceName, clusters, healthy);
	}

	@Override
	public List<Instance> selectInstances(String serviceName, String groupName, List<String> clusters, boolean healthy) throws NacosException {
		return namingService.selectInstances(serviceName, groupName, clusters, healthy);
	}

	@Override
	public List<Instance> selectInstances(String serviceName, List<String> clusters, boolean healthy, boolean subscribe) throws NacosException {
		return namingService.selectInstances(serviceName, clusters, healthy, subscribe);
	}

	@Override
	public List<Instance> selectInstances(String serviceName, String groupName, List<String> clusters, boolean healthy, boolean subscribe) throws NacosException {
		return namingService.selectInstances(serviceName, groupName, clusters, subscribe);
	}

	@Override
	public Instance selectOneHealthyInstance(String serviceName) throws NacosException {
		return namingService.selectOneHealthyInstance(serviceName);
	}

	@Override
	public Instance selectOneHealthyInstance(String serviceName, String groupName) throws NacosException {
		return namingService.selectOneHealthyInstance(serviceName, groupName);
	}

	@Override
	public Instance selectOneHealthyInstance(String serviceName, boolean subscribe) throws NacosException {
		return namingService.selectOneHealthyInstance(serviceName, subscribe);
	}

	@Override
	public Instance selectOneHealthyInstance(String serviceName, String groupName, boolean subscribe) throws NacosException {
		return namingService.selectOneHealthyInstance(serviceName, groupName, subscribe);
	}

	@Override
	public Instance selectOneHealthyInstance(String serviceName, List<String> clusters) throws NacosException {
		return namingService.selectOneHealthyInstance(serviceName, clusters);
	}

	@Override
	public Instance selectOneHealthyInstance(String serviceName, String groupName, List<String> clusters) throws NacosException {
		return namingService.selectOneHealthyInstance(serviceName, groupName, clusters);
	}

	@Override
	public Instance selectOneHealthyInstance(String serviceName, List<String> clusters, boolean subscribe) throws NacosException {
		return namingService.selectOneHealthyInstance(serviceName, clusters, subscribe);
	}

	@Override
	public Instance selectOneHealthyInstance(String serviceName, String groupName, List<String> clusters, boolean subscribe) throws NacosException {
		return namingService.selectOneHealthyInstance(serviceName, groupName, clusters, subscribe);
	}

	@Override
	public void subscribe(String serviceName, EventListener listener) throws NacosException {
		namingService.subscribe(serviceName, listener);
	}

	@Override
	public void subscribe(String serviceName, String groupName, EventListener listener) throws NacosException {
		namingService.subscribe(serviceName, groupName, listener);
	}

	@Override
	public void subscribe(String serviceName, List<String> clusters, EventListener listener) throws NacosException {
		namingService.subscribe(serviceName, clusters, listener);
	}

	@Override
	public void subscribe(String serviceName, String groupName, List<String> clusters, EventListener listener) throws NacosException {
		namingService.subscribe(serviceName, groupName, clusters, listener);
	}

	@Override
	public void unsubscribe(String serviceName, EventListener listener) throws NacosException {
		namingService.unsubscribe(serviceName, listener);
	}

	@Override
	public void unsubscribe(String serviceName, String groupName, EventListener listener) throws NacosException {
		namingService.unsubscribe(serviceName, groupName, listener);
	}

	@Override
	public void unsubscribe(String serviceName, List<String> clusters, EventListener listener) throws NacosException {
		namingService.unsubscribe(serviceName, clusters, listener);
	}

	@Override
	public void unsubscribe(String serviceName, String groupName, List<String> clusters, EventListener listener) throws NacosException {
		namingService.unsubscribe(serviceName, groupName, clusters, listener);
	}

	@Override
	public ListView<String> getServicesOfServer(int pageNo, int pageSize) throws NacosException {
		return namingService.getServicesOfServer(pageNo, pageSize);
	}

	@Override
	public ListView<String> getServicesOfServer(int pageNo, int pageSize, String groupName) throws NacosException {
		return namingService.getServicesOfServer(pageNo, pageSize, groupName);
	}

	@Override
	public ListView<String> getServicesOfServer(int pageNo, int pageSize, AbstractSelector selector) throws NacosException {
		return namingService.getServicesOfServer(pageNo, pageSize, selector);
	}

	@Override
	public ListView<String> getServicesOfServer(int pageNo, int pageSize, String groupName, AbstractSelector selector) throws NacosException {
		return namingService.getServicesOfServer(pageNo, pageSize, groupName, selector);
	}

	@Override
	public List<ServiceInfo> getSubscribeServices() throws NacosException {
		return namingService.getSubscribeServices();
	}

	@Override
	public String getServerStatus() {
		return namingService.getServerStatus();
	}

	@Override
	public void shutDown() throws NacosException {
		namingService.shutDown();
	}

}
