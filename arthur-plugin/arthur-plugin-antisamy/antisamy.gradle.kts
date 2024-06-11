dependencies {
    implementation("org.owasp.antisamy:antisamy")
    implementation("org.apache.commons:commons-text")
    implementation(project(":arthur-starter:arthur-boot-starter"))
    implementation(project(":arthur-starter:arthur-web-starter:arthur-antisamy-starter"))

    optional("org.springframework:spring-webflux")
    optional(project(":arthur-starter:arthur-cloud-starter"))
    optional("org.springframework.cloud:spring-cloud-gateway-server")
    optional(project(":arthur-starter:arthur-web-starter:arthur-webflux-starter"))
}
