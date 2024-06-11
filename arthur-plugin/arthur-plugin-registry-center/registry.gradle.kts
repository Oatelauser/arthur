dependencies {
    optional("org.jctools:jctools-core")
    optional(project(":arthur-commons:arthur-common"))
    optional(project(":arthur-middleware:arthur-nacos-client"))

    optional("org.springframework.boot:spring-boot-starter")
    optional(project(":arthur-starter:arthur-boot-starter"))
    optional("org.springframework.cloud:spring-cloud-commons")
    optional("org.springframework.cloud:spring-cloud-context")
    optional("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-discovery")
}
