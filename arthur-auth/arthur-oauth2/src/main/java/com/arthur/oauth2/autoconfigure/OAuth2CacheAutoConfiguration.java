package com.arthur.oauth2.autoconfigure;

import com.arthur.boot.conditional.ConditionalOnMissingCaffeine;
import com.arthur.oauth2.cache.CaffeineBasedOAuth2CacheManager;
import com.arthur.oauth2.cache.DefaultOAuth2CacheManager;
import com.arthur.oauth2.cache.OAuth2CacheManager;
import com.arthur.oauth2.cache.OAuth2CacheProperties;
import com.arthur.oauth2.constant.CacheConstants;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OAuth2缓存自动配置类
 *
 * @author DearYang
 * @date 2022-09-08
 * @since 1.0
 */
@AutoConfiguration(after = CacheAutoConfiguration.class)
@EnableConfigurationProperties(OAuth2CacheProperties.class)
@ConditionalOnClass({CacheManager.class, CacheAutoConfiguration.class})
public class OAuth2CacheAutoConfiguration {

	@Configuration(proxyBeanMethods = false)
	@ConditionalOnClass({Caffeine.class, CaffeineCacheManager.class})
	static class CaffeineOAuth2CacheManagerConfiguration {

		@Bean
		@ConditionalOnMissingBean
        OAuth2CacheManager oAuth2CacheManager(OAuth2CacheProperties cacheProperties) {
			return new CaffeineBasedOAuth2CacheManager(CacheConstants.CACHE_USER_DETAILS_PARAMETER, cacheProperties);
		}

	}

	@ConditionalOnMissingCaffeine
	@Configuration(proxyBeanMethods = false)
	static class DefaultCacheManagerConfiguration {

		@Bean
		@ConditionalOnMissingBean
		OAuth2CacheManager oAuth2CacheManager() {
			return new DefaultOAuth2CacheManager();
		}

	}

}
