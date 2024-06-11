package com.arthur.boot.autoconfigure;

import com.arthur.boot.constant.CacheConstants;
import com.arthur.boot.core.url.AntUrlPathMather;
import com.arthur.boot.core.url.CachingUrlPathMather;
import com.arthur.boot.core.url.UrlPathMather;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

/**
 * URL匹配器自动配置类
 *
 * @author DearYang
 * @date 2022-08-16
 * @since 1.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = "arthur.url-matcher.enabled", havingValue = "true", matchIfMissing = true)
public class UrlPathMatherConfiguration {

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(name = "arthur.url-matcher.ant.enabled", havingValue = "true", matchIfMissing = true)
	public UrlPathMather defaultUrlPathMather() {
		return new AntUrlPathMather();
	}

	@Bean
	@ConditionalOnBean({CacheManager.class, UrlPathMather.class})
	@ConditionalOnProperty(name = "arthur.url-matcher.caching.enabled", havingValue = "true")
	public UrlPathMather cachingUrlPathMather(UrlPathMather urlPathMather, CacheManager cacheManager) {
		return new CachingUrlPathMather(urlPathMather, Objects.requireNonNull(cacheManager.getCache(CacheConstants.CACHE_URL_PARAMETER)));
	}

}
