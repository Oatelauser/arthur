package com.broadtech.arthur.admin.resource.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/9/16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ConfigurationProperties(prefix="secure.ignore")
public class IgnoreUrlsConfig {
    private List<String> urls;
}
