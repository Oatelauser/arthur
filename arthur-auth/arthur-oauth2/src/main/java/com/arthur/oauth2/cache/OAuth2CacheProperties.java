package com.arthur.oauth2.cache;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * 缓存配置信息
 * <p>
 * copy by {@code org.springframework.cloud.loadbalancer.cache.LoadBalancerCacheProperties}
 *
 * @author DearYang
 * @date 2022-09-08
 * @since 1.0
 */
@ConfigurationProperties(prefix = "arthur.oauth2.cache")
public class OAuth2CacheProperties {

	private int capacity = 256;
	private Duration ttl = Duration.ofSeconds(30);
	private Caffeine caffeine = new Caffeine();

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public Duration getTtl() {
		return ttl;
	}

	public void setTtl(Duration ttl) {
		this.ttl = ttl;
	}

	public Caffeine getCaffeine() {
		return caffeine;
	}

	public void setCaffeine(Caffeine caffeine) {
		this.caffeine = caffeine;
	}

	public static class Caffeine {

		private String spec = "";

		public String getSpec() {
			return spec;
		}

		public void setSpec(String spec) {
			this.spec = spec;
		}

	}

}
