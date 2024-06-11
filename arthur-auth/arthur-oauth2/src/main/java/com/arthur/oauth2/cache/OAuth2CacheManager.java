package com.arthur.oauth2.cache;

import com.arthur.oauth2.resource.RemoteUserDetailsService;
import org.springframework.cache.CacheManager;

/**
 * 缓存管理拓展标识接口
 *
 * @author DearYang
 * @date 2022-09-08
 * @see RemoteUserDetailsService
 * @since 1.0
 */
public interface OAuth2CacheManager extends CacheManager {
}
