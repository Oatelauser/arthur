package com.broadtech.arthur.admin.common.config;

import com.alibaba.fastjson2.schema.ValidateResult;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.Data;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.TimeUnit;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/8/22
 */
@ConfigurationProperties(prefix = "cache.expired")
@Data
public class CacheConfigProperties{
    private int    write;
    private String writeTimeGranularity;
    private int    access;
    private String accessTimeGranularity;
}
