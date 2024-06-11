dependencies {
    // 项目依赖
    optional(project(":arthur-commons:arthur-common"))

    // web
    optional("org.springframework.boot:spring-boot-starter")
    optional(project(":arthur-starter:arthur-boot-starter"))

    // AnnotationProcessor
//    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
}
