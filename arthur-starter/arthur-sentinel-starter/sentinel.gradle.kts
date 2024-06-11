dependencies {
    // 项目依赖
    optional(project(":arthur-commons:arthur-common"))

    // 基础组件
//    optional("javax.servlet:javax.servlet-api")
    optional("jakarta.servlet:jakarta.servlet-api")
    optional("com.fasterxml.jackson.core:jackson-databind")

    // Web
    optional("org.springframework:spring-webmvc")
    optional("org.springframework:spring-webflux")
    optional("org.springframework.boot:spring-boot-starter")
    optional(project(":arthur-starter:arthur-boot-starter"))

    // sentinel适配
	optional("com.alibaba.csp:sentinel-spring-webflux-adapter")
    optional("com.alibaba.csp:sentinel-spring-webmvc-6x-adapter")
    optional("com.alibaba.csp:sentinel-spring-cloud-gateway-adapter")
}
