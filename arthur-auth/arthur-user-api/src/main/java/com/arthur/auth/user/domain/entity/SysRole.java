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
 * 角色信息表
 */
@Data
@TableName(value = "sys_role")
@EqualsAndHashCode(callSuper = true)
public class SysRole extends BaseEntity implements Serializable {

	@Serial
	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	/**
	 * 角色ID
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;

	/**
	 * 角色名称
	 */
	@TableField(value = "name")
	private String name;

	/**
	 * 数据范围: 1-全部数据权限,2-自定数据权限,3-本部门数据权限,4-本部门及以下数据权限
	 */
	@TableField(value = "data_scope")
	private Integer dataScope;

	/**
	 * 角色状态: 2-正常,1-冻结,0-删除
	 */
	@TableField(value = "status")
	private Integer status;

	/**
	 * 显示顺序
	 */
	@TableField(value = "order_num")
	private Integer orderNum;

	/**
	 * 备注
	 */
	@TableField(value = "remark")
	private String remark;

}
