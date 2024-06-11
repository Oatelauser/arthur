package com.arthur.oauth2.constant;

import com.arthur.oauth2.resource.RemoteUserDetailsService;

/**
 * 缓存常量
 *
 * @author DearYang
 * @date 2022-09-05
 * @since 1.0
 */
public interface CacheConstants {

	/**
	 * 缓存用户详情信息
	 *
	 * @see RemoteUserDetailsService
	 */
	String CACHE_USER_DETAILS_PARAMETER = "user_details_cache";

}
