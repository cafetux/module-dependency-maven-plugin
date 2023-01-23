package com.legacy.remediation.model.graph;

import java.util.ArrayList;
import java.util.List;

/**
 * The Project view represent the project, with all of his components and dependencies between modules.
 */
public class ProjectView {

    private final List<Node> modules = new ArrayList<>();
    private String currentLetter = "A";
    private int alphabetCount = 1;


    /**
     * Assigne a symbol (letter) to module and register it on the project
     * @param module the module name to add
     */
    public void add(String module) {
        if (!exist(module)) {
            this.modules.add(new Node(currentSymbol(), module));
            nextSymbol();
        }
    }

    private String currentSymbol() {
        return currentLetter.concat(alphabetCount > 1 ? "" + alphabetCount : "");
    }

    private void nextSymbol() {
        if (currentLetter.equals("Z")) {
            this.currentLetter = "A";
            this.alphabetCount += 1;
        } else {
            this.currentLetter = String.valueOf((char) (currentLetter.charAt(0) + 1));
        }
    }

    private boolean exist(String moduleName) {
        return this.modules.stream().anyMatch(m -> m.getLabel().equals(moduleName));
    }

    /**
     *
     * @param module the module name that have dependency to declare
     * @param dependsOn the module label to add as dependency
     */
    public void addDependency(String module, String dependsOn) {
        Node source = getModule(module);
        Node dependency = getModule(dependsOn);
        source.addDependency(dependency);
    }

    private Node getModule(String module) {
        return this.modules.stream()
                .filter(m -> m.getLabel().equals(module))
                .findFirst()
                .orElseGet(() -> create(module));
    }

    private Node create(String moduleLabel) {
        add(moduleLabel);
        return getModule(moduleLabel);
    }

    public List<Node> all() {
        return new ArrayList<>(modules);
    }

    public boolean isEmpty() {
        return this.modules.isEmpty();
    }

    @Override
    public String toString() {
        return "Modules{" +
                "modules=" + modules +
                '}';
    }
}
