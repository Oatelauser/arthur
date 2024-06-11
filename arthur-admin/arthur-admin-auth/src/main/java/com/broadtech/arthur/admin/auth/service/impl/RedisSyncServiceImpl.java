package com.broadtech.arthur.admin.auth.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.broadtech.arthur.admin.auth.constant.RedisConstant;
import com.broadtech.arthur.admin.auth.service.UpLoadPermissionInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.TreeMap;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/9/16
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RedisSyncServiceImpl {
    private final UpLoadPermissionInfoService upLoadPermissionInfoService;

    @PostConstruct
    public void sync() {
        upLoadPermissionInfoService.load();
    }
}
