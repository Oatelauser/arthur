package com.arthur.plugin.circuitbreaker.autoconfigure;

import com.arthur.plugin.circuitbreaker.filter.factory.RateLimiterGatewayFilterFactory;
import com.arthur.plugin.circuitbreaker.key.KeyResolverFactory;
import com.arthur.plugin.circuitbreaker.ratelimit.*;
import com.arthur.web.reactive.server.WebServerResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisReactiveAutoConfiguration;
import org.springframework.cloud.gateway.config.GatewayAutoConfiguration;
import org.springframework.cloud.gateway.config.conditional.ConditionalOnEnabledFilter;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RateLimiter;
import org.springframework.cloud.gateway.support.ConfigurationService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.web.reactive.DispatcherHandler;

import java.util.List;

import static com.arthur.plugin.circuitbreaker.constant.CircuitBreakerConstant.*;

/**
 * Redis的分布式限流算法配置类
 *
 * @author DearYang
 * @date 2022-08-09
 * @since 1.0
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@Import(KeyResolverConfiguration.class)
@ConditionalOnBean(ReactiveRedisTemplate.class)
@ConditionalOnClass({RedisTemplate.class, DispatcherHandler.class})
@AutoConfiguration(before = GatewayAutoConfiguration.class, after = RedisReactiveAutoConfiguration.class)
@ConditionalOnProperty(name = {"spring.cloud.gateway.redis.enabled", "spring.cloud.gateway.enabled"}, havingValue = "true", matchIfMissing = true)
public class GatewayRateLimiterAutoConfiguration {

    @Bean(name = REDIS_CONCURRENT_REQUEST_BEAN_NAME)
    public RedisScript redisConcurrentRequestRateLimiterScript() {
        return buildRedisScript("concurrent_request_rate_limiter.lua");
    }

    @Bean(name = REDIS_LEAKY_BUCKET_BEAN_NAME)
    public RedisScript redisLeakyBucketRateLimiterScript() {
        return buildRedisScript("request_leaky_rate_limiter.lua");
    }

    @Bean(name = REDIS_SLIDING_WINDOW_BEAN_NAME)
    public RedisScript redisSlidingWindowRateLimiterScript() {
        return buildRedisScript("sliding_window_request_rate_limiter.lua");
    }

    @Bean
    public RedisLuaRateLimiter concurrentRateLimiter(ReactiveStringRedisTemplate redisTemplate, ConfigurationService configurationService, ApplicationContext context) {
        RedisScript redisScript = getRedisScriptBean(RateLimitEnum.CONCURRENT, context);
        return new ConcurrentRedisLuaRateLimiter(redisTemplate, redisScript, configurationService);
    }

    @Bean
    public RedisLuaRateLimiter leakyBucketRateLimiter(ReactiveStringRedisTemplate redisTemplate, ConfigurationService configurationService, ApplicationContext context) {
        RedisScript redisScript = getRedisScriptBean(RateLimitEnum.LEAKY_BUCKET, context);
        return new LeakyBucketRedisLuaRateLimiter(redisTemplate, redisScript, configurationService);
    }

    @Bean
    public RedisLuaRateLimiter slidingWindowRateLimiter(ReactiveStringRedisTemplate redisTemplate, ConfigurationService configurationService, ApplicationContext context) {
        RedisScript redisScript = getRedisScriptBean(RateLimitEnum.SLIDING_WINDOW, context);
        return new SlidingWindowRedisLuaRateLimiter(redisTemplate, redisScript, configurationService);
    }

    @Bean
    @Primary
    public RedisLuaRateLimiter tokenBucketRateLimiter(ReactiveStringRedisTemplate redisTemplate, ConfigurationService configurationService, ApplicationContext context) {
        RedisScript redisScript = getRedisScriptBean(RateLimitEnum.TOKEN_BUCKET, context);
        return new TokenBucketRedisLuaRateLimiter(redisTemplate, redisScript, configurationService);
    }

    @Bean
    @ConditionalOnEnabledFilter
    public RateLimiterGatewayFilterFactory requestRateLimiterMappingGatewayFilterFactory(
			@Qualifier("tokenBucketRateLimiter") RedisLuaRateLimiter defaultRateLimiter,
			KeyResolver defaultKeyResolver, KeyResolverFactory keyResolverFactory,
			List<RateLimiter> rateLimiters, WebServerResponse webServerResponse) {
        return new RateLimiterGatewayFilterFactory(defaultKeyResolver, keyResolverFactory,
			defaultRateLimiter, rateLimiters, webServerResponse);
    }

    /**
     * 创建redis lua脚本
     *
     * @param luaScriptFileName lua脚本文件名
     * @return {@link RedisScript}
     */
    private RedisScript buildRedisScript(String luaScriptFileName) {
        DefaultRedisScript redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource(DEFAULT_SCRIPT_PATH + luaScriptFileName)));
        redisScript.setResultType(List.class);
        return redisScript;
    }

    /**
     * 构建{@link RedisLuaRateLimiter}对象
     *
     * @param type    {@link RateLimitEnum}
     * @param context {@link ApplicationContext}
     * @return {@link RedisLuaRateLimiter}
     */
    private RedisScript getRedisScriptBean(RateLimitEnum type, ApplicationContext context) {
        return context.getBean(type.getBeanName(), RedisScript.class);
    }

}
