package com.broadtech.arthur.admin.auth.service.impl;

import com.alibaba.fastjson2.JSON;
import com.broadtech.arthur.admin.auth.entity.UserPermissionInfo;
import com.broadtech.arthur.admin.auth.service.UpLoadPermissionInfoService;
import com.broadtech.arthur.admin.auth.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.broadtech.arthur.admin.auth.constant.RedisConstant.RESOURCE_PERMISSION_MAP;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/9/22
 */
@Component
@Slf4j
public class RedisUpLoadPermissionInfoService extends AbstractUpLoadPermissionInfoService {

    RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void upLoad(List<UserPermissionInfo> userPermissions) {
        Map<String, String> pathPermissionMap = userPermissions.stream().collect(
                Collectors.toMap(UserPermissionInfo::getEndpoint, UserPermissionInfo::getPermission));
        if (log.isDebugEnabled()){
            log.debug("redis upload data : {}", JSON.toJSONString(pathPermissionMap));
        }
        redisTemplate.opsForHash().putAll(RESOURCE_PERMISSION_MAP, pathPermissionMap);
    }
}
