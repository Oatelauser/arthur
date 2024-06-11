@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    application
    id("common.spotbugs")
    alias(libs.plugins.springboot)
}

description = "认证授权中心"

dependencies {
    // 项目依赖
    implementation(project(":arthur-commons:arthur-common"))
    implementation(project(":arthur-auth:arthur-user-api"))

    // 基础组件
    implementation("com.lmax:disruptor")
    implementation("org.apache.httpcomponents:httpclient")
    implementation("com.github.ben-manes.caffeine:caffeine")

    // Web
    implementation("org.springdoc:springdoc-openapi-ui")
    implementation(project(":arthur-starter:arthur-boot-starter"))
    implementation("org.springframework.boot:spring-boot-starter-undertow")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation(project(":arthur-starter:arthur-web-starter:arthur-webmvc-starter"))
    implementation("org.springframework.boot:spring-boot-starter-web") {
        exclude("org.apache.tomcat", "tomcat-jdbc")
        exclude("org.springframework.boot", "spring-boot-starter-tomcat")
    }

    // Mybatis
    // implementation("com.baomidou:mybatis-plus-boot-starter")
    // implementation(project(":arthur-starter:arthur-boot-starter:arthur-mybatisplus-starter"))

    // 数据源
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    // OAuth2
    implementation(project(":arthur-auth:arthur-oauth2"))
    implementation(project(":arthur-starter:arthur-security-starter"))
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.security:spring-security-oauth2-jose")
    implementation("org.springframework.security:spring-security-oauth2-authorization-server")

    // OpenFeign
    implementation("io.github.openfeign:feign-httpclient")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
    implementation(project(":arthur-starter:arthur-cloud-starter:arthur-openfeign-starter"))

    // 监控
    implementation("io.micrometer:micrometer-registry-prometheus")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    // ================ 微服务 ==============
    implementation(project(":arthur-starter:arthur-cloud-starter"))
    implementation("org.springframework.cloud:spring-cloud-starter-loadbalancer")
//    implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-config")
    implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-discovery")

    // sentinel
//    implementation(project(":arthur-starter:arthur-sentinel-starter"))
//    implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-sentinel")

    // 日志
    implementation("org.springframework.boot:spring-boot-starter-log4j2")
    implementation(project(":arthur-starter:arthur-boot-starter:arthur-log4j2-starter"))
}

configurations.all {
    exclude("org.springframework.boot", "spring-boot-starter-logging")
}

application {
    mainClass.set("com.broadtech.arthur.auth.ArthurAuthApplication")
}
