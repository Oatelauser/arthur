import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    `maven-publish`
    id("com.github.johnrengelman.shadow")
}

tasks.withType(ShadowJar::class.java) {
    minimize()
    mergeServiceFiles()
}

publishing {
    publications {
        val publication = create<MavenPublication>("shadow")
        project.shadow.component(publication)
    }
}