package com.legacy.remediation.infrastructure;

import com.dot.renderder.DotDiagramParser;
import com.dot.renderder.chart.FlowChart;
import com.legacy.remediation.model.ImageRenderer;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

public class MindfusionPngRenderer implements ImageRenderer {

    private final DotDiagramParser parser = new DotDiagramParser();
    private com.dot.renderder.ImageRenderer imageRenderer = new com.dot.renderder.ImageRenderer();


    @Override
    public void render(String directory) {
        File directoryFile = new File(directory);
        Arrays.stream(Objects.requireNonNull(directoryFile.listFiles()))
                .filter(f -> f.getName().endsWith(".gv"))
                .forEach(this::render);
    }

    private void render(File diagram) {
        try {
            FlowChart chart = parser.parse(diagram.toPath());
            imageRenderer.render(chart, diagram.getPath().replace(".gv", ""));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
