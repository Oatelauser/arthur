plugins {
    checkstyle
}

dependencies {
    checkstyle("io.spring.javaformat:spring-javaformat-checkstyle:0.0.34")
}

checkstyle {
    toolVersion = "9.3"
    isIgnoreFailures = true
    configDirectory.set(rootProject.file("src/checkstyle"))
}

tasks.named("checkstyleMain", org.gradle.api.plugins.quality.Checkstyle::class) {
    reports {
        xml.required.set(false)
        html.required.set(true)
    }
}