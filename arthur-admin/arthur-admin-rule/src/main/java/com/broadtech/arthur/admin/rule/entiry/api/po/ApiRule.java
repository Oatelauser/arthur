package com.broadtech.arthur.admin.rule.entiry.api.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author Machenike
 * @TableName api
 */
@TableName(value = "api")
@Data
public class ApiRule {
    /**
     *
     */
    @TableId
    private String id;

    private String groupId;
    /**
     * api名字
     */
    private String apiName;

    /**
     *  Set<ApiPredicateItem> predicateItems
     */
    /**
     * api定义
     */
    private String apiPredicateItem;

}