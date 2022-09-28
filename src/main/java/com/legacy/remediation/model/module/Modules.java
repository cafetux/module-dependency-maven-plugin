package com.legacy.remediation.model.module;

import java.util.ArrayList;
import java.util.List;

public class Modules {

    private final List<Module> modules = new ArrayList<>();
    private String currentLetter = "A";
    private int alphabetCount = 1;


    public void add(String module) {
        if (!exist(module)) {
            this.modules.add(new Module(currentSymbol(), module));
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

    public void addDependency(String module, String dependsOn) {
        Module source = getModule(module);
        Module dependency = getModule(dependsOn);
        source.addDependency(dependency);
    }

    private Module getModule(String module) {
        return this.modules.stream()
                .filter(m -> m.getLabel().equals(module))
                .findFirst()
                .orElseGet(() -> create(module));
    }

    private Module create(String moduleLabel) {
        add(moduleLabel);
        return getModule(moduleLabel);
    }

    public List<Module> all() {
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
