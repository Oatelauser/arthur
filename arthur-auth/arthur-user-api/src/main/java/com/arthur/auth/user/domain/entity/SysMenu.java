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
 * 菜单权限表
 */
@Data
@TableName(value ="sys_menu")
@EqualsAndHashCode(callSuper = true)
public class SysMenu extends BaseEntity implements Serializable {

	@Serial
	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	/**
     * 菜单ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 菜单名称
     */
    @TableField(value = "menu_name")
    private String menuName;

    /**
     * 父菜单ID
     */
    @TableField(value = "parent_id")
    private Long parentId;

    /**
     * 显示顺序
     */
    @TableField(value = "order_num")
    private Integer orderNum;

    /**
     * 请求地址
     */
    @TableField(value = "url")
    private String url;

    /**
     * 打开方式: 1-页签 2-新窗口
     */
    @TableField(value = "open_type")
    private Integer openType;

    /**
     * 菜单类型: 1-目录 2-菜单 3-按钮）
     */
    @TableField(value = "menu_type")
    private Integer menuType;

    /**
     * 菜单状态: 0-隐藏 1-显示
     */
    @TableField(value = "visible")
    private Boolean visible;

    /**
     * 是否刷新: 0-不刷新 1-刷新
     */
    @TableField(value = "is_refresh")
    private Boolean isRefresh;

    /**
     * 权限标识
     */
    @TableField(value = "perms")
    private String perms;

    /**
     * 菜单图标
     */
    @TableField(value = "icon")
    private String icon;

}
