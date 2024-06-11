dependencies {
    optional("org.slf4j:slf4j-api")
    api(project(":arthur-commons:arthur-common"))

    api("org.springframework:spring-webflux")
    optional("org.springframework.boot:spring-boot-starter")
    optional(project(":arthur-starter:arthur-boot-starter"))
    optional("org.springframework.cloud:spring-cloud-gateway-server")
    api(project(":arthur-starter:arthur-web-starter:arthur-webflux-starter"))
}
