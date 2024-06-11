dependencies {
    // 基础组件
    optional("org.owasp.antisamy:antisamy")
    optional("org.apache.commons:commons-text")

    // Web
    optional("io.projectreactor:reactor-core")
    optional("jakarta.servlet:jakarta.servlet-api")

    optional("org.springframework:spring-webmvc")
    optional("org.springframework:spring-webflux")
    optional(project(":arthur-starter:arthur-boot-starter"))
    optional("org.springframework.boot:spring-boot-starter")
    optional(project(":arthur-starter:arthur-web-starter:arthur-webmvc-starter"))
    optional(project(":arthur-starter:arthur-web-starter:arthur-webflux-starter"))
}
