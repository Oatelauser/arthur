package com.arthur.oauth2.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.util.StringUtils;

/**
 * 自定义Caffeine缓存管理器
 *
 * @author DearYang
 * @date 2022-09-08
 * @since 1.0
 */
public class CaffeineBasedOAuth2CacheManager extends CaffeineCacheManager implements OAuth2CacheManager {

	public CaffeineBasedOAuth2CacheManager(String cacheName, OAuth2CacheProperties properties) {
		super(cacheName);
		this.applyCaffeineProperties(properties);
	}

	private void applyCaffeineProperties(OAuth2CacheProperties properties) {
		if (StringUtils.hasText(properties.getCaffeine().getSpec())) {
			setCacheSpecification(properties.getCaffeine().getSpec());
		} else {
			setCaffeine(Caffeine.newBuilder().initialCapacity(properties.getCapacity())
				.expireAfterWrite(properties.getTtl())
				.softValues());
		}
	}

}
