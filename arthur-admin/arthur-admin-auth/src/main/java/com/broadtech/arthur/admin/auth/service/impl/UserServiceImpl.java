package com.broadtech.arthur.admin.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.broadtech.arthur.admin.auth.entity.UserPermissionInfo;
import com.broadtech.arthur.admin.auth.entity.User;
import com.broadtech.arthur.admin.auth.service.UserService;
import com.broadtech.arthur.admin.auth.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Machenike
 * @description 针对表【user(路由用户表)】的数据库操作Service实现
 * @createDate 2022-09-20 14:37:27
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {


    @Override
    public List<UserPermissionInfo> findUserPermissionsByName(String username, Object expand) {
        return getBaseMapper()
                .findUserPermissionsByName(username);
    }

    @Override
    public List<UserPermissionInfo> loadAllUserPermissions() {
        return getBaseMapper().loadAllUserPermissions();
    }

}




