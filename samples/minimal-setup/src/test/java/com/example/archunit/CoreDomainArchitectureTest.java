package com.example.archunit;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

@AnalyzeClasses(
    packages = "com.example",
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
          .orShould().beAnnotatedWith("org.springframework.data.repository.Repository")
          .allowEmptyShould(true);

  @ArchTest
  static final ArchRule core_should_not_depend_on_adapters =
      noClasses()
          .that().resideInAPackage("..core..")
          .should().dependOnClassesThat()
          .resideInAnyPackage("..adapters..")
          .allowEmptyShould(true);

  @ArchTest
  static final ArchRule core_should_not_depend_on_infrastructure_config_or_util =
      noClasses()
          .that().resideInAPackage("..core..")
          .should().dependOnClassesThat()
          .resideInAnyPackage("..infrastructure.config..", "..infrastructure.util..")
          .allowEmptyShould(true)
          .because(
              "Core must not depend on infrastructure config or util packages; only infrastructure.stereotype is allowed");

  @ArchTest
  static final ArchRule core_should_not_depend_on_spring_framework =
      noClasses()
          .that().resideInAPackage("..core..")
          .should().dependOnClassesThat()
          .resideInAnyPackage("org.springframework..", "org.springframework.boot..")
          .allowEmptyShould(true);

  @ArchTest
  static final ArchRule core_should_not_depend_on_jpa =
      noClasses()
          .that().resideInAPackage("..core..")
          .should().dependOnClassesThat()
          .resideInAnyPackage("jakarta.persistence..", "javax.persistence..", "org.hibernate..")
          .allowEmptyShould(true);

  @ArchTest
  static final ArchRule core_should_not_depend_on_jackson =
      noClasses()
          .that().resideInAPackage("..core..")
          .should().dependOnClassesThat()
          .resideInAnyPackage("com.fasterxml.jackson..")
          .allowEmptyShould(true);

  @ArchTest
  static final ArchRule core_should_not_depend_on_web_frameworks =
      noClasses()
          .that().resideInAPackage("..core..")
          .should().dependOnClassesThat()
          .resideInAnyPackage("jakarta.servlet..", "javax.servlet..", "org.springframework.web..")
          .allowEmptyShould(true);

  @ArchTest
  static final ArchRule core_should_not_use_spring_annotations =
      noClasses()
          .that().resideInAPackage("..core..")
          .should().beAnnotatedWith("org.springframework.stereotype.Service")
          .orShould().beAnnotatedWith("org.springframework.stereotype.Repository")
          .orShould().beAnnotatedWith("org.springframework.web.bind.annotation.RestController")
          .orShould()
          .beAnnotatedWith("org.springframework.boot.autoconfigure.SpringBootApplication")
          .allowEmptyShould(true);

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
          .orShould().beAnnotatedWith("javax.persistence.Column")
          .allowEmptyShould(true);

  @ArchTest
  static final ArchRule core_should_not_have_external_annotations =
      noClasses()
          .that().resideInAPackage("..core..")
          .should().beAnnotatedWith("com.fasterxml.jackson.annotation.JsonProperty")
          .orShould().beAnnotatedWith("org.springframework.beans.factory.annotation.Autowired")
          .orShould().beAnnotatedWith("org.springframework.beans.factory.annotation.Value")
          .allowEmptyShould(true);


  @ArchTest
  static final ArchRule autowired_should_not_be_used_in_core =
      noClasses()
          .that().resideInAPackage("..core..")
          .should().beAnnotatedWith("org.springframework.beans.factory.annotation.Autowired")
          .allowEmptyShould(true);
}