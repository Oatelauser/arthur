plugins {
    application
    id("org.springframework.boot") version "3.0.1"
    id("common.test")
}

dependencies {
    // ================ gateway ==============
    implementation(project(":arthur-commons:arthur-common"))
    implementation("org.springdoc:springdoc-openapi-ui")
    implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-discovery")
    implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-config")

    implementation("org.springframework.cloud:spring-cloud-starter-gateway")
    implementation(project(":arthur-admin:arthur-admin-common"))

    // ================ 工具 ==============
    implementation("com.alibaba.fastjson2:fastjson2")
    implementation("com.github.ben-manes.caffeine:caffeine")
    implementation("cn.hutool:hutool-all")
    implementation("com.google.guava:guava:31.1-jre")


    // ================ 数据库 ==============

    implementation("org.apache.commons:commons-pool2")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("mysql:mysql-connector-java")
    implementation("com.alibaba:druid-spring-boot-starter")
    implementation("com.baomidou:mybatis-plus-boot-starter")



    // ================ 日志 ==============
    implementation("com.lmax:disruptor")
    implementation("org.springframework.boot:spring-boot-starter-log4j2")
    implementation(project(":arthur-starter:arthur-boot-starter:arthur-log4j2-starter"))

    // ================ 监控 ==============
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.micrometer:micrometer-registry-prometheus")

    // ================ annotation processor ==============
    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

}

configurations.all {
    exclude("org.springframework.boot", "spring-boot-starter-logging")
}
repositories {
    mavenCentral()
}