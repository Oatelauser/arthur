plugins {
    java
    id("com.github.spotbugs")
}

dependencies {
    implementation("com.github.spotbugs:spotbugs-annotations:4.7.1")
    spotbugsPlugins("com.h3xstream.findsecbugs:findsecbugs-plugin:1.12.0")
}

spotbugs {
    ignoreFailures.set(true)
}

tasks.spotbugsMain {
    reports.create("html") {
        required.set(true)
        outputLocation.set(file("$buildDir/reports/spotbugs.html"))
        setStylesheet("fancy-hist.xsl")
    }
}