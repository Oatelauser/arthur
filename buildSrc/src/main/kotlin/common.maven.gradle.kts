plugins {
    signing
    `maven-publish`
}

val projectVersion = project.version.toString()
val isReleaseVersion = !projectVersion.endsWith("SNAPSHOT") &&
/* for Eclipse plugin */ !projectVersion.contains("-SNAPSHOT.")
publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components[if (plugins.hasPlugin("java-platform")) "javaPlatform" else "java"])
            // 作为插件的优先级狠高，因此很有可能无法读取到正确的信息，需要懒加载配置
            // 懒加载配置，在项目评估完之后，在去执行如下操作
            afterEvaluate {
                groupId = "${project.group}"
                artifactId = project.name
                version = "${project.version}"
            }
            pom {
                description.set(project.description)
            }
        }
    }
}

val signingKey = System.getenv("SIGNING_KEY")
val signingPassword = System.getenv("SIGNING_PASSWORD")

signing {
    if (signingKey != null &&
        signingPassword != null &&
        signingKey.isNotEmpty() &&
        signingPassword.isNotEmpty()
    ) {
        useInMemoryPgpKeys(signingKey, signingPassword)
        sign(publishing.publications.find { publication ->
            publication::class.java == MavenPublication::class.java
        })
    } else {
        logger.warn("The signing key and password are null. This can be ignored if this is a pull request.")
    }
}

// 可重现的构建
tasks.withType(AbstractArchiveTask::class.java).configureEach {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true
    dirMode = Integer.parseInt("0755", 8)
    fileMode = Integer.parseInt("0644", 8)
}

tasks.withType(Sign::class.java) {
    onlyIf {
        isReleaseVersion
    }
}