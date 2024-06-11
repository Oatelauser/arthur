plugins {
    `java-platform`
}

dependencies {
    api(platform(libs.springBootDependencies))
    api(platform(libs.springCloudDependencies))
    api(platform(libs.springCloudAlibabaDependencies))

    constraints {
        api(libs.mapstruct)
		api("com.alibaba:druid:1.2.17")
		api("com.lmax:disruptor:3.4.4")
        api("com.stoyanr:evictor:1.0.0")
        api("com.alibaba:easyexcel:3.2.1")
        api("cn.hutool:hutool-all:5.8.11")
		api("org.objenesis:objenesis:3.3")
		api("org.jctools:jctools-core:4.0.1")
        api("com.google.guava:guava:31.1-jre")
        api("org.codehaus.janino:janino:3.1.9")
        api("org.owasp.encoder:encoder:1.2.3")
        api("org.owasp.antisamy:antisamy:1.7.2")
        api("org.apache.commons:commons-text:1.10.0")
        api("org.apache.httpcomponents:httpclient:4.5.14")

        api("com.alibaba.fastjson2:fastjson2:2.0.23")
		api("com.alibaba:transmittable-thread-local:2.14.2")
        api("com.baomidou:mybatis-plus-annotation:3.5.3.1")
        api("com.baomidou:mybatis-plus-boot-starter:3.5.3.1")
        api("org.redisson:redisson-spring-boot-starter:3.19.3")
        api("com.alibaba:druid-spring-boot-starter:1.2.17")

        // API文档
        api("org.springdoc:springdoc-openapi-ui:2.0.4")
        api("io.swagger.core.v3:swagger-annotations:2.2.7")
        api("org.springdoc:springdoc-openapi-webflux-ui:2.0.4")
        api("com.github.xiaoymin:knife4j-openapi3-jakarta-spring-boot-starter:4.1.0")

		api("org.springframework.cloud:spring-cloud-sleuth-api:3.1.5")
		api("org.springframework.cloud:spring-cloud-starter-sleuth:3.1.5")
        api("org.springframework.security:spring-security-oauth2-authorization-server:1.0.0")
    }
}

javaPlatform {
    allowDependencies()
}
