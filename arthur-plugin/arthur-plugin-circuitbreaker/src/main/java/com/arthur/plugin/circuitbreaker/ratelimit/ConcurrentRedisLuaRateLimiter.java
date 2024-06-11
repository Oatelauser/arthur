package com.arthur.plugin.circuitbreaker.ratelimit;

import com.arthur.plugin.circuitbreaker.utils.UUIDUtils;
import org.springframework.cloud.gateway.support.ConfigurationService;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.List;

/**
 * 并发限流限流器
 *
 * @author DearYang
 * @date 2022-08-12
 * @since 1.0
 */
public class ConcurrentRedisLuaRateLimiter extends RedisLuaRateLimiter {

    public ConcurrentRedisLuaRateLimiter(ReactiveStringRedisTemplate redisTemplate, RedisScript<List<Long>> script, ConfigurationService configurationService) {
        super(RateLimitEnum.CONCURRENT, redisTemplate, script, configurationService);
    }

    @Override
    public List<String> getKeys(String id) {
        String tokenKey = RateLimitEnum.CONCURRENT.getRedisKey() + ".{" + id + "}.tokens";
        String requestKey = UUIDUtils.getInstance().generateShortUuid();
        return List.of(tokenKey, requestKey);
    }

}
