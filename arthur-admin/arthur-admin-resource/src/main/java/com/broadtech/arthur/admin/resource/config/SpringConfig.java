package com.broadtech.arthur.admin.resource.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/9/15
 */
@Configuration
@EnableConfigurationProperties(value = {IgnoreUrlsConfig.class})
public class SpringConfig {



}
