package com.arthur.plugin.circuitbreaker.ratelimit;

import org.springframework.cloud.gateway.support.ConfigurationService;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.List;

/**
 * 漏桶算法限流器
 *
 * @author DearYang
 * @date 2022-08-12
 * @since 1.0
 */
public class LeakyBucketRedisLuaRateLimiter extends RedisLuaRateLimiter {

    public LeakyBucketRedisLuaRateLimiter(ReactiveStringRedisTemplate redisTemplate, RedisScript<List<Long>> script, ConfigurationService configurationService) {
        super(RateLimitEnum.LEAKY_BUCKET, redisTemplate, script, configurationService);
    }

    @Override
    public List<String> getKeys(String id) {
        String prefix = RateLimitEnum.LEAKY_BUCKET.getRedisKey() + ".{" + id;
        String tokenKey = prefix + "}.tokens";
        String timestampKey = prefix + "}.timestamp";
        return List.of(tokenKey, timestampKey);
    }

}
