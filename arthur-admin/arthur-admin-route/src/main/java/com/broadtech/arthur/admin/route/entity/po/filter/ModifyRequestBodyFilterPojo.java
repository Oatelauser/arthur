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
public class ModifyRequestBodyFilterPojo {
    private final  String name ="ModifyRequestBody";
    private Class inClass;
    private Class outClass;
    private String contentType;
    private String rewriteFunction;
}
