plugins {
	`java-library`
	id("common.maven")
	id("common.versions")
	id("io.spring.convention.spring-module")
}


allprojects {
	version = "1.0-SNAPSHOT"
	configureProjectGroup(project)

	plugins.withId("maven-publish") {
		publishing {
			repositories {
				maven {
					val snapshotsRepoUrl = uri("F:\\repository")
					val releasesRepoUrl = uri("F:\\repository")
					afterEvaluate {
						url = if (version.toString().endsWith("-SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
					}
				}
			}
		}
	}

	configurations.all {
		resolutionStrategy {
			cacheChangingModulesFor(0, "seconds")
		}
	}

	repositories {
		mavenLocal()
		mavenCentral()
	}
}

/**
 * 动态计算module的group名称，如果存在.gradle.kts文件，则以该文件的前缀作为group名的后缀
 */
fun configureProjectGroup(target: Project) {
	val parent = target.parent
	target.group = "com.arthur"
	if (parent != null && target.rootProject != parent && project != parent) {
		val name: String = parent.buildFile.name
		val subGroupName = name.substring(0, name.indexOf(".", 0, true))
		if (subGroupName != "build") {
			target.group = "${target.group}.$subGroupName"
		}
	}
}


configure(subprojects - project(":arthur-bom")) {
	if (project.file("src/main/java").exists() || project.file("src/main/kotlin")
			.exists() || project.file("src/main/groovy").exists()
	) {
		apply(plugin = "idea")
//    apply(plugin = "java-library")
		apply(plugin = "common.java")
//    apply(plugin = "common.jacoco")
		apply(plugin = "common.maven")
		apply(plugin = "common.compiler")
		apply(plugin = "io.spring.convention.spring-module")

		if (project.file("src/test").exists()) {
			apply(plugin = "common.test")
		}

		dependencies {
			api(platform(project(":arthur-bom")))
			afterEvaluate {
				management(platform(libs.springBootDependencies))
			}
		}

		java {
			withSourcesJar()
		}

		tasks.clean {
			delete("$projectDir/out")
		}
	} else if (project.buildFile.exists()) {
		apply(plugin = "java-library")
	} else {
		apply(plugin = "java-platform")
	}
}
//apply(plugin = "common.japicmp")