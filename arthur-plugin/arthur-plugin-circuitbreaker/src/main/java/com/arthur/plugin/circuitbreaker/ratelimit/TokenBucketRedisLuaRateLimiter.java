package com.arthur.plugin.circuitbreaker.ratelimit;

import org.springframework.cloud.gateway.support.ConfigurationService;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.Arrays;
import java.util.List;

/**
 * 令牌桶限流器
 *
 * @author DearYang
 * @date 2022-08-12
 * @since 1.0
 */
public class TokenBucketRedisLuaRateLimiter extends RedisLuaRateLimiter {

    public TokenBucketRedisLuaRateLimiter(ReactiveStringRedisTemplate redisTemplate, RedisScript<List<Long>> script, ConfigurationService configurationService) {
        super(RateLimitEnum.TOKEN_BUCKET, redisTemplate, script, configurationService);
    }

    @Override
    public List<String> getKeys(String id) {
        String prefix = RateLimitEnum.TOKEN_BUCKET.getRedisKey() + ".{" + id;
        String tokenKey = prefix + "}.tokens";
        String timestampKey = prefix + "}.timestamp";
        return Arrays.asList(tokenKey, timestampKey);
    }

}
