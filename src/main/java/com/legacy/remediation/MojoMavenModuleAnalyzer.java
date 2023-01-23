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
import com.legacy.remediation.infrastructure.DotWriter;
import com.legacy.remediation.model.graph.ProjectView;
import com.legacy.remediation.model.maven.Dependency;
import com.legacy.remediation.model.maven.DependencyFilter;
import com.legacy.remediation.model.maven.Module;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Goal which touches a timestamp file.
 *
 * @goal touch
 * @phase process-sources
 */
@Mojo(name = "maven-analyzer", defaultPhase = LifecyclePhase.COMPILE)
public class MojoMavenModuleAnalyzer extends AbstractMojo {

    private static final Logger LOGGER = LoggerFactory.getLogger(MojoMavenModuleAnalyzer.class);
    private static final ProjectView projectView = new ProjectView();
    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    MavenProject project;

    @Parameter(defaultValue = "${reactorProjects}", required = true, readonly = true)
    private List<MavenProject> reactorProjects;

    @Parameter(name = "resultDirectory", property = "dependency.graph.resultDirectory", defaultValue = "target")
    private String resultDirectory;

    @Parameter(name = "excludeClassifiers", property = "dependency.graph.excludeClassifiers", defaultValue = "")
    private List<String> excludeClassifiers;

    @Parameter(name = "excludeScopes", property = "dependency.graph.excludeScopes", defaultValue = "")
    private List<String> excludeScopes;

    @Parameter(name = "excludeArtifactIds", property = "dependency.graph.excludeArtifactIds", defaultValue = "")
    private List<String> excludeArtifactIds;

    @Parameter(name = "includeExternalDependencies", property = "dependency.graph.includeExternals", defaultValue = "false")
    private boolean includeExternalDependencies;

    private final DiagrammWriter writer = new DotWriter();

    private DependencyFilter filter;


    public void execute() throws MojoExecutionException {

        Module current = new Module(project.getGroupId(), project.getArtifactId(), project.getVersion());
        this.filter = new DependencyFilter(
                current,
                excludeScopes, excludeClassifiers, excludeArtifactIds, includeExternalDependencies);

        List<Dependency> dependencies = project.getDependencies().stream()
                .map(md -> new Dependency(md.getGroupId(), md.getArtifactId(), md.getScope(), md.getClassifier(), md.getVersion()))
                .collect(Collectors.toList());

        filter.filter(dependencies).forEach(d -> projectView.addDependency(current.getArtifactId(), d.getArtifactId()));

        if (isLastModule()) {
            LOGGER.info("is last module, try to generate: " + projectView);
            File resultFile = new File(resultDirectory);
            if (!resultFile.exists()) {
                if(!resultFile.mkdirs()) {
                    LOGGER.error("cannot create result directory");
                }
            }
            this.writer.write(projectView, resultDirectory + "/module-dependency");
        }
    }

    private boolean isLastModule() {
        return lastProject().equals(project);
    }

    private MavenProject lastProject() {
        return this.reactorProjects.get(this.reactorProjects.size() - 1);
    }


}
