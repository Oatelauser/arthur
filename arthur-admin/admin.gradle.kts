plugins {
    application
    id("org.springframework.boot") version "3.0.1"
    id("common.test")
}

dependencies {
    // ================ web ==============
    implementation(project(":arthur-admin:arthur-admin-main"))
}


