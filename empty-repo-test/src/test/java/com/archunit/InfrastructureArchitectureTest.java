package com.archunit;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;

@AnalyzeClasses(
    packages = "com",
    importOptions = {
        ImportOption.DoNotIncludeTests.class,
        ImportOption.DoNotIncludeJars.class,
        ImportOption.DoNotIncludeArchives.class
    }
)
public class InfrastructureArchitectureTest {
    @ArchTest
    static final ArchRule documentation_controller_should_be_in_infrastructure =
        classes()
            .that().haveSimpleName("DocumentationController")
            .should().resideInAPackage("..infrastructure..")
            .allowEmptyShould(true)
            .because("DocumentationController is a configuration/utility class and belongs in infrastructure layer");
}