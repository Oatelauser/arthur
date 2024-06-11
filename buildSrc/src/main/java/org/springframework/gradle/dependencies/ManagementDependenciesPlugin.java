package org.springframework.gradle.dependencies;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ConfigurationContainer;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.JavaTestFixturesPlugin;
import org.gradle.api.plugins.PluginContainer;
import org.gradle.api.publish.PublishingExtension;
import org.gradle.api.publish.VariantVersionMappingStrategy;
import org.gradle.api.publish.maven.MavenPublication;
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin;

/**
 * Creates a Management configuration that is appropriate for adding a platform to that is not exposed externally. If
 * the JavaPlugin is applied, the compileClasspath, runtimeClasspath, testCompileClasspath, and testRuntimeClasspath
 * will extend from it.
 * <p>
 * 拓展{@code org.springframework.gradle.dependencies.SpringManagementConfigurationPlugin}功能，提供AnnotationProcessor
 *
 * @author Rob Winch
 * @author Steve Riesenberg
 */
public class ManagementDependenciesPlugin implements Plugin<Project> {

    public static final String MANAGEMENT_CONFIGURATION_NAME = "management";

    @Override
    public void apply(Project project) {
        ConfigurationContainer configurations = project.getConfigurations();
        configurations.create(MANAGEMENT_CONFIGURATION_NAME, management -> {
            management.setVisible(true);
            management.setCanBeConsumed(false);
            management.setCanBeResolved(false);

            PluginContainer plugins = project.getPlugins();
            plugins.withType(JavaPlugin.class, javaPlugin -> {
                configurations.getByName(JavaPlugin.COMPILE_CLASSPATH_CONFIGURATION_NAME).extendsFrom(management);
                configurations.getByName(JavaPlugin.RUNTIME_CLASSPATH_CONFIGURATION_NAME).extendsFrom(management);
                configurations.getByName(JavaPlugin.TEST_COMPILE_CLASSPATH_CONFIGURATION_NAME).extendsFrom(management);
                configurations.getByName(JavaPlugin.TEST_RUNTIME_CLASSPATH_CONFIGURATION_NAME).extendsFrom(management);
                // AnnotationProcessor
                Configuration annotationProcessor = configurations.getByName(JavaPlugin.ANNOTATION_PROCESSOR_CONFIGURATION_NAME).extendsFrom(management);
                configurations.getByName(JavaPlugin.COMPILE_ONLY_CONFIGURATION_NAME).extendsFrom(annotationProcessor);
                Configuration testAnnotationProcessor = configurations.getByName(JavaPlugin.TEST_ANNOTATION_PROCESSOR_CONFIGURATION_NAME).extendsFrom(management);
                configurations.getByName(JavaPlugin.TEST_COMPILE_ONLY_CONFIGURATION_NAME).extendsFrom(testAnnotationProcessor);
            });

            //plugins.withType(JavaLibraryPlugin.class, javaLibraryPlugin -> {
                //Configuration configuration = configurations.getByName(JavaPlugin.API_ELEMENTS_CONFIGURATION_NAME).extendsFrom(management);
            //});

            plugins.withType(JavaTestFixturesPlugin.class, javaTestFixturesPlugin -> {
                configurations.getByName("testFixturesCompileClasspath").extendsFrom(management);
                configurations.getByName("testFixturesRuntimeClasspath").extendsFrom(management);
            });

            plugins.withType(MavenPublishPlugin.class, mavenPublishPlugin -> {
                PublishingExtension publishingExtension = project.getExtensions().getByType(PublishingExtension.class);
                publishingExtension.getPublications().withType(MavenPublication.class, publication -> {
                    publication.versionMapping(versions -> {
                        versions.allVariants(VariantVersionMappingStrategy::fromResolutionResult);
                    });
                });
            });
        });
    }

}
