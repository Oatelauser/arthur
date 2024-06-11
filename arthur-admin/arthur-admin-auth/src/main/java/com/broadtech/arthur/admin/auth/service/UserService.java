package com.broadtech.arthur.admin.auth.service;

import com.broadtech.arthur.admin.auth.entity.UserPermissionInfo;
import com.broadtech.arthur.admin.auth.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author Machenike
 * @description 针对表【user(路由用户表)】的数据库操作Service
 * @createDate 2022-09-20 14:37:27
 */
public interface UserService extends IService<User> {


    /**
     * 找到用户权限名字
     *
     * @param username 用户名
     * @param expand   扩大
     * @return {@link List}<{@link UserPermissionInfo}>
     */
    List<UserPermissionInfo> findUserPermissionsByName(String username, Object expand);

    /**
     * 加载所有用户权限
     *
     * @return {@link List}<{@link UserPermissionInfo}>
     */
    List<UserPermissionInfo> loadAllUserPermissions();
}
