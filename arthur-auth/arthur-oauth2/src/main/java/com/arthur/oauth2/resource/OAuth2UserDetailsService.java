package com.arthur.oauth2.resource;

import com.arthur.auth.user.domain.entity.UserDetail;
import com.arthur.auth.user.domain.entity.UserInfo;
import com.arthur.oauth2.constant.SecurityConstants;
import org.springframework.core.Ordered;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 拓展{@link UserDetailsService}功能设计抽象基类
 *
 * @author DearYang
 * @date 2022-08-14
 * @since 1.0
 */
public interface OAuth2UserDetailsService extends UserDetailsService, Ordered {

	/**
	 * 是否支持此客户端校验
	 *
	 * @param clientId  客户端ID
	 * @param grantType 授权类型
	 * @return yes or no
	 */
	default boolean supports(String clientId, String grantType) {
		return true;
	}

	/**
	 * 获取用户信息
	 *
	 * @return {@link UserDetails}
	 */
	default UserDetails getUserDetails(UserInfo userInfo) {
		Long[] roles = userInfo.getRoles();
		Set<String> authorities = new HashSet<>();
		if (!ObjectUtils.isEmpty(roles)) {
			for (Long role : roles) {
				authorities.add(SecurityConstants.ROLE + role);
			}
		}

		String[] permissions = userInfo.getPermissions();
		if (!ObjectUtils.isEmpty(permissions)) {
			CollectionUtils.mergeArrayIntoCollection(permissions, authorities);
		}

		UserDetail user = userInfo.getUser();
		// TODO：密码待加特征码，比如：{bcrypt}
		List<GrantedAuthority> grantedAuthorities = AuthorityUtils.createAuthorityList(authorities.toArray(new String[0]));
		return new OAuth2User(user.getUserId(), user.getDeptId(), user.getPhone(),
			user.getUsername(), user.getPassword(), true, true,
			true, !user.getLocked(), grantedAuthorities);
	}

	/**
	 * 加载用户
	 *
	 * @return {@link UserDetails}
	 */
	default UserDetails loadUserByClientUser(OAuth2User user) {
		return this.loadUserByUsername(user.getName());
	}

	@Override
	default int getOrder() {
		return 0;
	}

}
