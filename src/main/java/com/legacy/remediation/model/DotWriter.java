package com.legacy.remediation.model;

import com.legacy.remediation.model.module.Module;
import com.legacy.remediation.model.module.Modules;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * a writer implementation for DOT language (agraph description language)
 *
 * <a href="https://en.wikipedia.org/wiki/DOT_%28graph_description_language%29">...</a>
 */
public class DotWriter implements DiagrammWriter {


    @Override
    public void write(Modules modules, String fileName) {
        File toSave = new File(fileName+".gv");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(toSave))) {
            writer.write("digraph modules {");
            writer.write("\n");
            for (Module module : modules.all()) {
                writer.write(module.getSymbol()+"[label=\""+module.getLabel()+"\"];");
                writer.write("\n");
            }
            for (Module module : modules.all()) {
                for (Module dependency : module.getDependencies()) {
                    writer.write(module.getSymbol() + " -> " + dependency.getSymbol()+";");
                    writer.write("\n");
                }
            }
            writer.write("}");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
