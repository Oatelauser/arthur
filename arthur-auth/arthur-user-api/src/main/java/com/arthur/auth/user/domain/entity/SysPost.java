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
 * 岗位信息表
 */
@Data
@TableName(value = "sys_post")
@EqualsAndHashCode(callSuper = true)
public class SysPost extends BaseEntity implements Serializable {

	@Serial
	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	/**
	 * 岗位ID
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;

	/**
	 * 岗位编码
	 */
	@TableField(value = "post_code")
	private String postCode;

	/**
	 * 岗位名称
	 */
	@TableField(value = "post_name")
	private String postName;

	/**
	 * 显示顺序
	 */
	@TableField(value = "order_num")
	private Integer orderNum;

	/**
	 * 状态:0-停用,1-正常
	 */
	@TableField(value = "status")
	private Boolean status;

	/**
	 * 备注
	 */
	@TableField(value = "remark")
	private String remark;

}
