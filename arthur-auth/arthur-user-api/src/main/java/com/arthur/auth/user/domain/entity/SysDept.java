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
 * 部门表
 */
@Data
@TableName(value ="sys_dept")
@EqualsAndHashCode(callSuper = true)
public class SysDept extends BaseEntity implements Serializable {

	@Serial
	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	/**
     * 部门id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 父部门id
     */
    @TableField(value = "parent_id")
    private Long parentId;

    /**
     * 祖级列表
     */
    @TableField(value = "ancestors")
    private String ancestors;

    /**
     * 部门名称
     */
    @TableField(value = "dept_name")
    private String deptName;

    /**
     * 显示顺序
     */
    @TableField(value = "order_num")
    private Integer orderNum;

    /**
     * 负责人
     */
    @TableField(value = "leader")
    private String leader;

    /**
     * 联系电话
     */
    @TableField(value = "phone")
    private String phone;

    /**
     * 邮箱
     */
    @TableField(value = "email")
    private String email;

    /**
     * 部门状态: 0-删除,1-停用,2-正常
     */
    @TableField(value = "status")
    private Integer status;

}
