package com.arthur.oauth2.utils;

import com.arthur.oauth2.constant.SecurityConstants;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * security工具类
 *
 * @author DearYang
 * @date 2022-08-14
 * @since 1.0
 */
public abstract class SecurityUtils {

    /**
     * 获取当前Authentication
     *
     * @return {@link Authentication}
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 获取用户角色集合
     *
     * @return 角色集合
     */
    public static List<Long> getRoles() {
        Authentication authentication = getAuthentication();

        List<Long> roleIds = new ArrayList<>();
        for (GrantedAuthority granted : authentication.getAuthorities()) {
            String authority = granted.getAuthority();
            if (!StringUtils.hasText(authority)) {
                continue;
            }
            if (!authority.startsWith(SecurityConstants.ROLE) || SecurityConstants.ROLE.equals(authority)) {
                continue;
            }

            String roleId = authority.substring(SecurityConstants.ROLE.length());
            roleIds.add(Long.parseLong(roleId));
        }

        return roleIds;
    }


}
