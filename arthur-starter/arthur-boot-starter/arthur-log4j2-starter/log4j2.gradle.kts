dependencies {
    optional("com.lmax:disruptor")
    optional("io.micrometer:micrometer-registry-prometheus")
    optional("org.springframework.boot:spring-boot-starter-log4j2")
    optional("org.springframework.boot:spring-boot-starter-actuator")

    // AnnotationProcessor
    annotationProcessor("org.apache.logging.log4j:log4j-core")
}
