plugins {
    java
    `kotlin-dsl`
    `java-gradle-plugin`
}

dependencies {
    implementation(libs.pluginJmh)
    implementation(libs.pluginShadow)
    implementation(libs.pluginJapicmp)
    implementation(libs.pluginProfiles)
    implementation(libs.pluginSpotbugs)
    implementation(libs.pluginVersions)
    implementation(libs.pluginKotlin)
}

//tasks.jar {
//    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
//}

tasks.compileKotlin {
    kotlinOptions {
        val envJdkVersion = JavaVersion.toVersion(System.getProperty("java.version"))
        jvmTarget = envJdkVersion.toString()
    }
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

java.sourceCompatibility = JavaVersion.VERSION_17