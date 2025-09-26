package com.archunit.hexagonal;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

@AnalyzeClasses(
    packages = "com",
    importOptions = {
        ImportOption.DoNotIncludeTests.class,
        ImportOption.DoNotIncludeJars.class,
        ImportOption.DoNotIncludeArchives.class
    }
)
public class HexagonalArchitectureTest {

    @ArchTest
    static final ArchRule hexagonal_architecture_is_respected = layeredArchitecture()
            .consideringAllDependencies()

            .layer("Core").definedBy("..core..")
            .layer("Adapters").definedBy("..adapters..")
            .layer("Infrastructure").definedBy("..infrastructure..")

            .whereLayer("Core").mayOnlyBeAccessedByLayers("Adapters")
            .whereLayer("Adapters").mayNotBeAccessedByAnyLayer();

    @ArchTest
    static final ArchRule core_should_not_depend_on_adapters =
        noClasses()
            .that().resideInAPackage("..core..")
            .should().dependOnClassesThat()
            .resideInAnyPackage("..adapters..");

    @ArchTest
    static final ArchRule core_should_not_depend_on_infrastructure_except_stereotypes =
        noClasses()
            .that().resideInAPackage("..core..")
            .should().dependOnClassesThat()
            .resideInAPackage("..infrastructure.config..")
            .orShould().dependOnClassesThat()
            .resideInAPackage("..infrastructure.util..")
            .because("Core may only depend on infrastructure.stereotype package, not config or util packages");

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

    // Note: Specific adapter rules are defined in AdapterTest.java and SpringBootArchitectureTest.java
    // to allow adapters to depend on classes within their own subfolder while preventing cross-adapter dependencies

    @ArchTest
    static final ArchRule infrastructure_should_only_contain_config_and_utils =
        classes()
            .that().resideInAPackage("..infrastructure..")
            .should().resideInAnyPackage(
                "..infrastructure.config..",
                "..infrastructure.util..",
                "..infrastructure.stereotype..",
                "..infrastructure.web.."
            )
            .because("Infrastructure should be organized into config, util, stereotype, and web packages");

    @ArchTest
    static final ArchRule infrastructure_should_not_contain_business_logic =
        noClasses()
            .that().resideInAPackage("..infrastructure..")
            .should().haveSimpleNameEndingWith("Service")
            .orShould().haveSimpleNameEndingWith("Repository")
            .orShould().haveSimpleNameEndingWith("Controller")
            .orShould().haveSimpleNameEndingWith("Adapter");
}