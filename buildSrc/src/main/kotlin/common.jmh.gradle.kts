plugins {
    id("me.champeau.jmh")
}

dependencies {
    jmh("org.openjdk.jmh:jmh-core:1.36")
    jmh("net.sf.jopt-simple:jopt-simple")
    jmhAnnotationProcessor("org.openjdk.jmh:jmh-generator-annprocess:1.36")
}

jmh {
    duplicateClassesStrategy.set(DuplicatesStrategy.EXCLUDE)
}

tasks.processJmhResources {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
