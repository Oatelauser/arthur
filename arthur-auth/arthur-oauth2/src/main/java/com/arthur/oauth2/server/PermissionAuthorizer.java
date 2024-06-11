package com.arthur.oauth2.server;

import com.arthur.oauth2.utils.SecurityUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.ObjectUtils;
import org.springframework.util.PatternMatchUtils;
import org.springframework.util.StringUtils;

/**
 * 权限判断器
 * <p>
 * 示例：
 * {@code @PreAuthorize("@pms.hasPermission('sys_log_del')")}
 *
 * @author DearYang
 * @date 2022-09-05
 * @see org.springframework.security.access.prepost.PreAuthorize
 * @since 1.0
 */
@SuppressWarnings("all")
public class PermissionAuthorizer {

	/**
	 * 重复代码，减少数组对象的创建，提高性能
	 *
	 * @see PermissionAuthorizer#hasPermission(String...)
	 */
	public boolean hasPermission(String permission) {
		if (ObjectUtils.isEmpty(permission)) {
			return false;
		}

		Authentication authentication = SecurityUtils.getAuthentication();
		if (authentication == null) {
			return false;
		}

		for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
			String authority = grantedAuthority.getAuthority();
			if (StringUtils.hasText(authority) && PatternMatchUtils.simpleMatch(permission, authority)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 判断权限
	 *
	 * @param permission 权限标识
	 * @return yes or no
	 */
	public boolean hasPermission(String... permissions) {
		if (ObjectUtils.isEmpty(permissions)) {
			return false;
		}

		Authentication authentication = SecurityUtils.getAuthentication();
		if (authentication == null) {
			return false;
		}

		for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
			String authority = grantedAuthority.getAuthority();
			if (StringUtils.hasText(authority) && PatternMatchUtils.simpleMatch(permissions, authority)) {
				return true;
			}
		}

		return false;
	}

}
