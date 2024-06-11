package com.arthur.boot.core.url;

import com.arthur.boot.constant.BootConstants;
import org.springframework.cache.Cache;

import java.util.Objects;

/**
 * Caffeine缓存URL匹配结果
 *
 * @author DearYang
 * @date 2022-08-16
 * @since 1.0
 */
public class CachingUrlPathMather extends AbstractCachingUrlPathMather {

	private final Cache cache;

	public CachingUrlPathMather(UrlPathMather urlPathMather, Cache cache) {
		super(urlPathMather);
		this.cache = cache;
	}

	@Override
	public Boolean getCache(String pattern, String path) {
		return (Boolean) Objects.requireNonNull(cache.get(pattern + BootConstants.CACHE_URL_DELIMITER + path)).get();
	}

	@Override
	public void cache(String pattern, String path, boolean matches) {
		cache.put(pattern + BootConstants.CACHE_URL_DELIMITER + path, matches);
	}

}
