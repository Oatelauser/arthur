dependencies {
    optional(project(":arthur-starter:arthur-boot-starter"))
    optional("org.springframework.boot:spring-boot-starter")

    api(project(":arthur-starter:arthur-security-starter"))
    optional("org.springframework.boot:spring-boot-starter-security")
    optional("org.springframework.security:spring-security-oauth2-jose")
    optional("org.springframework.security:spring-security-oauth2-resource-server")
}
