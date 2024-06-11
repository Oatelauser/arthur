/*
 * Copyright 2002-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.gradle;

import org.gradle.api.JavaVersion;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.GroovyPlugin;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.PluginManager;
import org.gradle.api.tasks.compile.CompileOptions;
import org.gradle.api.tasks.compile.JavaCompile;
import org.gradle.jvm.tasks.Jar;
import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper;
import org.springframework.gradle.dependencies.ManagementDependenciesPlugin;
import org.springframework.gradle.dependencies.OptionalDependenciesPlugin;
import org.springframework.gradle.properties.SpringCopyPropertiesPlugin;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Steve Riesenberg
 */
public class SpringJavaPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        // Apply default plugins
        PluginManager pluginManager = project.getPluginManager();
        pluginManager.apply(JavaPlugin.class);
        pluginManager.apply(OptionalDependenciesPlugin.class);
        pluginManager.apply(ManagementDependenciesPlugin.class);

        if (project.file("src/main/groovy").exists()
                || project.file("src/test/groovy").exists()
                || project.file("src/integration-test/groovy").exists()) {
            pluginManager.apply(GroovyPlugin.class);
        }

        if (project.file("src/main/kotlin").exists()
                || project.file("src/test/kotlin").exists()
                || project.file("src/integration-test/kotlin").exists()) {
            pluginManager.apply(KotlinPluginWrapper.class);
        }
        pluginManager.apply(SpringCopyPropertiesPlugin.class);

        project.getTasks().withType(Jar.class, (jar) -> jar.manifest((manifest) -> {
            Map<String, String> attributes = new HashMap<>();
            attributes.put("Created-By", String.format("%s (%s)", System.getProperty("java.version"), System.getProperty("java.specification.vendor")));
            attributes.put("Implementation-Title", project.getName());
            attributes.put("Implementation-Version", project.getVersion().toString());
            attributes.put("Automatic-Module-Name", project.getName().replace("-", "."));
            manifest.attributes(attributes);
        }));
    }
}
