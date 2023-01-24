package com.legacy.remediation.model.maven;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class DependencyFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(DependencyFilter.class);
    private final Module current;
    private final List<String> scopesToExclude;
    private final List<String> classifiersToExclude;
    private final List<String> excludeArtifactIds;
    private final boolean includeExternalDependencies;


    public DependencyFilter(Module current, List<String> scopesToExclude, List<String> classifiersToExclude, List<String> excludeArtifactIds, boolean includeExternalDependencies) {
        this.current = current;
        this.scopesToExclude = scopesToExclude;
        this.classifiersToExclude = classifiersToExclude;
        this.excludeArtifactIds = excludeArtifactIds;
        this.includeExternalDependencies = includeExternalDependencies;
    }

    public List<Dependency> filter(List<Dependency> dependencies) {
        return dependencies.stream()
                .peek(x -> LOGGER.debug("check for "+x.toString()))
                .filter(this::checkIfCurrentIsExcluded)
                .filter(d -> includeExternalDependencies || isOnSameGroupId(d))
                .filter(this::checkIfExcludeByScope)
                .filter(this::checkIfExcludeByClassifier)
                .filter(this::checkIfExcludeByArtifactId)
                .collect(Collectors.toList());

    }

    private boolean checkIfCurrentIsExcluded(Dependency dependency) {
        if(excludeArtifactIds.contains(current.getArtifactId())) {
            LOGGER.debug("[excluded by current artifact id] " + dependency);
            return false;
        } else {
            return true;
        }
    }

    private boolean checkIfExcludeByScope(Dependency d) {
        if (scopesToExclude.contains(d.getScope())) {
            LOGGER.debug("[excluded by scope] " + d);
            return false;
        } else {
            return true;
        }
    }
    private boolean checkIfExcludeByClassifier(Dependency d) {
        if (classifiersToExclude.contains(d.getClassifier())) {
            LOGGER.debug("[excluded by classifier] " + d);
            return false;
        } else {
            return true;
        }
    }
    private boolean checkIfExcludeByArtifactId(Dependency d) {
        if (excludeArtifactIds.contains(d.getArtifactId())) {
            LOGGER.debug("[excluded by artifactID] " + d);
            return false;
        } else {
            return true;
        }
    }

    private boolean isOnSameGroupId(Dependency d) {
        if(d.getGroupId().equals(current.getGroupId())) {
            return true;
        }
        LOGGER.debug("[not in same group id] "+d);
        return false;
    }

}
