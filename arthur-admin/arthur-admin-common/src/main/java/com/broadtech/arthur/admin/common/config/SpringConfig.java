package com.broadtech.arthur.admin.common.config;


import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/8/22
 */
@EnableConfigurationProperties(value = {MetaConfigProperties.class,CacheConfigProperties.class})
@Configuration
public class SpringConfig {

}
