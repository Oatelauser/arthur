plugins {
    id("common.spotbugs")
}

dependencies {
    // 项目依赖
    optional(project(":arthur-auth:arthur-user-api"))
    optional(project(":arthur-commons:arthur-common"))
    optional(project(":arthur-starter:arthur-boot-starter"))
    optional(project(":arthur-starter:arthur-security-starter"))
    optional(project(":arthur-starter:arthur-boot-starter:arthur-mybatisplus-starter"))

    // 基础组件
	optional("com.stoyanr:evictor")
    optional("io.github.openfeign:feign-core")
//    optional("javax.servlet:javax.servlet-api")
    optional("jakarta.servlet:jakarta.servlet-api")
    optional("com.github.ben-manes.caffeine:caffeine")

    // 微服务
    optional("org.springframework:spring-jdbc")
    optional("org.springframework:spring-webmvc")
    optional(project(":arthur-starter:arthur-web-starter"))
    optional("org.springframework.cloud:spring-cloud-commons")
    optional("org.springframework.boot:spring-boot-starter-data-redis")
    optional("org.springframework.security:spring-security-oauth2-jose")
    optional("org.springframework.security:spring-security-oauth2-authorization-server")

    // AnnotationProcessor
//    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
}
