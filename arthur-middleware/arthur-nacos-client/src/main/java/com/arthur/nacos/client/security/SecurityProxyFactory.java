package com.arthur.nacos.client.security;

import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.client.env.NacosClientProperties;
import com.alibaba.nacos.client.security.SecurityProxy;
import com.alibaba.nacos.client.utils.ValidatorUtils;
import com.alibaba.nacos.common.utils.StringUtils;
import com.arthur.nacos.client.utils.NamingUtils;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import static com.arthur.nacos.client.constant.NacosClientConstants.NS_INVALID_PARAM;

/**
 * {@link SecurityProxy}工厂类
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-17
 * @since 1.0
 */
public class SecurityProxyFactory {

    protected static final Map<String, SecurityProxy> SECURITY_PROXY_POOL = new ConcurrentHashMap<>();

    public static SecurityProxy obtainSecurityProxy(String username, String serverAddr) {
        return SECURITY_PROXY_POOL.get(getSecurityProxyKey(username, serverAddr));
    }

    public static SecurityProxy obtainSecurityProxy(Properties properties) throws NacosException {
        SecurityProxy securityProxy;
		NacosClientProperties clientProperties = NacosClientProperties.PROTOTYPE.derive(properties);
        ValidatorUtils.checkInitParam(clientProperties);
        String username = properties.getProperty(PropertyKeyConst.USERNAME);
		String serverAddr = properties.getProperty(PropertyKeyConst.SERVER_ADDR);
		if (StringUtils.isBlank(serverAddr)) {
			throw new NacosException(NS_INVALID_PARAM, "serverAddr is empty");
		}

		List<String> serverList = List.of(serverAddr.split(","));
		for (String server : serverList) {
			securityProxy = obtainSecurityProxy(username, server);
			if (securityProxy != null) {
				return securityProxy;
			}
		}

		securityProxy = NamingUtils.getSecurityProxy(true, properties);
        if (securityProxy instanceof ScheduledSecurityProxy scheduledSecurityProxy) {
            scheduledSecurityProxy.login(properties);
            scheduledSecurityProxy.initSecurityProxy(properties);
        }

        addSecurityProxy(username, serverList, securityProxy);
        return securityProxy;
    }

    public static void addSecurityProxy(String username, List<String> serverList, SecurityProxy securityProxy) {
        for (String server : serverList) {
            String securityProxyKey = getSecurityProxyKey(username, server);
            SECURITY_PROXY_POOL.computeIfAbsent(securityProxyKey, k -> securityProxy);
        }
    }

    private static String getSecurityProxyKey(String username, String serverAddr) {
        return serverAddr + ":" + username;
    }

}
