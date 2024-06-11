package org.springframework.gradle.dependencies;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.tasks.SourceSetContainer;

/**
 * copy by spring
 */
public class OptionalDependenciesPlugin implements Plugin<Project> {

    /**
     * Name of the {@code optional} configuration.
     */
    public static final String OPTIONAL_CONFIGURATION_NAME = "optional";

    @Override
    public void apply(Project project) {
        Configuration configuration = project.getConfigurations().create(OPTIONAL_CONFIGURATION_NAME);
        configuration.setCanBeConsumed(false);
        configuration.setCanBeResolved(false);

        project.getPlugins().withType(JavaPlugin.class, javaPlugin -> {
            SourceSetContainer sourceSets = project.getExtensions().getByType(JavaPluginExtension.class).getSourceSets();
            sourceSets.all(sourceSet -> {
                project.getConfigurations().getByName(sourceSet.getCompileClasspathConfigurationName()).extendsFrom(configuration);
                project.getConfigurations().getByName(sourceSet.getRuntimeClasspathConfigurationName()).extendsFrom(configuration);
            });
        });
    }

}
