package com.arthur.auth.user.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户岗位关联表
 */
@Data
@TableName(value = "sys_user_post")
public class SysUserPost implements Serializable {

	@Serial
	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	/**
	 * 用户ID
	 */
	@TableField(value = "user_id")
	private Long userId;

	/**
	 * 岗位ID
	 */
	@TableField(value = "post_id")
	private Long postId;

}
