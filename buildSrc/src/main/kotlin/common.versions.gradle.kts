import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
    java
    id("com.github.ben-manes.versions")
}

tasks.withType<DependencyUpdatesTask> {
    rejectVersionIf {
        isNonStable(candidate.version) && !isNonStable(currentVersion)
    }
    checkForGradleUpdate = true
    outputFormatter = "plain"
    outputDir = "${buildDir}/reports/versions"
    reportfileName = "dependencyUpdates"
}

// 不允许发布候选版本作为稳定版本的可升级版本
fun isNonStable(version: String): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.uppercase().contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(version)
    return isStable.not()
}
