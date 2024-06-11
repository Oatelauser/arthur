@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    application
    alias(libs.plugins.springboot)
}

description = "用户权限管理系统"

dependencies {
    // 基础组件
    implementation("com.lmax:disruptor")
    implementation("com.alibaba:easyexcel")
    implementation("org.mapstruct:mapstruct")
	implementation(project(":arthur-commons"))
	implementation(project(":arthur-commons:arthur-common"))
    implementation(project(":arthur-auth:arthur-user-api"))

    // 数据库
    implementation("com.mysql:mysql-connector-j")
    implementation("com.alibaba:druid-spring-boot-starter")

    // Mybatis
    implementation("com.baomidou:mybatis-plus-boot-starter")
    implementation(project(":arthur-starter:arthur-boot-starter:arthur-mybatisplus-starter"))

    // Web
    implementation(project(":arthur-starter:arthur-boot-starter"))
    implementation(project(":arthur-starter:arthur-boot-starter:arthur-file-starter"))
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-undertow")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("com.github.xiaoymin:knife4j-openapi3-jakarta-spring-boot-starter")
    implementation(project(":arthur-starter:arthur-web-starter:arthur-webmvc-starter"))
    implementation("org.springframework.boot:spring-boot-starter-web") {
        exclude("org.apache.tomcat", "tomcat-jdbc")
        exclude("org.springframework.boot", "spring-boot-starter-tomcat")
    }


    // ================ 微服务 ==============
    implementation(project(":arthur-starter:arthur-cloud-starter"))
    implementation("org.springframework.cloud:spring-cloud-starter-loadbalancer")
//    implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-config")
    implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-discovery")

    // 日志
    implementation("org.springframework.boot:spring-boot-starter-log4j2")
    implementation(project(":arthur-starter:arthur-boot-starter:arthur-log4j2-starter"))

    // AnnotationProcessor
    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor(libs.lombokMapstructBinding)
    annotationProcessor(libs.mapstructProcessor)
}

configurations.all {
    exclude("org.springframework.boot", "spring-boot-starter-logging")
}

application {
    mainClass.set("com.arthur.auth.upms.ArthurUserApplication")
}