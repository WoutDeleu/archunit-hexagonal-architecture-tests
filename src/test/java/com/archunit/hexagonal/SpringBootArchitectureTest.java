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
public class SpringBootArchitectureTest {

    @ArchTest
    static final ArchRule controllers_should_be_in_api_adapters =
        classes()
            .that().areAnnotatedWith("org.springframework.web.bind.annotation.RestController")
            .or().areAnnotatedWith("org.springframework.stereotype.Controller")
            .and().doNotHaveSimpleName("DocumentationController")
            .and().resideOutsideOfPackages("com.sun..", "sun..", "java..", "javax..", "jakarta..")
            .should().resideInAPackage("..adapters.api..")
            .allowEmptyShould(true);

    @ArchTest
    static final ArchRule repositories_should_be_in_database_adapters =
        classes()
            .that().areAnnotatedWith("org.springframework.stereotype.Repository")
            .or().areAssignableTo("org.springframework.data.repository.Repository")
            .and().resideOutsideOfPackages("com.sun..", "sun..", "java..", "javax..", "jakarta..", "com.github..", "org.springframework.data..")
            .should().resideInAPackage("..adapters.database..")
            .allowEmptyShould(true);

    @ArchTest
    static final ArchRule services_should_be_in_adapters_only =
        classes()
            .that().areAnnotatedWith("org.springframework.stereotype.Service")
            .should().resideInAPackage("..adapters..")
            .allowEmptyShould(true);

    @ArchTest
    static final ArchRule entities_should_not_use_spring_annotations =
        noClasses()
            .that().areAnnotatedWith("jakarta.persistence.Entity")
            .or().areAnnotatedWith("javax.persistence.Entity")
            .should().beAnnotatedWith("org.springframework.stereotype.Component")
            .orShould().beAnnotatedWith("org.springframework.stereotype.Service")
            .orShould().beAnnotatedWith("org.springframework.stereotype.Repository");

    @ArchTest
    static final ArchRule components_should_not_be_in_core =
        noClasses()
            .that().resideInAPackage("..core..")
            .should().beAnnotatedWith("org.springframework.stereotype.Component");

    @ArchTest
    static final ArchRule autowired_should_not_be_used_in_core =
        noClasses()
            .that().resideInAPackage("..core..")
            .should().beAnnotatedWith("org.springframework.beans.factory.annotation.Autowired");

    @ArchTest
    static final ArchRule api_adapters_should_not_use_database_adapters_directly =
        noClasses()
            .that().resideInAPackage("..adapters.api..")
            .should().dependOnClassesThat()
            .areAnnotatedWith("org.springframework.stereotype.Repository")
            .orShould().dependOnClassesThat()
            .areAssignableTo("org.springframework.data.repository.Repository");

    @ArchTest
    static final ArchRule spring_boot_application_should_be_in_root =
        classes()
            .that().areAnnotatedWith("org.springframework.boot.autoconfigure.SpringBootApplication")
            .should().resideOutsideOfPackages("..core..", "..adapters..", "..infrastructure..")
            .because("@SpringBootApplication class must exist and be in root package (e.g., com.x.y.z.team.nameapp) but outside hexagonal architecture layers");

    @ArchTest
    static final ArchRule adapter_services_should_use_spring_annotations =
        classes()
            .that().resideInAPackage("..adapters..")
            .and().haveSimpleNameEndingWith("Adapter")
            .should().beAnnotatedWith("org.springframework.stereotype.Component")
            .orShould().beAnnotatedWith("org.springframework.stereotype.Service")
            .orShould().beAnnotatedWith("org.springframework.stereotype.Repository");

    @ArchTest
    static final ArchRule jpa_entities_should_be_in_database_adapters =
        classes()
            .that().areAnnotatedWith("jakarta.persistence.Entity")
            .or().areAnnotatedWith("javax.persistence.Entity")
            .should().resideInAPackage("..adapters.database..")
            .allowEmptyShould(true);

    @ArchTest
    static final ArchRule controllers_should_use_core_port_interfaces =
        classes()
            .that().haveSimpleNameEndingWith("Controller")
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