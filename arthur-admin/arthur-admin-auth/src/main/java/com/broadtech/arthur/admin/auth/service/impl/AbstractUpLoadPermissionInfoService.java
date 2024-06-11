package com.broadtech.arthur.admin.auth.service.impl;

import com.broadtech.arthur.admin.auth.entity.UserPermissionInfo;
import com.broadtech.arthur.admin.auth.service.UpLoadPermissionInfoService;
import com.broadtech.arthur.admin.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/9/22
 */
public abstract class AbstractUpLoadPermissionInfoService implements UpLoadPermissionInfoService {
    UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void load() {
        List<UserPermissionInfo> userPermissions = userService.loadAllUserPermissions();
        upLoad(userPermissions);
    }
}
