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
public class RedirectToFilterPojo {
    private final String name = "RedirectTo";
    private String status;
    private String url;
}
