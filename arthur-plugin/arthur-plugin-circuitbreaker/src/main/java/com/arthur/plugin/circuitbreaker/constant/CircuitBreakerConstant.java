package com.arthur.plugin.circuitbreaker.constant;


import com.arthur.plugin.circuitbreaker.autoconfigure.GatewayRateLimiterAutoConfiguration;
import com.arthur.plugin.circuitbreaker.autoconfigure.KeyResolverConfiguration;
import com.arthur.plugin.circuitbreaker.ratelimit.RedisLuaRateLimiter;

/**
 * 限流熔断常量
 *
 * @author DearYang
 * @date 2022-08-09
 * @since 1.0
 */
public interface CircuitBreakerConstant {

    /**
     * lua脚本的基本路径
     *
     * @see GatewayRateLimiterAutoConfiguration
     */
    String DEFAULT_SCRIPT_PATH = "META-INF/scripts/";

    /**
     * 并发请求Bean名称
     *
     * @see GatewayRateLimiterAutoConfiguration
     */
    String REDIS_CONCURRENT_REQUEST_BEAN_NAME = "redisConcurrentRequestRateLimiterScript";

    /**
     * 漏桶算法Bean名称
     *
     * @see GatewayRateLimiterAutoConfiguration
     */
    String REDIS_LEAKY_BUCKET_BEAN_NAME = "redisLeakyBucketRateLimiterScript";

    /**
     * 滑动窗口Bean名称
     *
     * @see GatewayRateLimiterAutoConfiguration
     */
    String REDIS_SLIDING_WINDOW_BEAN_NAME = "redisSlidingWindowRateLimiterScript";

    /**
     * 基于远程地址
     *
     * @see KeyResolverConfiguration
     */
    String KEY_RESOLVER_REMOTE_ADDR = "remoteAddrKeyResolver";

    /**
     * 基于url path
     *
     * @see KeyResolverConfiguration
     */
    String KEY_RESOLVER_URL_PATH = "urlPathKeyResolver";

    /**
     * 基于principalName
     *
     * @see KeyResolverConfiguration
     */
    String KEY_RESOLVER_PRINCIPAL = "principalKeyResolver";

    /**
     * 基于路由ID
     *
     * @see KeyResolverConfiguration
     */
    String KEY_RESOLVER_ROUTE_ID = "routeIdKeyResolver";

    /**
     * 路由策略的解析名
     *
     * @see RedisLuaRateLimiter
     */
    String KEY_RESOLVER_NAME = "key-resolver";

    /**
     * 路由策略的解析名
     *
     * @see RedisLuaRateLimiter
     */
    String KEY_RESOLVER_CAMEL_NAME = "keyResolver";

	String KEY_RESOLVER_ROUTE_KEY = "RouteId";

}
