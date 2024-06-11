dependencies {
    optional("org.jctools:jctools-core")
    optional("org.codehaus.janino:janino")
    optional("com.alibaba.csp:sentinel-core")
    optional("com.github.ben-manes.caffeine:caffeine")
	optional(project(":arthur-commons:arthur-common"))

	optional(project(":arthur-plugin"))
	optional(project(":arthur-plugin:arthur-plugin-route"))
	optional(project(":arthur-starter:arthur-boot-starter"))
	optional(project(":arthur-starter:arthur-sentinel-starter"))

    optional("io.projectreactor.netty:reactor-netty-http")
	optional(project(":arthur-starter:arthur-cloud-starter"))
    optional("org.springframework.cloud:spring-cloud-sleuth-api")
    optional("org.springframework.cloud:spring-cloud-gateway-server")
    optional("org.springframework.cloud:spring-cloud-starter-loadbalancer")
}
