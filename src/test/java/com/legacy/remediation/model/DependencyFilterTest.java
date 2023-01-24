package com.legacy.remediation.model;

import com.legacy.remediation.model.maven.Dependency;
import com.legacy.remediation.model.maven.DependencyFilter;
import com.legacy.remediation.model.maven.Module;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class DependencyFilterTest {

    private List<Dependency> moduleDependencies = Arrays.asList(
            module("com.test", "module-4"),
            module("com.test", "module-2"),
            module("com.test", "test-report"),
            module("com.spring", "spring-core", "compile", null, "2.6.4"),
            module("junit", "junit", "test", null, "4.8.0"),
            module("com.test", "module-1", "test", "initH2", "1.0")
    );
    private Module currentRootModule = new Module("com.test", "bootstrap", "1.0");

    @Test
    void should_keep_only_dependencies_with_same_group_id_as_root() {
        Module root = new Module("com.test", "bootstrap", "1.0");

        DependencyFilter filter = given_filter_with_default_parameters();

        assertThat(filter.filter(moduleDependencies)).containsExactly(
                module("com.test", "module-4"),
                module("com.test", "module-2"),
                module("com.test", "test-report"),
                module("com.test", "module-1", "test", "initH2", "1.0")
        );

    }

    @Test
    void should_can_keep_externals_dependencies() {
        Module root = new Module("com.test", "bootstrap", "1.0");

        DependencyFilter filter = given_filter_that_accept_externals_dependencies();

        assertThat(filter.filter(moduleDependencies)).containsExactlyInAnyOrder(
                module("com.test", "module-4"),
                module("com.test", "module-2"),
                module("com.test", "test-report"),
                module("com.test", "module-1", "test", "initH2", "1.0"),

                module("com.spring", "spring-core", "compile", null, "2.6.4"),
                module("junit", "junit", "test", null, "4.8.0")
        );

    }

    @Test
    void should_can_exclude_some_scopes() {

        DependencyFilter filter = given_filter_that_exclude_scopes(Arrays.asList("test"));

        assertThat(filter.filter(moduleDependencies)).containsExactlyInAnyOrder(
                module("com.test", "module-4"),
                module("com.test", "module-2"),
                module("com.test", "test-report")
        );
    }

    @Test
    void should_can_exclude_some_classifier() {

        DependencyFilter filter = given_filter_that_exclude_classifier(Arrays.asList("initH2"));

        assertThat(filter.filter(moduleDependencies)).containsExactlyInAnyOrder(
                module("com.test", "module-4"),
                module("com.test", "module-2"),
                module("com.test", "test-report")
        );
    }

    @Test
    void should_can_exclude_some_artifact_id() {

        DependencyFilter filter = given_filter_that_exclude_artifact_id(Arrays.asList("test-report"));

        assertThat(filter.filter(moduleDependencies)).containsExactlyInAnyOrder(
                module("com.test", "module-4"),
                module("com.test", "module-2"),
                module("com.test", "module-1", "test", "initH2", "1.0")
        );
    }
    @Test
    void should_can_exclude_some_artifact_ids() {

        DependencyFilter filter = given_filter_that_exclude_artifact_id(Arrays.asList("bootstrap","test-report"));

        assertThat(filter.filter(moduleDependencies)).isEmpty();
    }

    private DependencyFilter given_filter_that_exclude_classifier(List<String> classifier) {
        return new DependencyFilter(this.currentRootModule, new ArrayList<>(), classifier, new ArrayList<>(), false);
    }

    private DependencyFilter given_filter_that_exclude_artifact_id(List<String> artifactIds) {
        return new DependencyFilter(this.currentRootModule, new ArrayList<>(), new ArrayList<>(), artifactIds, false);
    }

    private DependencyFilter given_filter_that_exclude_scopes(List<String> scopes) {
        return new DependencyFilter(this.currentRootModule, scopes, new ArrayList<>(), new ArrayList<>(), false);
    }

    private DependencyFilter given_filter_that_accept_externals_dependencies() {
        return new DependencyFilter(this.currentRootModule, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), true);
    }

    private DependencyFilter given_filter_with_default_parameters() {
        return new DependencyFilter(this.currentRootModule, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), false);
    }

    private Dependency module(String groupId, String artifactId) {
        return new Dependency(groupId, artifactId, "compile", null, "1.0");
    }

    private Dependency module(String groupId, String artifactId, String scope, String classifier, String version) {
        return new Dependency(groupId, artifactId, scope, classifier, version);
    }
}