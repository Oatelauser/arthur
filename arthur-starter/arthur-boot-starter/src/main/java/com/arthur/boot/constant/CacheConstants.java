package com.arthur.boot.constant;

import com.arthur.boot.autoconfigure.UrlPathMatherConfiguration;

/**
 * 缓存常量
 *
 * @author DearYang
 * @date 2022-09-05
 * @since 1.0
 */
public interface CacheConstants {

	/**
	 * 缓存URL匹配结果的key
	 *
	 * @see UrlPathMatherConfiguration
	 */
	String CACHE_URL_PARAMETER = "url_matches_cache";

}
