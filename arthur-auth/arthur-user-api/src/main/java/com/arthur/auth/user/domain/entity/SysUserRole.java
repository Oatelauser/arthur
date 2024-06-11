package com.arthur.auth.user.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户角色关联表
 */
@Data
@TableName(value = "sys_user_role")
public class SysUserRole implements Serializable {

	@Serial
	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	/**
	 * 用户ID
	 */
	@TableField(value = "user_id")
	private Long userId;

	/**
	 * 角色ID
	 */
	@TableField(value = "role_id")
	private Long roleId;

}
