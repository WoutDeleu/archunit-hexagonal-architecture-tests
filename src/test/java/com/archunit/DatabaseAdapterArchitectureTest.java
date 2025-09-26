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
public class DatabaseAdapterArchitectureTest {

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
    static final ArchRule jpa_entities_should_be_in_database_adapters =
        classes()
            .that().areAnnotatedWith("jakarta.persistence.Entity")
            .or().areAnnotatedWith("javax.persistence.Entity")
            .should().resideInAPackage("..adapters.database..")
            .allowEmptyShould(true);

    @ArchTest
    static final ArchRule entities_should_not_use_spring_annotations =
        noClasses()
            .that().areAnnotatedWith("jakarta.persistence.Entity")
            .or().areAnnotatedWith("javax.persistence.Entity")
            .should().beAnnotatedWith("org.springframework.stereotype.Component")
            .orShould().beAnnotatedWith("org.springframework.stereotype.Service")
            .orShould().beAnnotatedWith("org.springframework.stereotype.Repository")
            .allowEmptyShould(true);

    @ArchTest
    static final ArchRule database_adapters_should_implement_core_interfaces =
        classes()
            .that().resideInAPackage("..adapters.database..")
            .and().haveSimpleNameEndingWith("Adapter")
            .should().dependOnClassesThat()
            .resideInAPackage("..core..")
            .andShould().beAnnotatedWith("org.springframework.stereotype.Component")
            .orShould().beAnnotatedWith("org.springframework.stereotype.Service")
            .orShould().beAnnotatedWith("org.springframework.stereotype.Repository")
            .allowEmptyShould(true);
}