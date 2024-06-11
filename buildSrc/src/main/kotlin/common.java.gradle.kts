import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

plugins {
    `java-library`
}

// java doc 配置
tasks.javadoc {
    val option = options as StandardJavadocDocletOptions
    option.author(true)
    option.docTitle = "${project.name}(${project.version})文档"
    option.header = "<b>${project.name} ${project.version}</b>"
    option.windowTitle = "${project.name} ${project.version} API"
    option.bottom =
        "<font size='-1'><a href='http://github.com/spotbugs/' target='_parent'>${project.name}</a> is licensed under the LGPL.</font>"
    option.isNoDeprecated = false
    option.isNoDeprecatedList = false
    option.isNoIndex = false
    option.isNoNavBar = false
    option.isNoTree = false
    option.isSplitIndex = true
    option.isUse = true
    option.isVersion = true
    option.tags = listOf("ant.task", "noinspection")
    option.encoding("UTF-8")
    isFailOnError = false

    if (JavaVersion.current().isJava9Compatible) {
        option.addBooleanOption("html5", true)
    }
}

val time = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(/* temporal = */ LocalDateTime.now())

// manifest配置
tasks.jar {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    manifest {
        attributes(
            mapOf(
                "Build-By" to System.getProperty("user.name"),
                "Created-By" to "${System.getProperty("java.version")} (${System.getProperty("java.specification.vendor")})",
                "Build-Date" to time.subSequence(0, 10),
                "Build-Time" to time.subSequence(10, time.length),
                "Specification-Title" to project.name,
                "Specification-Version" to project.version,
                "Implementation-Title" to project.name,
                "Implementation-Version" to project.version,
//                "Automatic-Module-Name" to project.name.replace('-', '.'),
                "Build-Jdk-Spec" to System.getProperty("java.version"),
            )
        )
    }
}


// 配置文件变量替换
//tasks.processResources {
//    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
//    from("src/main/resources")
//    filesMatching(listOf("*.properties", "*.yml")) {
//        expand(project.properties).filter(ReplaceTokens::class,
//            "tokens" to mapOf("SRC_ROOT_DIR" to "1"))
//    }
//
//    filteringCharset = "UTF-8"
//}