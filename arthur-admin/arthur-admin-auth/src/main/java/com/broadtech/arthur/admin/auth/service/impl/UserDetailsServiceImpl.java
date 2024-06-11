package com.broadtech.arthur.admin.auth.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.broadtech.arthur.admin.auth.constant.MessageConstant;
import com.broadtech.arthur.admin.auth.entity.UserPermissionInfo;
import com.broadtech.arthur.admin.auth.bo.SecurityUserBo;
import com.broadtech.arthur.admin.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/9/16
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements  UserDetailsService {
    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<UserPermissionInfo> userPermissions = userService.findUserPermissionsByName(username,null);
        if (CollUtil.isEmpty(userPermissions)) {
            throw new UsernameNotFoundException(MessageConstant.USERNAME_PASSWORD_ERROR);
        }
        SecurityUserBo securityUserBo = new SecurityUserBo(userPermissions);
        if (!securityUserBo.isEnabled()) {
            throw new DisabledException(MessageConstant.ACCOUNT_DISABLED);
        } else if (!securityUserBo.isAccountNonLocked()) {
            throw new LockedException(MessageConstant.ACCOUNT_LOCKED);
        } else if (!securityUserBo.isAccountNonExpired()) {
            throw new AccountExpiredException(MessageConstant.ACCOUNT_EXPIRED);
        } else if (!securityUserBo.isCredentialsNonExpired()) {
            throw new CredentialsExpiredException(MessageConstant.CREDENTIALS_EXPIRED);
        }

        return securityUserBo;
    }
}
