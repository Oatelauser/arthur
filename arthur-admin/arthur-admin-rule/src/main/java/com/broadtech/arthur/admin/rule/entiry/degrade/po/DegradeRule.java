package com.broadtech.arthur.admin.rule.entiry.degrade.po;

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
 * @TableName degrade_rule
 */
@TableName(value ="degrade_rule")
@Data
public class DegradeRule  {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private String id;
    private String groupId;

    /**
     * 策略
     */
    private Integer grade;

    /**
     * 慢调用比例模式下为慢调用临界 RT（超出该值计为慢调用）；异常比例/异常数模式下为对应的阈值
     */
    private Double count;

    /**
     * 熔断时长
     */
    private Integer timeWindow;

    /**
     * 统计时长
     */
    private Integer statIntervalMs;

    /**
     * 慢调用比例阈值，仅慢调用比例模式有效
     */
    private Double slowRatioThreshold;

    /**
     * 熔断触发的最小请求数
     */
    private Integer minRequestAmount;

    /**
     * 
     */
    private Date createTime;

    /**
     * CURRENT_TIMESTAMP()
     */
    private Date updateTime;

    private String resource;
    private String limitApp;

    /**
     * @author Machenike
     * @version 1.0.0
     * @date 2022/8/15
     */

}