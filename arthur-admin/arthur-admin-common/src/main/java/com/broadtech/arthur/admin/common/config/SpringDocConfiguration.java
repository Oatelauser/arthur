package com.broadtech.arthur.admin.common.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * SpringDoc API文档相关配置
 * Created by macro on 2022/3/4.
 *
 * @author Dear Yang
 * @date 2022-07-14
 * @since 1.0
 */
@Configuration(proxyBeanMethods = false)
public class SpringDocConfiguration {

    @Bean
    public OpenAPI arthurAdminOpenApi() {
        Contact contact = new Contact();
        contact.setName("Dear Yang");
        contact.setEmail("545896770@qq.com");
        return new OpenAPI().info(new Info()
                .title("Arthur Admin")
                .description("博尔德API网关后管系统REST API接口文档")
                .version("version 1.0.0")
                .contact(contact)
        );
    }

}
