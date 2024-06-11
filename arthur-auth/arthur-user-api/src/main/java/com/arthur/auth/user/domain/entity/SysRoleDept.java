package com.arthur.auth.user.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 角色部门关联表
 */
@Data
@TableName(value = "sys_role_dept")
public class SysRoleDept implements Serializable {

	@Serial
	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	/**
	 * 角色ID
	 */
	@TableField(value = "role_id")
	private Long roleId;

	/**
	 * 部门ID
	 */
	@TableField(value = "dept_id")
	private Long deptId;

}
