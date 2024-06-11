package com.arthur.sentinel.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

import static com.arthur.sentinel.constant.SentinelConstants.PROXY_IP_HEADERS;

/**
 * Sentinel相关配置
 *
 * @author DearYang
 * @date 2022-09-12
 * @since 1.0
 */
@ConfigurationProperties(prefix = "sentinel")
public class SentinelProperties {

	/**
	 * 解析请求IP的请求头
	 */
	private List<String> origins = List.of(PROXY_IP_HEADERS);

	public List<String> getOrigins() {
		return origins;
	}

	public void setOrigins(List<String> origins) {
		this.origins = origins;
	}

}
