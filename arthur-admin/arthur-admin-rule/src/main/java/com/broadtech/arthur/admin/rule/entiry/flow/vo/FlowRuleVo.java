package com.broadtech.arthur.admin.rule.entiry.flow.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName gateway_flow_rule
 */

@Data
public class FlowRuleVo {
    /**
     * 
     */

    private String id;

    /**
     * 资源名称
     */
    private String resource;

    /**
     * 规则是针对 API Gateway 的 route（RESOURCE_MODE_ROUTE_ID）还是用户在 Sentinel 中定义的 API 分组（RESOURCE_MODE_CUSTOM_API_NAME），默认是 route
     */
    private Integer resourceMode;

    /**
     * 限流指标维度
     */
    private Integer grade;

    /**
     * 限流阈值
     */
    private Double count;

    /**
     * 统计时间窗口，单位是秒，默认是 1 秒
     */
    private Long intervalSec;

    /**
     * 流量整形的控制效果,目前支持快速失败和匀速排队两种模式
     */
    private Integer controlBehavior;

    /**
     * 应对突发请求时额外允许的请求数目
     */
    private Integer burst;

    /**
     * 匀速排队模式下的最长排队时间
     */
    private Integer maxQueueingTimeoutMs;

    /**
     * 
     */
    private Date createTime;

    /**
     * 
     */
    private Date updateTime;



    //item

    private String flowRuleId;
    /**
     *
     */
    private Integer indexes;

    /**
     *
     */
    private Integer parseStrategy;

    /**
     *
     */
    private String fieldName;

    /**
     *
     */
    private String pattern;

    /**
     *
     */
    private Integer matchStrategy;






}