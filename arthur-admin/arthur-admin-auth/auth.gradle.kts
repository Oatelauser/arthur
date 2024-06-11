plugins {
    application
    id("org.springframework.boot") version "3.0.1"
    id("common.test")
}

dependencies {
    // ================ gateway ==============
    implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-discovery")
    implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-config")
    implementation("org.springframework.boot:spring-boot-starter-web") {
        exclude("org.apache.tomcat", "tomcat-jdbc")
        exclude("org.springframework.boot", "spring-boot-starter-tomcat")
    }
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-starter-undertow")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation(project(":arthur-commons:arthur-common"))
    implementation("org.springdoc:springdoc-openapi-ui")

    //================ security ==============
    implementation("com.nimbusds:nimbus-jose-jwt")
    implementation("org.springframework.security:spring-security-oauth2-jose")
    implementation("org.springframework.cloud:spring-cloud-starter-oauth2")
    implementation("org.springframework.security:spring-security-web:5.8.0")

    // ================ 工具 ==============
    implementation("com.alibaba.fastjson2:fastjson2")
    implementation("com.github.ben-manes.caffeine:caffeine")
    implementation("cn.hutool:hutool-all")

    // ================ 数据库 ==============
    implementation("mysql:mysql-connector-java")
    implementation("com.alibaba:druid-spring-boot-starter")
    implementation("com.baomidou:mybatis-plus-boot-starter")
    implementation("org.apache.commons:commons-pool2")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")


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
