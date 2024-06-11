dependencies {
	// 基础组件
	optional(project(":arthur-commons:arthur-common"))

	// Web
	optional("org.springframework.boot:spring-boot-starter")
	optional(project(":arthur-starter:arthur-boot-starter"))

	// 微服务
	optional("com.alibaba.nacos:nacos-client")
	optional("org.springframework.cloud:spring-cloud-commons")
	optional("org.springframework.cloud:spring-cloud-context")
	optional("org.springframework.cloud:spring-cloud-loadbalancer")
	optional("org.springframework.cloud:spring-cloud-gateway-server")
}
