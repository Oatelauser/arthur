package com.broadtech.arthur.admin.register.infrastructure.db.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 服务注册表
 *
 * @author Machenike
 * @TableName server_register
 */
@TableName(value = "server_register")
@Data
public class ServerRegisterPo implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     *
     */
    private String serverName;

    /**
     *
     */
    private String groupName;

    /**
     *
     */
    private String ip;

    /**
     *
     */
    private Integer port;

    /**
     *
     */
    private String clusterName;

    /**
     *
     */
    private Date createAt;

    /**
     *
     */
    private Date    updateAt;
    /**
     *
     */
    private Integer action;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


}