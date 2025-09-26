package com.archunit.hexagonal;

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
public class AdapterTest {

    @ArchTest
    static final ArchRule adapters_should_implement_core_interfaces =
        classes()
            .that().resideInAPackage("..adapters..")
            .and().haveSimpleNameEndingWith("Adapter")
            .should().dependOnClassesThat()
            .resideInAPackage("..core..")
            .andShould().beAnnotatedWith("org.springframework.stereotype.Component")
            .orShould().beAnnotatedWith("org.springframework.stereotype.Service")
            .orShould().beAnnotatedWith("org.springframework.stereotype.Repository");

    @ArchTest
    static final ArchRule api_adapters_should_be_in_api_package =
        classes()
            .that().haveSimpleNameEndingWith("Controller")
            .or().areAnnotatedWith("org.springframework.web.bind.annotation.RestController")
            .or().areAnnotatedWith("org.springframework.stereotype.Controller")
            .and().doNotHaveSimpleName("DocumentationController")
            .and().resideOutsideOfPackages("com.sun..", "sun..", "java..", "javax..", "jakarta..")
            .should().resideInAPackage("..adapters.api..")
            .allowEmptyShould(true);

    @ArchTest
    static final ArchRule database_adapters_should_be_in_database_package =
        classes()
            .that().haveSimpleNameEndingWith("Repository")
            .and().areNotInterfaces()
            .or().areAnnotatedWith("org.springframework.stereotype.Repository")
            .and().resideOutsideOfPackages("com.sun..", "sun..", "java..", "javax..", "jakarta..", "com.github..", "org.springframework.data..")
            .should().resideInAPackage("..adapters.database..")
            .allowEmptyShould(true);

    @ArchTest
    static final ArchRule repositories_should_not_be_in_core =
        noClasses()
            .that().resideInAPackage("..core..")
            .should().haveSimpleNameEndingWith("Repository")
            .andShould().beAnnotatedWith("org.springframework.stereotype.Repository")
            .orShould().beAnnotatedWith("org.springframework.data.repository.Repository");

    @ArchTest
    static final ArchRule controllers_should_not_be_in_core =
        noClasses()
            .that().resideInAPackage("..core..")
            .and().doNotHaveSimpleName("DocumentationController")
            .should().haveSimpleNameEndingWith("Controller")
            .andShould().beAnnotatedWith("org.springframework.web.bind.annotation.RestController")
            .orShould().beAnnotatedWith("org.springframework.stereotype.Controller");

    @ArchTest
    static final ArchRule api_adapters_should_not_depend_on_database_adapters =
        noClasses()
            .that().resideInAPackage("..adapters.api..")
            .should().dependOnClassesThat()
            .resideInAPackage("..adapters.database..");

    @ArchTest
    static final ArchRule database_adapters_should_not_depend_on_api_adapters =
        noClasses()
            .that().resideInAPackage("..adapters.database..")
            .should().dependOnClassesThat()
            .resideInAPackage("..adapters.api..");

    @ArchTest
    static final ArchRule adapters_should_not_depend_on_messaging_adapters =
        noClasses()
            .that().resideInAPackage("..adapters.api..")
            .or().resideInAPackage("..adapters.database..")
            .should().dependOnClassesThat()
            .resideInAPackage("..adapters.messaging..");

    @ArchTest
    static final ArchRule database_adapters_should_not_access_api_adapters =
        noClasses()
            .that().resideInAPackage("..adapters.database..")
            .should().dependOnClassesThat()
            .resideInAPackage("..adapters.api..");

    @ArchTest
    static final ArchRule adapters_should_only_access_core_through_interfaces =
        classes()
            .that().resideInAPackage("..adapters..")
            .should().onlyAccessClassesThat()
            .resideInAPackage("..core..")
            .andShould().beInterfaces()
            .orShould().accessClassesThat()
            .resideOutsideOfPackage("..core..");

    @ArchTest
    static final ArchRule api_adapters_should_only_access_core_and_infrastructure =
        classes()
            .that().resideInAPackage("..adapters.api..")
            .should().onlyDependOnClassesThat()
            .resideInAnyPackage("..core..", "..adapters.api..", "..infrastructure..", "java..", "javax..", "jakarta..", "org.springframework..");

    @ArchTest
    static final ArchRule documentation_controller_should_be_in_infrastructure =
        classes()
            .that().haveSimpleName("DocumentationController")
            .should().resideInAPackage("..infrastructure..")
            .allowEmptyShould(true)
            .because("DocumentationController is a configuration/utility class and belongs in infrastructure layer");
}