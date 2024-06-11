import me.champeau.gradle.japicmp.JapicmpTask

plugins {
    java
    id("me.champeau.gradle.japicmp")
}

if (project.hasProperty("baselineVersion") && project == project.rootProject) {
    val baselineVersion: String = project.properties["baselineVersion"].toString()
    project.subprojects {
        if (project.plugins.hasPlugin(JavaPlugin::class.java) && project.plugins.hasPlugin(MavenPublishPlugin::class.java)) {
            project.tasks.create("japicmp", JapicmpTask::class.java) {
                group = JavaBasePlugin.DOCUMENTATION_GROUP
                description = "Generates an API diff report with japicmp"
                oldClasspath = project.files(
                    project.rootProject.configurations.detachedConfiguration(
                        project.dependencies.create("${project.group}:${project.name}:$baselineVersion@jar")
                    )
                )
                newClasspath = files(project.tasks.jar.get().archiveFile)
                isOnlyModified = true
                isIgnoreMissingClasses = true
                annotationExcludes.add("@kotlin.Metadata")
                project.afterEvaluate {
                    val outDir = "${project.rootProject.buildDir.absolutePath}\\reports\\api-diff\\${baselineVersion}_to_${project.version}\\${project.name}.html"
                    htmlOutputFile = project.file(outDir)
                }
            }
        } else {
            project.logger.debug("${project.name}未使用插件：java、maven-publish，无法开启插件【common.japicmp】")
        }
    }
} else {
    project.logger.warn("插件【common.japicmp】只能用于根目录项目，且必须要有变量baselineVersion用于指定比较版本，比如：-PbaselineVersion=1.0")
}
