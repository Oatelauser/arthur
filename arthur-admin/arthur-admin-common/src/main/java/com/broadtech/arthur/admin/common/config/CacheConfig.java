package com.broadtech.arthur.admin.common.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author Machenike
 * date 2022/10/31
 * @version 1.0.0
 */

@Configuration
@EnableCaching
public class CacheConfig {
    @Resource
    CacheConfigProperties cacheConfigProperties;

    @Bean(name = "DEGRADE")
    public Caffeine singleDegradeCache() {

        return Caffeine.newBuilder()
                .expireAfterWrite(cacheConfigProperties.getWrite(), TimeUnit.valueOf(cacheConfigProperties.getWriteTimeGranularity()))
                .expireAfterAccess(cacheConfigProperties.getWrite(), TimeUnit.valueOf(cacheConfigProperties.getAccessTimeGranularity()))
                .maximumSize(1000);
    }

    @Bean(name = "IDEMPOTENT")
    public Cache<Object, Object> idempotentCache() {
        return Caffeine.newBuilder()
                .expireAfterWrite(cacheConfigProperties.getWrite(), TimeUnit.valueOf(cacheConfigProperties.getWriteTimeGranularity()))
                .expireAfterAccess(cacheConfigProperties.getWrite(), TimeUnit.valueOf(cacheConfigProperties.getAccessTimeGranularity()))
                .maximumSize(1000)
                .build();
    }


    @Bean
    public CacheManager getCaffeineCacheManager(Caffeine caffeine) {
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
        caffeineCacheManager.setCaffeine(caffeine);
        return caffeineCacheManager;
    }


}
