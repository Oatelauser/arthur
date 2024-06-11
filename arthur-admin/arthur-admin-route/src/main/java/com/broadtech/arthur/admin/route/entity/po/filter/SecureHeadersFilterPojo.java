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
public class SecureHeadersFilterPojo {
    private final String name = "SecureHeaders";
    private String xssProtectionHeader;
    private String strictTransportSecurity;
    private String frameOptions;
    private String contentTypeOptions;
    private String referrerPolicy;
    private String contentSecurityPolicy;
    private String downloadOptions;
    private String permittedCrossDomainPolicies;
}
