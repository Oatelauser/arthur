dependencies {
	optional(project(":arthur-commons:arthur-common"))

    optional("org.springframework.boot:spring-boot-starter")
    optional(project(":arthur-starter:arthur-boot-starter"))
    optional("org.springframework.cloud:spring-cloud-commons")
    optional("org.springframework.cloud:spring-cloud-context")

    optional(project(":arthur-middleware:arthur-nacos-client"))
    optional("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-config")
    optional("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-discovery")
}
