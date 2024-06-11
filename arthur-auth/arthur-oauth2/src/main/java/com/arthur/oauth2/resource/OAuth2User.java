package com.arthur.oauth2.resource;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;

import java.io.Serial;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 拓展{@link UserDetails}用户信息
 *
 * @author DearYang
 * @date 2022-08-23
 * @since 1.0
 */
@SuppressWarnings("unused")
public class OAuth2User extends User implements OAuth2AuthenticatedPrincipal, Cloneable {

	@Serial
	private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

	/**
	 * 用户ID
	 */
	private final Long id;

	/**
	 * 部门ID
	 */
	private final Long deptId;

	/**
	 * 手机号
	 */
	private final String phone;


	public OAuth2User(Long id, Long deptId, String phone,
			String username, String password, boolean enabled,
			boolean accountNonExpired, boolean credentialsNonExpired,
			boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		this.id = id;
		this.deptId = deptId;
		this.phone = phone;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return new HashMap<>();
	}

	@Override
	public String getName() {
		return this.getUsername();
	}

	public Long getId() {
		return id;
	}

	public Long getDeptId() {
		return deptId;
	}

	public String getPhone() {
		return phone;
	}

	@Override
	public OAuth2User clone()  {
		try {
			return (OAuth2User) super.clone();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String toString() {
		return "ClientUser{" +
			"id=" + id +
			", deptId=" + deptId +
			", phone='" + phone + '\'' +
			"} " + super.toString();
	}

}
