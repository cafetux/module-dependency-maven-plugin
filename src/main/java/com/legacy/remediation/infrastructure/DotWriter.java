package com.legacy.remediation.infrastructure;

import com.legacy.remediation.model.DiagrammWriter;
import com.legacy.remediation.model.graph.Node;
import com.legacy.remediation.model.graph.ProjectView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * a writer implementation for DOT language (a graph description language)
 *
 * <a href="https://en.wikipedia.org/wiki/DOT_%28graph_description_language%29">...</a>
 */
public class DotWriter implements DiagrammWriter {


    @Override
    public File write(ProjectView modules, String fileName) {
        File toSave = new File(fileName+".gv");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(toSave))) {
            writer.write("digraph modules {");
            writer.write("\n");
            for (Node module : modules.all()) {
                writer.write(module.getSymbol()+"[label=\""+module.getLabel()+"\"];");
                writer.write("\n");
            }
            for (Node module : modules.all()) {
                for (Node dependency : module.getDependencies()) {
                    writer.write(module.getSymbol() + " -> " + dependency.getSymbol()+";");
                    writer.write("\n");
                }
            }
            writer.write("}");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return toSave;
    }

}
