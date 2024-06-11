plugins {
    id("common.spotbugs")
}

dependencies {
    // 项目依赖
    optional(project(":arthur-commons:arthur-common"))

    // 基础组件
//    optional("javax.servlet:javax.servlet-api")
    optional("jakarta.servlet:jakarta.servlet-api")
    optional("com.fasterxml.jackson.core:jackson-databind")

    // web
    optional("org.springframework:spring-webmvc")
    optional("org.springframework:spring-webflux")
    optional("org.springframework.boot:spring-boot-starter")
    optional(project(":arthur-starter:arthur-boot-starter"))

    // 微服务
    optional(project(":arthur-starter:arthur-sentinel-starter"))
    optional("com.alibaba.cloud:spring-cloud-starter-alibaba-sentinel")
    optional("org.springframework.cloud:spring-cloud-starter-openfeign")

    testImplementation(project(":arthur-starter:arthur-boot-starter:arthur-test-starter"))
}
