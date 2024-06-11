plugins {
    java
    jacoco
}

tasks.jacocoTestReport {
    reports {
        html.required.set(true)
    }
}

tasks.test {
    jacoco {
        setExcludes(listOf("jdk.internal.*"))
    }
    finalizedBy("jacocoTestReport")
}