package com.broadtech.arthur.admin.resource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/9/9
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ResourceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ResourceApplication.class, args);
    }
}
