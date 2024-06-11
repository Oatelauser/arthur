dependencies {
    // 基础组件
//	optional("jakarta.servlet:jakarta.servlet-api")
	optional("io.netty:netty-buffer")
    optional(project(":arthur-commons:arthur-common"))
    optional("io.projectreactor.netty:reactor-netty-http")

	// Web
    optional("org.springframework:spring-webflux")
	api(project(":arthur-starter:arthur-web-starter"))
    api(project(":arthur-starter:arthur-boot-starter"))
    optional("org.springframework.boot:spring-boot-starter")
}
