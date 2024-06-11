import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
    java
//    `java-test-fixtures`
//    groovy
}


dependencies {
//    testImplementation(platform("org.spockframework:spock-bom:2.1-groovy-3.0"))
//    testImplementation("org.spockframework:spock-spring")
//    testImplementation("org.spockframework:spock-core")
//    testImplementation("org.codehaus.groovy:groovy")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude("org.junit.vintage:junit-vintage-engine")
    }
//    testImplementation("org.mockito:mockito-inline")
//    afterEvaluate {
//        plugins.withId("org.springframework.boot") {
//            testImplementation("org.springframework.boot:spring-boot-starter-test") {
//                exclude("org.junit.vintage:junit-vintage-engine")
//            }
//        }

//        plugins.findPlugin("org.springframework.boot") ?: let {
//            testImplementation(platform("org.junit:junit-bom:5.8.2"))
//            testImplementation("org.junit.jupiter:junit-jupiter-api")
//            testImplementation("org.junit.jupiter:junit-jupiter-engine")
//        }
//    }
}

tasks.withType(Test::class.java) {
    testLogging {
        events("FAILED")
        exceptionFormat = TestExceptionFormat.FULL
    }
    doFirst {
        // http://openjdk.java.net/jeps/223
        if (System.getProperty("java.specification.version") == "9" || System.getProperty("java.specification.version") == "10") {
            jvmArgs = listOf(
                "--add-opens", "java.base/java.lang=ALL-UNNAMED"
            )
        }
    }
}

tasks.test {
    useJUnitPlatform()
}

//val component = components["java"] as AdhocComponentWithVariants
//component.withVariantsFromConfiguration(configurations.testFixturesApiElements.get()) { skip() }
//component.withVariantsFromConfiguration(configurations.testFixturesRuntimeElements.get()) { skip() }