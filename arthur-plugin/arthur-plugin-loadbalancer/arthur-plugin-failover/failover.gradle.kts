dependencies {
    optional("org.jctools:jctools-core")
    optional("com.github.ben-manes.caffeine:caffeine")

    optional(project(":arthur-plugin"))
    optional(project(":arthur-plugin:arthur-plugin-route"))
    optional(project(":arthur-starter:arthur-boot-starter"))

    optional(project(":arthur-plugin:arthur-plugin-loadbalancer"))
    optional("org.springframework.cloud:spring-cloud-starter-loadbalancer")
}
