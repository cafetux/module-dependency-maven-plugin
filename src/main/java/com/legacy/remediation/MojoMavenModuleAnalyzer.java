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

import com.legacy.remediation.infrastructure.MindfusionPngRenderer;
import com.legacy.remediation.model.DiagrammWriter;
import com.legacy.remediation.infrastructure.DotWriter;
import com.legacy.remediation.model.ImageRenderer;
import com.legacy.remediation.model.module.Modules;
import org.apache.maven.model.Dependency;
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

/**
 * Goal which touches a timestamp file.
 *
 * @goal touch
 * @phase process-sources
 */
@Mojo(name = "maven-analyzer", defaultPhase = LifecyclePhase.COMPILE)
public class MojoMavenModuleAnalyzer extends AbstractMojo {

    private static final Logger LOGGER = LoggerFactory.getLogger(MojoMavenModuleAnalyzer.class);
    private static final Modules modules = new Modules();
    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    MavenProject project;

    @Parameter(defaultValue = "${reactorProjects}", required = true, readonly = true)
    private List<MavenProject> reactorProjects;

    @Parameter(name = "resultDirectory", property = "dependency.graph.resultDirectory", defaultValue="target")
    private String resultDirectory;
    @Parameter(name = "renderImage", property = "dependency.graph.renderImage", defaultValue = "true")
    private boolean renderImage;

    @Parameter(name = "excludeClassifiers", property = "dependency.graph.excludeClassifiers", defaultValue="")
    private List<String> excludeClassifiers;

    @Parameter(name = "excludeScopes", property = "dependency.graph.excludeScopes", defaultValue="")
    private List<String> excludeScopes;

    @Parameter(name = "excludeArtifactIds", property = "dependency.graph.excludeArtifactIds", defaultValue="")
    private List<String> excludeArtifactIds;

    @Parameter(name = "includeExternalDependencies", property = "dependency.graph.includeExternals", defaultValue="false")
    private boolean includeExternalDependencies;

    private final DiagrammWriter writer = new DotWriter();
    private final ImageRenderer imageriter = new MindfusionPngRenderer();


    public void execute() throws MojoExecutionException {

        String groupId = project.getGroupId();
        String artifactId = project.getArtifactId();
        LOGGER.info("current module is "+groupId+":"+artifactId);
        List<Dependency> dependencies = project.getDependencies();
        LOGGER.info(dependencies.size()+" found");
        dependencies.stream()
                .peek(x -> LOGGER.info("check for "+x.getGroupId()+":"+x.getArtifactId()))
                .filter(d -> !excludeClassifiers.contains(d.getClassifier()))
                .filter(d -> !excludeScopes.contains(d.getScope()))
                .filter(d -> !excludeArtifactIds.contains(d.getArtifactId()))
                .filter(d -> includeExternalDependencies || d.getGroupId().equals(groupId))
                .forEach(d -> {
                    LOGGER.info("Keep depeendency between "+d.getArtifactId());
                    modules.addDependency(artifactId, d.getArtifactId());
                });

        if(isLastModule()) {
            LOGGER.info("is last module, try to generate: "+modules);
            File resultFile = new File(resultDirectory);
            if(!resultFile.exists()){
                if(!resultFile.mkdirs()) {
                    LOGGER.error("cannot create directory "+resultDirectory);
                }
            }
            this.writer.write(modules, resultDirectory+"/module-dependency");
            if(renderImage) {
                imageriter.render(resultDirectory);
            }
        }
    }

    private boolean isLastModule() {
        return lastProject().equals(project);
    }

    private MavenProject lastProject() {
        return reactorProjects.get(reactorProjects.size() - 1);
    }


}
