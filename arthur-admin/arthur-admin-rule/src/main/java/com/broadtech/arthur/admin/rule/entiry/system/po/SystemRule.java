package com.broadtech.arthur.admin.rule.entiry.system.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName system_rule
 */
@TableName(value ="system_rule")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SystemRule  {
    /**
     * 
     */
    @TableId
    private String id;
    private String groupId;

    /**
     * qps
     */
    private Double qps;

    /**
     * 
     */
    private Long maxThread;

    /**
     * 
     */
    private Long avgRt;

    /**
     * 系统load阈值
     */
    private Double highestSystemLoad;

    /**
     * CPU 使用率
     */
    private Double highestCpuUsage;

    /**
     * 
     */
    private Date updateTime;

    /**
     * 
     */
    private Date createTime;

    private String resource;
    private String limitApp;


}