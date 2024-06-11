plugins {
    id("common.jmh")
    id("common.spotbugs")
}

dependencies {
    optional("org.slf4j:slf4j-api")
    optional("com.lmax:disruptor")
    implementation("com.fasterxml.jackson.core:jackson-core")
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")

    // AnnotationProcessor
    annotationProcessor("org.projectlombok:lombok")
}
