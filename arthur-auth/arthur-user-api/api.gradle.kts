dependencies {
    // 项目依赖
    optional(project(":arthur-commons:arthur-common"))
    optional(project(":arthur-starter:arthur-web-starter:arthur-webmvc-starter"))
    optional(project(":arthur-starter:arthur-cloud-starter:arthur-openfeign-starter"))
    optional(project(":arthur-starter:arthur-boot-starter:arthur-mybatisplus-starter"))

    // 基础组件
    optional("com.baomidou:mybatis-plus-annotation")
    optional("io.swagger.core.v3:swagger-annotations")
    optional("jakarta.validation:jakarta.validation-api")
    optional("org.springframework.cloud:spring-cloud-openfeign-core")

    // AnnotationProcessor
    annotationProcessor("org.projectlombok:lombok")
}
