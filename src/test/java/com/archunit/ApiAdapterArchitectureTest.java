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
public class ApiAdapterArchitectureTest {

    @ArchTest
    static final ArchRule controllers_should_be_in_api_adapters =
        classes()
            .that().haveSimpleNameEndingWith("Controller")
            .or().areAnnotatedWith("org.springframework.web.bind.annotation.RestController")
            .or().areAnnotatedWith("org.springframework.stereotype.Controller")
            .and().doNotHaveSimpleName("DocumentationController")
            .and().resideOutsideOfPackages("com.sun..", "sun..", "java..", "javax..", "jakarta..")
            .should().resideInAPackage("..adapters.api..")
            .allowEmptyShould(true);

    @ArchTest
    static final ArchRule controllers_should_not_be_in_core =
        noClasses()
            .that().resideInAPackage("..core..")
            .and().doNotHaveSimpleName("DocumentationController")
            .should().haveSimpleNameEndingWith("Controller")
            .andShould().beAnnotatedWith("org.springframework.web.bind.annotation.RestController")
            .orShould().beAnnotatedWith("org.springframework.stereotype.Controller");


    @ArchTest
    static final ArchRule api_adapters_should_not_use_database_adapters_directly =
        noClasses()
            .that().resideInAPackage("..adapters.api..")
            .should().dependOnClassesThat()
            .areAnnotatedWith("org.springframework.stereotype.Repository")
            .orShould().dependOnClassesThat()
            .areAssignableTo("org.springframework.data.repository.Repository");
@ArchTest
static final ArchRule api_adapters_and_controllers_should_only_access_allowed_packages =
    classes()
        .that().resideInAPackage("..adapters.api..")
        .or().haveSimpleNameEndingWith("Controller")
        .and().areAnnotatedWith("org.springframework.stereotype.Controller")
        .or().haveSimpleNameEndingWith("Controller")
        .and().areAnnotatedWith("org.springframework.web.bind.annotation.RestController")
        .and().doNotHaveSimpleName("DocumentationController")
        .should().onlyDependOnClassesThat()
        .resideInAnyPackage("..core..port..", "..core..", "..adapters.api..", "..infrastructure..", "java..", "javax..", "jakarta..", "org.springframework..")
        .allowEmptyShould(true);

    @ArchTest
    static final ArchRule controllers_must_depend_on_port_interfaces =
        classes()
            .that().haveSimpleNameEndingWith("Controller")
            .and().areAnnotatedWith("org.springframework.stereotype.Controller")
            .or().haveSimpleNameEndingWith("Controller")
            .and().areAnnotatedWith("org.springframework.web.bind.annotation.RestController")
            .and().doNotHaveSimpleName("DocumentationController")
            .should().dependOnClassesThat()
            .resideInAPackage("..core..port..")
            .andShould().dependOnClassesThat()
            .areInterfaces()
            .allowEmptyShould(true);

}