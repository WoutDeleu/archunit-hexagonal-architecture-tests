package com.archunit.hexagonal;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

@AnalyzeClasses(
    packages = "com",
    importOptions = {
        ImportOption.DoNotIncludeTests.class,
        ImportOption.DoNotIncludeJars.class,
        ImportOption.DoNotIncludeArchives.class
    }
)
public class CoreDomainArchitectureTest {

  @ArchTest
  static final ArchRule repositories_should_not_be_in_core =
      noClasses()
          .that().resideInAPackage("..core..")
          .should().haveSimpleNameEndingWith("Repository")
          .andShould().beAnnotatedWith("org.springframework.stereotype.Repository")
          .orShould().beAnnotatedWith("org.springframework.data.repository.Repository");

  @ArchTest
  static final ArchRule core_should_not_depend_on_adapters =
      noClasses()
          .that().resideInAPackage("..core..")
          .should().dependOnClassesThat()
          .resideInAnyPackage("..adapters..");

  @ArchTest
  static final ArchRule core_should_not_depend_on_infrastructure_config_or_util =
      noClasses()
          .that().resideInAPackage("..core..")
          .should().dependOnClassesThat()
          .resideInAnyPackage("..infrastructure.config..", "..infrastructure.util..")
          .because(
              "Core must not depend on infrastructure config or util packages; only infrastructure.stereotype is allowed");

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
          .should().beAnnotatedWith("org.springframework.stereotype.Service")
          .orShould().beAnnotatedWith("org.springframework.stereotype.Repository")
          .orShould().beAnnotatedWith("org.springframework.web.bind.annotation.RestController")
          .orShould()
          .beAnnotatedWith("org.springframework.boot.autoconfigure.SpringBootApplication");

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
  static final ArchRule core_should_not_have_external_annotations =
      noClasses()
          .that().resideInAPackage("..core..")
          .should().beAnnotatedWith("com.fasterxml.jackson.annotation.JsonProperty")
          .orShould().beAnnotatedWith("org.springframework.beans.factory.annotation.Autowired")
          .orShould().beAnnotatedWith("org.springframework.beans.factory.annotation.Value");

  @ArchTest
  static final ArchRule core_interfaces_should_be_implemented_in_adapters =
      classes()
          .that().resideInAPackage("..core..")
          .and().areInterfaces()
          .should().onlyHaveDependentClassesThat()
          .resideInAnyPackage("..adapters..", "..core..");

  @ArchTest
  static final ArchRule autowired_should_not_be_used_in_core =
      noClasses()
          .that().resideInAPackage("..core..")
          .should().beAnnotatedWith("org.springframework.beans.factory.annotation.Autowired");
}