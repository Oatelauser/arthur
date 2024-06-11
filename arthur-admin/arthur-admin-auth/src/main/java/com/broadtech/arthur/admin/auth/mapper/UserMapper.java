package com.broadtech.arthur.admin.auth.mapper;

import com.broadtech.arthur.admin.auth.entity.UserPermissionInfo;
import com.broadtech.arthur.admin.auth.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author Machenike
* @description 针对表【user(路由用户表)】的数据库操作Mapper
* @createDate 2022-09-20 14:37:27
* @Entity com.broadtech.arthur.admin.auth.entity.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 找到用户权限名字
     *
     * @param username 用户名
     * @return {@link List}<{@link UserPermissionInfo}>
     */
    List<UserPermissionInfo> findUserPermissionsByName(String username);

    /**
     * 加载所有用户权限
     *
     * @return {@link List}<{@link UserPermissionInfo}>
     */
    List<UserPermissionInfo> loadAllUserPermissions();
}




