package com.broadtech.arthur.admin.route.entity.po.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

/**
 * @Author Machenike
 * @Date 2022/7/19
 * @Version 1.0.0
 * @Description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RewriteLocationResponseHeaderFilterPojo {
    private final String name = "RewriteLocationResponseHeader";
    private String stripVersion;
    private String locationHeaderName;
    private String hostValue;
    private String protocols;
    private Pattern hostPortPattern;
    private Pattern hostPortVersionPattern;
}
