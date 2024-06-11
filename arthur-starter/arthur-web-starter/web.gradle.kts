dependencies {
    // 基础组件
    optional(project(":arthur-commons:arthur-common"))

    optional("io.swagger.core.v3:swagger-annotations")
    optional("jakarta.validation:jakarta.validation-api")
    optional("org.hibernate.validator:hibernate-validator")

    // Web
    optional("io.undertow:undertow-core")
    optional("org.springframework:spring-web")
    optional(project(":arthur-starter:arthur-boot-starter"))
    optional("org.springframework.boot:spring-boot-starter")
    optional("org.springframework.boot:spring-boot-starter-json")
}
