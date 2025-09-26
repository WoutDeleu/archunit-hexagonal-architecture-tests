package com.archunit.hexagonal;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

@AnalyzeClasses(
    packages = "com",
    importOptions = {
        ImportOption.DoNotIncludeTests.class,
        ImportOption.DoNotIncludeJars.class,
        ImportOption.DoNotIncludeArchives.class
    }
)
public class LayeredArchitectureTest {

    @ArchTest
    static final ArchRule hexagonal_architecture_is_respected = layeredArchitecture()
            .consideringAllDependencies()

            .layer("Core").definedBy("..core..")
            .layer("Adapters").definedBy("..adapters..")
            .layer("Infrastructure").definedBy("..infrastructure..")

            .whereLayer("Core").mayOnlyBeAccessedByLayers("Adapters")
            .whereLayer("Adapters").mayNotBeAccessedByAnyLayer();

    @ArchTest
    static final ArchRule adapters_should_depend_on_core =
        classes()
            .that().resideInAPackage("..adapters..")
            .should().dependOnClassesThat()
            .resideInAPackage("..core..")
            .orShould().dependOnClassesThat()
            .resideOutsideOfPackages("..adapters..", "..infrastructure..");

    @ArchTest
    static final ArchRule adapters_should_not_depend_on_each_other =
        noClasses()
            .that().resideInAPackage("..adapters.api..")
            .should().dependOnClassesThat()
            .resideInAPackage("..adapters.database..");
}