dependencies {
    // 基础组件
    optional("org.owasp.antisamy:antisamy")
//    optional("javax.servlet:javax.servlet-api")
    optional("jakarta.servlet:jakarta.servlet-api")
    optional("org.apache.httpcomponents:httpclient")
	optional(project(":arthur-commons:arthur-common"))

    // Web
    optional("io.undertow:undertow-servlet")
    optional("org.springframework:spring-webmvc")
	api(project(":arthur-starter:arthur-web-starter"))
    api(project(":arthur-starter:arthur-boot-starter"))
    optional("org.springframework.boot:spring-boot-starter")
}
