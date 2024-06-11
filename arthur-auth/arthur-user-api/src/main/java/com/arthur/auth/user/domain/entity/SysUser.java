package com.arthur.auth.user.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户信息表
 */
@Data
@TableName(value = "sys_user")
@EqualsAndHashCode(callSuper = true)
public class SysUser extends BaseEntity implements Serializable {

	@Serial
	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	/**
	 * 用户ID
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;

	/**
	 * 部门ID
	 */
	@TableField(value = "dept_id")
	private Long deptId;

	/**
	 * 登录账号
	 */
	@TableField(value = "username")
	private String username;

	/**
	 * 用户昵称
	 */
	@TableField(value = "nickname")
	private String nickname;

	/**
	 * 用户邮箱
	 */
	@TableField(value = "email")
	private String email;

	/**
	 * 手机号码
	 */
	@TableField(value = "phone")
	private String phone;

	/**
	 * 性别: 0-男,1-女,2-未知
	 */
	@TableField(value = "sex")
	private Integer sex;

	/**
	 * 头像路径
	 */
	@TableField(value = "avatar")
	private String avatar;

	/**
	 * 密码
	 */
	@TableField(value = "password")
	private String password;

	/**
	 * 盐加密
	 */
	@TableField(value = "salt")
	private String salt;

	/**
	 * 帐号状态: 0-删除,1-停用,2-正常
	 */
	@TableField(value = "status")
	private Integer status;

	/**
	 * 备注
	 */
	@TableField(value = "remark")
	private String remark;

}
