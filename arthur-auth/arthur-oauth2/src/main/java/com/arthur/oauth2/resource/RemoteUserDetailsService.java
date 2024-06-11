package com.arthur.oauth2.resource;

import com.arthur.auth.user.api.RemoteUserDetailService;
import com.arthur.auth.user.domain.entity.UserDetail;
import com.arthur.auth.user.domain.entity.UserInfo;
import com.arthur.oauth2.cache.OAuth2CacheManager;
import com.arthur.oauth2.constant.CacheConstants;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Objects;

/**
 * 用户详细信息
 *
 * @author DearYang
 * @date 2022-09-05
 * @since 1.0
 */
public class RemoteUserDetailsService implements OAuth2UserDetailsService {

	private final Cache userDetailsCache;
	private final RemoteUserDetailService userDetailService;

	public RemoteUserDetailsService(OAuth2CacheManager cacheManager, RemoteUserDetailService userDetailService) {
		this.userDetailService = userDetailService;
		this.userDetailsCache = Objects.requireNonNull(cacheManager.getCache(CacheConstants.CACHE_USER_DETAILS_PARAMETER));
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Cache.ValueWrapper wrapper = userDetailsCache.get(username, () -> {
			// TODO:开发测试造数，生产环境删除
			//GenericServerResponse<UserInfo> serverResponse = userDetailService.getUserInfo(username, INTERNAL_AUTH_HEADER_VALUE);
			//UserInfo userInfo = serverResponse.getData();
			UserInfo userInfo = new UserInfo();
			userInfo.setRoles(new Long[]{ 1L, 2L });
			userInfo.setPosts(new Long[]{ 1L });
			UserDetail userDetail = new UserDetail();
			userDetail.setUserId(1L);
			userDetail.setUsername("admin");
			userDetail.setPassword("{noop}123456");
			userDetail.setDeptId(1L);
			userDetail.setPhone("13368074573");
			userInfo.setUser(userDetail);

			if (userInfo == null) {
				throw new UsernameNotFoundException("user [" + username + "] not found");
			}

			return new SimpleValueWrapper(this.getUserDetails(userInfo));
		});

		if (wrapper == null) {
			throw new UsernameNotFoundException("user [" + username + "] not found");
		}
		OAuth2User user = (OAuth2User) wrapper.get();
		return Objects.requireNonNull(user).clone();
	}

	@Override
	public int getOrder() {
		return OAuth2UserDetailsService.super.getOrder() - 1;
	}

}
