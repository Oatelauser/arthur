package com.broadtech.arthur.admin.common.service.impl;

import com.broadtech.arthur.admin.common.service.IdempotentService;
import com.github.benmanes.caffeine.cache.Cache;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/9/14
 */
@Component
public class IdempotentServiceImpl implements IdempotentService {


    private Cache<Object, Object> cache;

    private final ReentrantLock LOCK = new ReentrantLock();

    @Autowired
    @Qualifier(value = "IDEMPOTENT")
    public void setCache(Cache<Object, Object> cache) {
        this.cache = cache;
    }

    @Override
    public boolean checkDupSubmit(Object key) {
        LOCK.lock();
        try {
            Preconditions.checkNotNull(key, new IllegalArgumentException("key is null"));
            if (cache.getIfPresent(key) == null) {
                cache.put(key, 0);
                return false;
            }
            return true;
        } finally {
            LOCK.unlock();
        }


    }

}
