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
public class InfrastructureArchitectureTest {

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

    @ArchTest
    static final ArchRule spring_boot_application_should_be_in_root =
        classes()
            .that().areAnnotatedWith("org.springframework.boot.autoconfigure.SpringBootApplication")
            .should().resideOutsideOfPackages("..core..", "..adapters..", "..infrastructure..")
            .because("@SpringBootApplication class must exist and be in root package (e.g., com.x.y.z.team.nameapp) but outside hexagonal architecture layers");

    @ArchTest
    static final ArchRule services_should_be_in_adapters_only =
        classes()
            .that().areAnnotatedWith("org.springframework.stereotype.Service")
            .should().resideInAPackage("..adapters..")
            .allowEmptyShould(true);

    @ArchTest
    static final ArchRule adapter_services_should_use_spring_annotations =
        classes()
            .that().resideInAPackage("..adapters..")
            .and().haveSimpleNameEndingWith("Adapter")
            .should().beAnnotatedWith("org.springframework.stereotype.Component")
            .orShould().beAnnotatedWith("org.springframework.stereotype.Service")
            .orShould().beAnnotatedWith("org.springframework.stereotype.Repository");

    @ArchTest
    static final ArchRule documentation_controller_should_be_in_infrastructure =
        classes()
            .that().haveSimpleName("DocumentationController")
            .should().resideInAPackage("..infrastructure..")
            .allowEmptyShould(true)
            .because("DocumentationController is a configuration/utility class and belongs in infrastructure layer");
}