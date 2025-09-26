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
public class CoreLayerTest {

    @ArchTest
    static final ArchRule core_should_not_depend_on_spring_framework =
        noClasses()
            .that().resideInAPackage("..core..")
            .should().dependOnClassesThat()
            .resideInAnyPackage("org.springframework..", "org.springframework.boot..");

    @ArchTest
    static final ArchRule core_should_not_depend_on_jpa =
        noClasses()
            .that().resideInAPackage("..core..")
            .should().dependOnClassesThat()
            .resideInAnyPackage("jakarta.persistence..", "javax.persistence..", "org.hibernate..");

    @ArchTest
    static final ArchRule core_should_not_depend_on_jackson =
        noClasses()
            .that().resideInAPackage("..core..")
            .should().dependOnClassesThat()
            .resideInAnyPackage("com.fasterxml.jackson..");

    @ArchTest
    static final ArchRule core_should_not_depend_on_web_frameworks =
        noClasses()
            .that().resideInAPackage("..core..")
            .should().dependOnClassesThat()
            .resideInAnyPackage("jakarta.servlet..", "javax.servlet..", "org.springframework.web..");

    @ArchTest
    static final ArchRule core_should_not_use_spring_annotations =
        noClasses()
            .that().resideInAPackage("..core..")
            .should().beAnnotatedWith("org.springframework.stereotype.Component")
            .orShould().beAnnotatedWith("org.springframework.stereotype.Service")
            .orShould().beAnnotatedWith("org.springframework.stereotype.Repository")
            .orShould().beAnnotatedWith("org.springframework.web.bind.annotation.RestController")
            .orShould().beAnnotatedWith("org.springframework.boot.autoconfigure.SpringBootApplication");

    @ArchTest
    static final ArchRule core_should_not_use_jpa_annotations =
        noClasses()
            .that().resideInAPackage("..core..")
            .should().beAnnotatedWith("jakarta.persistence.Entity")
            .orShould().beAnnotatedWith("jakarta.persistence.Table")
            .orShould().beAnnotatedWith("jakarta.persistence.Id")
            .orShould().beAnnotatedWith("jakarta.persistence.Column")
            .orShould().beAnnotatedWith("javax.persistence.Entity")
            .orShould().beAnnotatedWith("javax.persistence.Table")
            .orShould().beAnnotatedWith("javax.persistence.Id")
            .orShould().beAnnotatedWith("javax.persistence.Column");

    @ArchTest
    static final ArchRule core_should_not_depend_on_spring_util =
        noClasses()
            .that().resideInAPackage("..core..")
            .should().dependOnClassesThat()
            .resideInAnyPackage("org.springframework.util..")
            .because("Core classes should not depend on Spring utility classes like org.springframework.util.Assert");

    @ArchTest
    static final ArchRule core_interfaces_should_be_implemented_in_adapters =
        classes()
            .that().resideInAPackage("..core..")
            .and().areInterfaces()
            .should().onlyHaveDependentClassesThat()
            .resideInAnyPackage("..adapters..", "..core..");

    @ArchTest
    static final ArchRule core_should_not_have_external_annotations =
        noClasses()
            .that().resideInAPackage("..core..")
            .should().beAnnotatedWith("com.fasterxml.jackson.annotation.JsonProperty")
            .orShould().beAnnotatedWith("org.springframework.beans.factory.annotation.Autowired")
            .orShould().beAnnotatedWith("org.springframework.beans.factory.annotation.Value");

}