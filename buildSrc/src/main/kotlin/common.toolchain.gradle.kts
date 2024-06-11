plugins {
    java
}

fun mainToolchainConfigured(): Boolean {
    return project.hasProperty("mainToolchain")
}

fun testToolchainConfigured(): Boolean {
    return project.hasProperty("testToolchain")
}

fun mainToolchainLanguageVersion(): JavaLanguageVersion {
    if (mainToolchainConfigured()) {
        return JavaLanguageVersion.of(property("mainToolchain").toString())
    }
    return JavaLanguageVersion.of(17)
}

fun testToolchainLanguageVersion(): JavaLanguageVersion {
    if (testToolchainConfigured()) {
        return JavaLanguageVersion.of(property("testToolchain").toString())
    }
    return mainToolchainLanguageVersion()
}

plugins.withType(JavaPlugin::class.java) {
    // Configure the Java Toolchain if the 'mainToolchain' is configured
    if (mainToolchainConfigured()) {
        java {
            toolchain {
                languageVersion.set(mainToolchainLanguageVersion())
            }
        }
    } else {
        // Fallback to JDK17
        java {
            sourceCompatibility = JavaVersion.VERSION_17
        }
    }
    // Configure a specific Java Toolchain for compiling and running tests if the 'testToolchain' property is varined
    if (testToolchainConfigured()) {
        val testLanguageVersion = testToolchainLanguageVersion()
        tasks.withType(JavaCompile::class.java).matching { it.name.contains("Test") }.configureEach {
            javaToolchains.compilerFor {
                languageVersion.set(testLanguageVersion)
            }
        }
        tasks.withType(Test::class.java).configureEach {
            javaToolchains.launcherFor {
                languageVersion.set(testLanguageVersion)
            }
        }
    }
}

// Configure the JMH plugin to use the toolchain for generating and running JMH bytecode
pluginManager.withPlugin("me.champeau.jmh") {
    if (mainToolchainConfigured() || testToolchainConfigured()) {
        tasks.matching { it.name.contains("jmh") && it.hasProperty("javaLauncher") }.configureEach {
            (this as me.champeau.jmh.WithJavaToolchain).javaLauncher.set(javaToolchains.launcherFor {
                languageVersion.set(testToolchainLanguageVersion())
            })
        }
        tasks.withType(JavaCompile::class.java).matching { it.name.contains("Jmh") }.configureEach {
            javaToolchains.compilerFor {
                languageVersion.set(testToolchainLanguageVersion())
            }
        }
    }
}