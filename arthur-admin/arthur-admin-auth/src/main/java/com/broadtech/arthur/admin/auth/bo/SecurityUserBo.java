package com.broadtech.arthur.admin.auth.bo;

import cn.hutool.core.collection.ListUtil;
import com.broadtech.arthur.admin.auth.entity.UserPermissionInfo;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/9/16
 */

@Data
public class SecurityUserBo implements UserDetails {

    /**
     * ID
     */
    private String id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 用户密码
     */
    private String password;
    /**
     * 用户状态
     */
    private Boolean enabled;
    /**
     * 权限数据
     */
    private Collection<SimpleGrantedAuthority> authorities;

    public SecurityUserBo() {

    }

    public SecurityUserBo(List<UserPermissionInfo> userPerms) {
        UserPermissionInfo userPermissionInfo = userPerms.get(0);
        this.setId(userPermissionInfo.getId());
        this.setUsername(userPermissionInfo.getUserName());
        this.setPassword(userPermissionInfo.getUserPassword());
        this.setEnabled(userPermissionInfo.getIsDelete() == 0);
        Set<SimpleGrantedAuthority> permissions = userPerms.stream()
                .map(UserPermissionInfo::getPermission)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
        this.authorities= ListUtil.toList(permissions);

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
