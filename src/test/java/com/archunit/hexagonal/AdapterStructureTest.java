package com.archunit.hexagonal;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

@AnalyzeClasses(packages = "com", importOptions = ImportOption.DoNotIncludeTests.class)
public class AdapterStructureTest {

  @ArchTest
  static final ArchRule adapter_classes_should_implement_core_ports =
      classes()
          .that()
          .resideInAPackage("..adapters.*.adapter..")
          .and()
          .haveSimpleNameEndingWith("Adapter")
          .should()
          .dependOnClassesThat()
          .resideInAPackage("..core.*.port..")
          .because("Adapter classes should implement interfaces from core ports");

  @ArchTest
  static final ArchRule each_adapter_package_should_have_at_least_one_adapter_class =
      classes()
          .that()
          .resideInAPackage("..adapters.*.adapter..")
          .should()
          .haveSimpleNameEndingWith("Adapter")
          .because("Each adapter package must contain at least one class ending with 'Adapter'");

}
