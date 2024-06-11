package com.broadtech.arthur.admin.common.template;

import cn.hutool.core.util.ObjectUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/8/17
 */
@Component
public class CacheTemplate {

    Map<String, Cache<Object, Object>> cacheMap;

    @Autowired
    public void setCacheMap(Map<String, Cache<Object, Object>> cacheMap) {
        this.cacheMap = cacheMap;
    }

    public int upsert(String group, Object key, Object data) {
        Cache<Object, Object> cache = cacheMap.get(group);
        Preconditions.checkNotNull(cache,"缓存未命中");
        cache.put(key, data);
        return 1;
    }

    public int delAll(String group, String key) {
        cacheMap.remove(key);
        return 1;
    }

    public int del(String group, String key) {
        Cache<Object, Object> cache = cacheMap.get(group);
        Preconditions.checkNotNull(cache,"缓存未命中");
        cache.invalidate(key);
        return 1;
    }

    public Object select(String group, String key){
        Cache<Object, Object> cache = cacheMap.get(group);
        Preconditions.checkNotNull(cache,"缓存未命中");
        return cache.getIfPresent(key);
    }

    public Map<Object,Object> selectAll(String group){
        Cache<Object, Object> cache = cacheMap.get(group);
        Preconditions.checkNotNull(cache,"缓存未命中");
        return  cache.asMap();
    }


}
