package com.legacy.remediation.model.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represent a node on the graph of dependencies.
 */
public class Node {

    private final String symbol;
    private final String label;

    private final List<Node> dependencies = new ArrayList<>();

    /**
     *
     * @param symbol the symbol on the dot diagram scheme (ex: a letter)
     * @param label the label of the node (ex: name of the module)
     */
    public Node(String symbol, String label) {
        this.symbol = symbol;
        this.label = label;
    }

    /**
     * to add dependency on this node
     * @param dependency the dependency to add on the graph
     */
    public void addDependency(Node dependency) {
        this.dependencies.add(dependency);
    }

    /**
     *
     * @return the symbol (ex: a letter) to represent shortly the node on a graph
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     *
     * @return the label of the node (module name, library name, ect)
     */
    public String getLabel() {
        return label;
    }

    /**
     *
     * @return the dependencies of the current node
     */
    public List<Node> getDependencies() {
        return new ArrayList<>(dependencies);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node module = (Node) o;
        return Objects.equals(symbol, module.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol);
    }

    @Override
    public String toString() {
        return "Module{" +
                "symbol='" + symbol + '\'' +
                ", label='" + label + '\'' +
                ", dependencies=" + dependencies +
                '}';
    }
}
