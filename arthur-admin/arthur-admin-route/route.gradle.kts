plugins {
    application
    id("org.springframework.boot") version "3.0.1"
    id("common.test")
}

dependencies {
    // ================ web ==============
    implementation("org.springframework.boot:spring-boot-starter-web") {
        exclude("org.apache.tomcat", "tomcat-jdbc")
        exclude("org.springframework.boot", "spring-boot-starter-tomcat")
    }
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-starter-undertow")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation(project(":arthur-commons:arthur-common"))
    implementation("org.springdoc:springdoc-openapi-ui")
    implementation(project(":arthur-plugin:arthur-plugin-config-center:arthur-plugin-config-center-nacos"))
    implementation(project(":arthur-admin:arthur-admin-common"))
    implementation(project(":arthur-admin:arthur-admin-group"))

    // 微服务
    implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-discovery")
    implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-config")



    // ================ 工具 ==============
    implementation("com.alibaba.fastjson2:fastjson2")
    implementation("com.github.ben-manes.caffeine:caffeine")
    implementation("cn.hutool:hutool-all")
    implementation("commons-chain:commons-chain")


    // ================ 数据库 ==============
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