dependencies {
    optional(project(":arthur-plugin"))

    optional("com.alibaba.csp:sentinel-datasource-nacos")
    optional(project(":arthur-starter:arthur-sentinel-starter"))
    optional("com.alibaba.cloud:spring-cloud-starter-alibaba-sentinel")
    optional("com.alibaba.cloud:spring-cloud-alibaba-sentinel-gateway")

    optional(project(":arthur-starter:arthur-boot-starter"))
    optional(project(":arthur-starter:arthur-cloud-starter"))
    optional("org.springframework.cloud:spring-cloud-gateway-server")
    optional("org.springframework.boot:spring-boot-starter-data-redis-reactive")

//    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
}
