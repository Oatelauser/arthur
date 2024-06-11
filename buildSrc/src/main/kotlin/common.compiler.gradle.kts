project.plugins.withId("java-library") {
    val commonCompilerArgs = listOf(
//        "-Xlint:overrides"
        "-Xlint:serial", "-Xlint:cast", "-Xlint:classfile", "-Xlint:dep-ann",
        "-Xlint:divzero", "-Xlint:empty", "-Xlint:finally", "-Xlint:overrides",
        "-Xlint:path", "-Xlint:processing", "-Xlint:static", "-Xlint:try",
        "-Xlint:-options", "-parameters"
    )

    val compilerArgs = ArrayList<String>(commonCompilerArgs)
    compilerArgs.addAll(
        listOf(
            "-Xlint:varargs", "-Xlint:fallthrough", "-Xlint:deprecation"
            , "-Xlint:rawtypes", "-Xlint:unchecked"
        )
    )

    val testCompilerArgs = ArrayList<String>(commonCompilerArgs)
    testCompilerArgs.addAll(
        listOf(
            "-Xlint:-varargs", "-Xlint:-fallthrough", "-Xlint:-rawtypes",
            "-Xlint:-deprecation", "-Xlint:-unchecked"
        )
    )

    project.plugins.withType(JavaLibraryPlugin::class.java) {
        project.tasks.withType(JavaCompile::class.java)
            .matching { compileTask ->
                JavaPlugin.COMPILE_JAVA_TASK_NAME == compileTask.name
            }.forEach { compileTask ->
                compileTask.options.compilerArgs.addAll(compilerArgs)
                compileTask.options.encoding = "UTF-8"
            }
        project.tasks.withType(JavaCompile::class.java)
            .matching { compileTask ->
                JavaPlugin.COMPILE_TEST_JAVA_TASK_NAME == compileTask.name
                        || "compileTestFixturesJava" == compileTask.name
            }.forEach { compileTask ->
                compileTask.options.compilerArgs.addAll(testCompilerArgs)
                compileTask.options.encoding = "UTF-8"
            }
    }
}