package com.arthur.auth.uaa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/**
 * 认证授权中心
 *
 * @author DearYang
 * @date 2022-08-14
 * @since 1.0
 */
@EnableWebSecurity
@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients(basePackages = "com.arthur.auth.user.api")
public class ArthurAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArthurAuthApplication.class, args);
    }

}
