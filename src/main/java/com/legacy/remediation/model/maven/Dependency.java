package com.legacy.remediation.model.maven;

import java.util.Objects;

public class Dependency {

    private final String groupId;
    private final String artifactId;
    private final String scope;
    private final String classifier;
    private final String version;

    public Dependency(String groupId, String artifactId, String scope, String classifier, String version) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.scope = scope;
        this.classifier = classifier;
        this.version = version;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public String getScope() {
        return scope;
    }

    public String getClassifier() {
        return classifier;
    }

    public String getVersion() {
        return version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dependency that = (Dependency) o;
        return Objects.equals(groupId, that.groupId) && Objects.equals(artifactId, that.artifactId) && Objects.equals(scope, that.scope) && Objects.equals(classifier, that.classifier) && Objects.equals(version, that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, artifactId, scope, classifier, version);
    }

    @Override
    public String toString() {
        return "Dependency{" +
                "groupId='" + groupId + '\'' +
                ", artifactId='" + artifactId + '\'' +
                ", scope='" + scope + '\'' +
                ", classifier='" + classifier + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
