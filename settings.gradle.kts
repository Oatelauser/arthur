rootProject.name = "arthur"

fun buildFiles(): ConfigurableFileTree {
    return fileTree(rootDir) {
        val excludes = gradle.startParameter.projectProperties["excludeProjects"]?.split(",")
        include("**/*.gradle", "**/*.gradle.kts")
        exclude(
            "build", "**/gradle", "settings.gradle", "settings.gradle.kts",
            "buildSrc", "/build.gradle", "/build.gradle.kts", ".*", "out"
        )
        if (!excludes.isNullOrEmpty()) {
            excludes.forEach {
                exclude(it)
            }
        }
    }
}

buildFiles().forEach { buildFile ->
    val isDefaultBuildName = buildFile.name == "build.gradle" || buildFile.name == "build.gradle.kts"
    val buildFilePath = buildFile.parentFile.absolutePath
    val projectPath = buildFilePath.replace(rootDir.absolutePath, "").replace(File.separator, ":")
    include(projectPath)
    if (!isDefaultBuildName) {
        val project = findProject(projectPath)
        project?.name = buildFile.parentFile.name
        project?.projectDir = buildFile.parentFile
        project?.buildFileName = buildFile.name
    }
}

//enableFeaturePreview("VERSION_CATALOGS")
dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("gradle/build.versions.toml"))
        }
    }
}
