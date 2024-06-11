package com.arthur.plugin.circuitbreaker.ratelimit;

import com.arthur.plugin.circuitbreaker.constant.CircuitBreakerConstant;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;

/**
 * 限流算法枚举
 *
 * @author DearYang
 * @date 2022-08-11
 * @since 1.0
 */
@SuppressWarnings("unused")
public enum RateLimitEnum {

    /**
     * 滑动窗口
     */
    SLIDING_WINDOW("sliding_window_rate_limiter", "sliding_window_request_rate_limiter.lua", CircuitBreakerConstant.REDIS_SLIDING_WINDOW_BEAN_NAME, "rate_limiter.sliding_window"),
    /**
     * 漏桶算法
     */
    LEAKY_BUCKET("leaky_bucket_rate_limiter", "request_leaky_rate_limiter.lua", CircuitBreakerConstant.REDIS_LEAKY_BUCKET_BEAN_NAME, "rate_limiter.leaky_bucket"),
    /**
     * 并发请求限流
     */
    CONCURRENT("concurrent_request_rate_limiter", "concurrent_request_rate_limiter.lua", CircuitBreakerConstant.REDIS_CONCURRENT_REQUEST_BEAN_NAME, "rate_limiter.concurrent_request"),
    /**
     * 令牌桶
     */
    TOKEN_BUCKET("token_bucket_rate_limiter", "request_rate_limiter.lua", RedisRateLimiter.REDIS_SCRIPT_NAME, "rate_limiter.token_bucket"),
    ;

    private final String rateLimitAlgorithm;
    private final String luaScriptName;
    private final String beanName;
    private final String redisKey;

    RateLimitEnum(final String rateLimitAlgorithm, final String luaScriptName, final String beanName, final String redisKey) {
        this.rateLimitAlgorithm = rateLimitAlgorithm;
        this.luaScriptName = luaScriptName;
        this.beanName = beanName;
        this.redisKey = redisKey;
    }

    /**
     * 限流算法名
     *
     * @return rateLimitAlgorithm
     */
    public String getRateLimitAlgorithm() {
        return this.rateLimitAlgorithm;
    }

    /**
     * lua脚本名称
     *
     * @return scriptName
     */
    public String getLuaScriptName() {
        return this.luaScriptName;
    }

    /**
     * lua脚本的bean名称
     *
     * @return bean名称
     */
    public String getBeanName() {
        return this.beanName;
    }

    /**
     * 存放在redis的key
     *
     * @return redis key
     */
    public String getRedisKey() {
        return redisKey;
    }

}
