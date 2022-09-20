package com.legacy.remediation.model.module;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ModulesTest {

    private Modules sut;

    @Test
    void should_be_unique_module_by_label() {
        sut = new Modules();
        sut.add("toto");
        sut.add("titi");
        sut.add("toto");
        sut.add("tata");
        assertThat(sut.all()).hasSize(3);
    }

    @Test
    void should_increment_symbol_for_each_module(){
        sut = new Modules();
        sut.add("toto");
        sut.add("titi");
        sut.add("toto");
        sut.add("tata");
        assertThat(sut.all())
                .extracting(Module::getSymbol)
                .containsExactly("A","B","C");
    }

    @Test
    void should_save_dependencies(){
        sut = new Modules();
        sut.add("toto");
        sut.add("titi");
        sut.add("tata");
        sut.addDependency("toto", "titi");
        sut.addDependency("toto", "tata");
        sut.addDependency("titi", "tata");

        assertThat(find("toto").getDependencies()).hasSize(2);
        assertThat(find("tata").getDependencies()).hasSize(0);
        assertThat(find("titi").getDependencies()).hasSize(1);
    }

    @Test
    void should_can_contains_more_than_26_modules() {
        sut = new Modules();
        for (int i = 1; i <= 27; i++) {
            sut.add("test0"+i);
        }
        assertThat(lastModule().getSymbol()).isEqualTo("A2");
        for (int i = 1; i <= 40; i++) {
            sut.add("test1"+i);
        }
        assertThat(lastModule().getSymbol()).isEqualTo("O3");
    }

    private Module lastModule() {
        return sut.all().get(sut.all().size() - 1);
    }

    private Module find(String moduleLabel) {
        return sut.all().stream().filter(x -> x.getLabel().equals(moduleLabel)).findFirst().get();
    }


}