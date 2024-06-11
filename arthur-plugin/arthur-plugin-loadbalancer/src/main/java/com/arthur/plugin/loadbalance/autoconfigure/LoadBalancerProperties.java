package com.arthur.plugin.loadbalance.autoconfigure;

import com.arthur.plugin.loadbalance.constant.LoadBalancerConstants;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.Map;

/**
 * 负载均衡配置信息
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-01-05
 * @since 1.0
 */
@ConfigurationProperties(prefix = "arthur.loadbalancer")
public class LoadBalancerProperties {

	/**
	 * 全局默认的负载均衡初始化URL
	 */
	private String defaultLbInitializeUrl = "http://{serviceId}/actuator/health";

	/**
	 * key-ServiceId（服务注册名），value-负载均衡初始化URL
	 */
	private Map<String, String> lbInitializeUrls = Collections.emptyMap();

	public String getDefaultLbInitializeUrl() {
		return defaultLbInitializeUrl;
	}

	public void setDefaultLbInitializeUrl(String defaultLbInitializeUrl) {
		if (this.validateLbInitializeUrl(defaultLbInitializeUrl)) {
			this.defaultLbInitializeUrl = defaultLbInitializeUrl;
		}
	}

	public Map<String, String> getLbInitializeUrls() {
		return lbInitializeUrls;
	}

	public void setLbInitializeUrls(Map<String, String> lbInitializeUrls) {
		if (!CollectionUtils.isEmpty(lbInitializeUrls)) {
			lbInitializeUrls.entrySet()
				.removeIf(stringStringEntry ->
					!this.validateLbInitializeUrl(stringStringEntry.getValue()));
			this.lbInitializeUrls = lbInitializeUrls;
		}
	}

	private boolean validateLbInitializeUrl(String lbInitializeUrl) {
		return StringUtils.hasText(lbInitializeUrl) && lbInitializeUrl.contains(LoadBalancerConstants.LB_URL_ATTR);
	}

}
