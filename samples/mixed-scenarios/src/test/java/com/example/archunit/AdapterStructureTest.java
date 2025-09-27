package com.example.archunit;

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
public class AdapterStructureTest {

  @ArchTest
  static final ArchRule adapter_classes_should_implement_core_ports =
      classes()
          .that()
          .resideInAPackage("..adapters.*.adapter..")
          .and()
          .haveSimpleNameEndingWith("Adapter")
          .or()
          .haveSimpleNameEndingWith("Controller")
          .and()
          .haveSimpleNameNotEndingWith("DocumentationController")
          .should()
          .dependOnClassesThat()
          .resideInAPackage("..core.*.port..")
          .allowEmptyShould(true)
          .because("Adapter classes should implement interfaces from core ports");

  @ArchTest
  static final ArchRule adapter_packages_contain_appropriate_classes =
      classes()
          .that()
          .resideInAPackage("..adapters.*.adapter..")
          .should()
          .haveSimpleNameEndingWith("Adapter")
          .orShould()
          .haveSimpleNameEndingWith("Controller")
          .orShould()
          .haveSimpleNameEndingWith("Repository")
          .allowEmptyShould(true)
          .because("Classes in adapter packages should end with 'Adapter', 'Controller', or 'Repository'");

}
