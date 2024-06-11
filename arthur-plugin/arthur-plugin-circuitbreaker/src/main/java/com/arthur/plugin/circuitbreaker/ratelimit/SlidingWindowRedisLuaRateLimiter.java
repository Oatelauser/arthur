package com.arthur.plugin.circuitbreaker.ratelimit;

import com.arthur.plugin.circuitbreaker.utils.UUIDUtils;
import org.springframework.cloud.gateway.support.ConfigurationService;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.List;

/**
 * 滑动窗口限流器
 *
 * @author DearYang
 * @date 2022-08-12
 * @since 1.0
 */
public class SlidingWindowRedisLuaRateLimiter extends RedisLuaRateLimiter {

    public SlidingWindowRedisLuaRateLimiter(ReactiveStringRedisTemplate redisTemplate, RedisScript<List<Long>> script, ConfigurationService configurationService) {
        super(RateLimitEnum.SLIDING_WINDOW, redisTemplate, script, configurationService);
    }

    @Override
    public List<String> getKeys(String id) {
        String prefix = RateLimitEnum.SLIDING_WINDOW.getRedisKey() + ".{" + id;
        String tokenKey = prefix + "}.tokens";
        String timestampKey = UUIDUtils.getInstance().generateShortUuid();
        return List.of(tokenKey, timestampKey);
    }

}
