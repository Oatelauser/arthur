package org.springframework.convention;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.PluginManager;
import org.springframework.gradle.SpringJavaPlugin;

/**
 * @author Steve Riesenberg
 */
public class SpringModulePlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        PluginManager pluginManager = project.getPluginManager();
        pluginManager.apply(SpringJavaPlugin.class);
    }

}
