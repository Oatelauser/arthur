dependencies {
    optional("io.projectreactor.netty:reactor-netty-http")

    optional(project(":arthur-plugin"))
    optional(project(":arthur-starter:arthur-boot-starter"))
    optional(project(":arthur-starter:arthur-cloud-starter"))
    optional("org.springframework.cloud:spring-cloud-gateway-server")
}
