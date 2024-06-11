package com.arthur.web.antisamy.autoconfigure;

import com.arthur.web.antisamy.constant.AntiSamyPolicyType;
import com.arthur.web.antisamy.context.AntiSamyConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * AntiSamy配置文件
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-06
 * @since 1.0
 */
@ConfigurationProperties(prefix = "arthur.antisamy")
public class AntiSamyProperties {

	private AntiSamyPolicyType defaultPolicyType = AntiSamyPolicyType.DEFAULT;
	private Map<String/*请求路径表达式*/, AntiSamyRouteConfig> routes = new HashMap<>();

	public AntiSamyPolicyType getDefaultPolicyType() {
		return defaultPolicyType;
	}

	public void setDefaultPolicyType(AntiSamyPolicyType defaultPolicyType) {
		this.defaultPolicyType = defaultPolicyType;
	}

	public Map<String, AntiSamyRouteConfig> getRoutes() {
		return routes;
	}

	public void setRoutes(Map<String, AntiSamyRouteConfig> routes) {
		this.routes = routes;
	}

	public static class AntiSamyRouteConfig extends AntiSamyConfig {

		private String[] method;

		public String[] getMethod() {
			return method;
		}

		public void setMethod(String[] method) {
			this.method = method;
		}
	}

}
