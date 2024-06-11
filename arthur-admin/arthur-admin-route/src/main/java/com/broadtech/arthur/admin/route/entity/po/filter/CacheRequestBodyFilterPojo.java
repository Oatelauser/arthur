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
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CacheRequestBodyFilterPojo {
    private String name= "CacheRequestBody";
    private Class<?> bodyClass;
}
