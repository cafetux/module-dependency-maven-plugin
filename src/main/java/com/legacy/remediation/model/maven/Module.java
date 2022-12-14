package com.legacy.remediation.model.maven;

public class Module {

    private final String groupId;
    private final String artifactId;
    private final String version;

    public Module(String groupId, String artifactId, String version) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public String getVersion() {
        return version;
    }
}
