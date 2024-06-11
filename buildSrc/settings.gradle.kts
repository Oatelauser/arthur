rootProject.name = "arthur-gradle-plugin"

//enableFeaturePreview("VERSION_CATALOGS")
dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../gradle/build.versions.toml"))
        }
    }
}
