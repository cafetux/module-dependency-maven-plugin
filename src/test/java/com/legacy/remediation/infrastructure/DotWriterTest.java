package com.legacy.remediation.infrastructure;

import com.legacy.remediation.model.graph.ProjectView;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class DotWriterTest {

    @Test
    void should_write_dot_diagram() {
        ProjectView project = new ProjectView();
        project.addDependency("web", "service");
        project.addDependency("service", "database");
        project.addDependency("database", "web");
        File result = new DotWriter().write(project, "target/result");
        assertThat(result).exists();
        assertThat(result.getName()).isEqualTo("result.gv");

        List<String> lines = read(result.toPath());

        assertThat(lines)
                .containsExactly(
                        "digraph modules {",
                        "A[label=\"web\"];",
                        "B[label=\"service\"];",
                        "C[label=\"database\"];",
                        "A -> B;",
                        "B -> C;",
                        "C -> A;",
                        "}"
                );
    }

    private List<String> read(Path file) {
        try {
            return Files.readAllLines(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}