package com.broadtech.arthur.admin.resource.service.impl;

import cn.hutool.core.convert.Convert;
import com.broadtech.arthur.admin.resource.common.AuthConstants;
import com.broadtech.arthur.admin.resource.service.BaseService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.broadtech.arthur.admin.resource.constant.RedisConstant.RESOURCE_PERMISSION_MAP;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/9/15
 */
@Service
public class UserInfoServiceImpl implements BaseService {

    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean verify(String permission, String path) {
        Object obj = redisTemplate.opsForHash().get(RESOURCE_PERMISSION_MAP, path);
        String pathPermission = (AuthConstants.AUTHORITY_PREFIX + obj).trim();
        return pathPermission.equals(permission);
    }
}
