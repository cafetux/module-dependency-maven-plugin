package com.legacy.remediation.model.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Node {

    private final String symbol;
    private final String label;
    private final List<Node> dependencies = new ArrayList<>();

    public Node(String symbol, String label) {
        this.symbol = symbol;
        this.label = label;
    }

    public void addDependency(Node dependency) {
        this.dependencies.add(dependency);
    }

    public String getSymbol() {
        return symbol;
    }

    public String getLabel() {
        return label;
    }

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
