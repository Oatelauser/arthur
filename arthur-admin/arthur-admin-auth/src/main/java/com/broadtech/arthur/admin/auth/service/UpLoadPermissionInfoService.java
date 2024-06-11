package com.broadtech.arthur.admin.auth.service;

import com.broadtech.arthur.admin.auth.entity.UserPermissionInfo;

import java.util.List;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/9/22
 */
public interface UpLoadPermissionInfoService {

    /**
     * 加载
     */
    void load();


    /**
     * 上传
     *
     * @param userPermissions 用户权限
     */
    void upLoad( List<UserPermissionInfo> userPermissions);

}
