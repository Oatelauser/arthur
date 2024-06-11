@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    application
    alias(libs.plugins.springboot)
}

description = "API网关"

dependencies {
    // 项目依赖
    implementation(project(":arthur-commons:arthur-common"))
    implementation(project(":arthur-plugin:arthur-plugin-all"))
    implementation(project(":arthur-starter:arthur-boot-starter"))
    implementation(project(":arthur-starter:arthur-boot-starter:arthur-event-starter"))

    // 基础组件
    implementation("com.lmax:disruptor")
	implementation("org.jctools:jctools-core")
    implementation("org.codehaus.janino:janino")
    implementation("com.github.ben-manes.caffeine:caffeine")

    // 微服务
    implementation(project(":arthur-starter:arthur-cloud-starter"))
//    implementation("org.springframework.cloud:spring-cloud-starter-sleuth")
    implementation("org.springframework.cloud:spring-cloud-starter-gateway")
    implementation("org.springframework.cloud:spring-cloud-starter-loadbalancer")
    implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-discovery")
    implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive")
    implementation(project(":arthur-starter:arthur-web-starter:arthur-webflux-starter"))
//    implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-config")

    // sentinel
    implementation("com.alibaba.csp:sentinel-datasource-nacos")
    implementation(project(":arthur-starter:arthur-sentinel-starter"))
    implementation("com.alibaba.cloud:spring-cloud-alibaba-sentinel-gateway")
    implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-sentinel")

    // 日志
    implementation("org.springframework.boot:spring-boot-starter-log4j2")
    implementation(project(":arthur-starter:arthur-boot-starter:arthur-log4j2-starter"))

    // 监控
    implementation("io.micrometer:micrometer-registry-prometheus")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    // AnnotationProcessor
    annotationProcessor("org.projectlombok:lombok")
//    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
}

configurations.all {
    exclude("org.springframework.boot", "spring-boot-starter-logging")
}

application {
    mainClass.set("com.broadtech.arthur.gateway.ArthurGatewayApplication")
}
