package com.arthur.auth.user.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 角色菜单关联表
 */
@Data
@TableName(value = "sys_role_menu")
public class SysRoleMenu implements Serializable {

	@Serial
	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	/**
	 * 角色ID
	 */
	@TableField(value = "role_id")
	private Long roleId;

	/**
	 * 菜单ID
	 */
	@TableField(value = "menu_id")
	private Long menuId;

}
