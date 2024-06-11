package com.arthur.plugin.circuitbreaker.ratelimit;

import org.springframework.beans.BeansException;
import org.springframework.cloud.gateway.event.FilterArgsEvent;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.support.ConfigurationService;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.arthur.plugin.circuitbreaker.constant.CircuitBreakerConstant.KEY_RESOLVER_CAMEL_NAME;
import static com.arthur.plugin.circuitbreaker.constant.CircuitBreakerConstant.KEY_RESOLVER_NAME;

/**
 * 拓展SpringCloud Gateway的redis限流
 * <p>
 * 1.加入请求头”X-RateLimit-Algorithm“
 * 2.新增isSupport方法判断是否支持限流算法
 * 3.拓展支持多种限流规则并存路由的方式
 *
 * @author DearYang
 * @date 2022-08-11
 * @since 1.0
 */
@SuppressWarnings("unused")
public class RedisLuaRateLimiter extends RedisRateLimiter {
    public static final String RATE_LIMIT_ALGORITHM_HEADER = "X-RateLimit-Algorithm";

    private final RateLimitEnum type;
    private KeyResolver defaultKeyResolver;

    private String rateLimitAlgorithmHeader = RATE_LIMIT_ALGORITHM_HEADER;

    public RedisLuaRateLimiter(RateLimitEnum type, ReactiveStringRedisTemplate redisTemplate, RedisScript<List<Long>> script, ConfigurationService configurationService) {
        super(redisTemplate, script, configurationService);
        this.type = type;
    }

    public boolean supports(String rateLimitAlgorithm) {
        if (StringUtils.hasText(rateLimitAlgorithm)) {
            return rateLimitAlgorithm.equalsIgnoreCase(type.getRateLimitAlgorithm());
        }

        return false;
    }

    @Override
    public Map<String, String> getHeaders(Config config, Long tokensLeft) {
        if (!isIncludeHeaders()) {
            return new HashMap<>();
        }

        Map<String, String> headers = super.getHeaders(config, tokensLeft);
        headers.put(rateLimitAlgorithmHeader, type.getRateLimitAlgorithm());
        return headers;
    }

    public String getRateLimitAlgorithmHeader() {
        return rateLimitAlgorithmHeader;
    }

    public void setRateLimitAlgorithmHeader(String rateLimitAlgorithmHeader) {
        this.rateLimitAlgorithmHeader = rateLimitAlgorithmHeader;
    }

    @Override
    public void onApplicationEvent(FilterArgsEvent event) {
        super.onApplicationEvent(event);

        String routeId = event.getRouteId();
        Map<String, Config> configMap = getConfig();
        Config config = configMap.remove(routeId);
        if (config == null) {
            return;
        }

        Object keyResolver = event.getArgs().get(KEY_RESOLVER_NAME);
        if (keyResolver != null) {
            routeId += keyResolver.hashCode();
        } else if ((keyResolver = event.getArgs().get(KEY_RESOLVER_CAMEL_NAME)) != null) {
            routeId += keyResolver.hashCode();
        } else {
            routeId += defaultKeyResolver.hashCode();
        }

        configMap.put(routeId, config);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        super.setApplicationContext(context);
        this.defaultKeyResolver = context.getBean(KeyResolver.class);
        this.script = context.getBean(type.getBeanName(), RedisScript.class);
    }

}
