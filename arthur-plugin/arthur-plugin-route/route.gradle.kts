dependencies {
    optional("org.springframework.cloud:spring-cloud-gateway-server")

    optional(project(":arthur-plugin"))
    optional(project(":arthur-starter:arthur-boot-starter"))
    optional(project(":arthur-starter:arthur-boot-starter:arthur-event-starter"))

//    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
}
