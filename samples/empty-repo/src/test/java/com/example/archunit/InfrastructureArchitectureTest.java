package com.example.archunit;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;

@AnalyzeClasses(
    packages = "com.example",
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

    @ArchTest
    static final ArchRule configuration_classes_that_create_core_beans_should_not_be_in_infrastructure_config =
        noClasses()
            .that().resideInAPackage("..infrastructure.config..")
            .and().areAnnotatedWith("org.springframework.context.annotation.Configuration")
            .should().dependOnClassesThat()
            .resideInAPackage("..core..")
            .allowEmptyShould(true)
            .because("Configuration classes that create core beans should be at application root level or within core domain config, not in infrastructure/config. Valid locations: root package or core.{domain}.config");

    @ArchTest
    static final ArchRule infrastructure_config_should_only_contain_infrastructure_concerns =
        classes()
            .that().resideInAPackage("..infrastructure.config..")
            .and().areAnnotatedWith("org.springframework.context.annotation.Configuration")
            .should().onlyDependOnClassesThat()
            .resideInAnyPackage("java..", "javax..", "jakarta..", "org.springframework..", "..infrastructure..")
            .allowEmptyShould(true)
            .because("Infrastructure config should only configure infrastructure concerns, not core domain");
}