package com.broadtech.arthur.admin.route.entity.po.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author Machenike
 * @Date 2022/7/19
 * @Version 1.0.0
 * @Description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DedupeResponseHeaderFilterPojo {
    private final String name ="DedupeResponseHeader";
    /***
     *         RETAIN_FIRST,
     *         RETAIN_LAST,
     *         RETAIN_UNIQUE;
     */
    private String strategy;

}
