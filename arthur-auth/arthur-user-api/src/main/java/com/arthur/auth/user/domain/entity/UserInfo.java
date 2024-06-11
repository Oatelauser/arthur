package com.arthur.auth.user.domain.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户信息
 *
 * @author DearYang
 * @date 2022-08-31
 * @since 1.0
 */
@Data
public class UserInfo implements Serializable {

	@Serial
	private static final long serialVersionUID = -3310722712466485333L;

	/**
	 *用户基本信息
	 */
	private UserDetail user;

	/**
	 * 权限标识集合
	 */
	private String[] permissions;

	/**
	 * 角色
	 */
	private Long[] roles;

	/**
	 * 岗位
	 */
	private Long[] posts;

}
