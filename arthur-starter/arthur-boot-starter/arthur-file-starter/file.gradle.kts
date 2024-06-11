plugins {
    id("common.spotbugs")
}

dependencies {
    // 基础组件
    optional("com.alibaba:easyexcel")
    optional(project(":arthur-commons"))
    optional("jakarta.servlet:jakarta.servlet-api")
    optional("jakarta.validation:jakarta.validation-api")

    // Web
    optional("org.springframework:spring-webmvc")
    optional("org.springframework.boot:spring-boot-starter")
    optional(project(":arthur-starter:arthur-boot-starter"))
    optional(project(":arthur-starter:arthur-web-starter:arthur-webmvc-starter"))

    // AnnotationProcessor
//    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
}
