package com.legacy.remediation;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.legacy.remediation.model.DiagrammWriter;
import com.legacy.remediation.model.DotWriter;
import com.legacy.remediation.model.module.Module;
import com.legacy.remediation.model.module.Modules;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.*;
import java.util.List;

/**
 * Goal which touches a timestamp file.
 *
 * @goal touch
 * @phase process-sources
 */
@Mojo(name = "maven-analyzer", defaultPhase = LifecyclePhase.COMPILE)
public class MojoMavenModuleAnalyzer extends AbstractMojo {

    private static final Modules modules = new Modules();
    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    MavenProject project;

    @Parameter(defaultValue = "${reactorProjects}", required = true, readonly = true)
    private List<MavenProject> reactorProjects;

    private DiagrammWriter writer = new DotWriter();



    public void execute() throws MojoExecutionException {

        String groupId = project.getGroupId();
        String artifactId = project.getArtifactId();

        List<Dependency> dependencies = project.getDependencies();
        dependencies.stream()
                .filter(d -> d.getGroupId().equals(groupId))
                .forEach(d -> modules.addDependency(artifactId, d.getArtifactId()));

        if(isLastModule()) {
            this.writer.write(modules, "module-dependency");
        }
    }

    private boolean isFirstModule() {
        return modules.isEmpty();
    }

    private boolean isLastModule() {
        return lastProject().equals(project);
    }

    private MavenProject lastProject() {
        return reactorProjects.get(reactorProjects.size() - 1);
    }


}
