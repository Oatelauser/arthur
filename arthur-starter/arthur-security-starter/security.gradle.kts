dependencies {
    optional("jakarta.servlet:jakarta.servlet-api")
    optional("com.alibaba:transmittable-thread-local")

    optional(project(":arthur-starter:arthur-boot-starter"))
    optional("org.springframework.boot:spring-boot-starter")
    optional("org.springframework.boot:spring-boot-starter-security")
}
