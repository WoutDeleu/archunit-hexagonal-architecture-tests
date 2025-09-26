package com.archunit.hexagonal;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

@AnalyzeClasses(
    packages = "com",
    importOptions = {
        ImportOption.DoNotIncludeTests.class,
        ImportOption.DoNotIncludeJars.class,
        ImportOption.DoNotIncludeArchives.class
    }
)
public class LayeredArchitectureTest {
    @ArchTest
    static final ArchRule spring_boot_application_should_be_in_root =
        classes()
            .that().areAnnotatedWith("org.springframework.boot.autoconfigure.SpringBootApplication")
            .should().resideOutsideOfPackages("..core..", "..adapters..", "..infrastructure..")
            .because("@SpringBootApplication class must exist and be in root package (e.g., com.x.y.z.team.nameapp) but outside hexagonal architecture layers");

    @ArchTest
    static final ArchRule hexagonal_architecture_is_respected = layeredArchitecture()
            .consideringAllDependencies()

            .layer("Core").definedBy("..core..")
            .layer("Adapters").definedBy("..adapters..")
            .layer("Infrastructure").definedBy("..infrastructure..")

            .whereLayer("Core").mayOnlyBeAccessedByLayers("Adapters")
            .whereLayer("Adapters").mayNotBeAccessedByAnyLayer();

    @ArchTest
    static final ArchRule adapters_should_depend_on_core =
        classes()
            .that().resideInAPackage("..adapters..")
            .should().dependOnClassesThat()
            .resideInAPackage("..core..")
            .orShould().dependOnClassesThat()
            .resideOutsideOfPackages("..adapters..", "..infrastructure..");

    @ArchTest
    static final ArchRule api_adapters_should_not_depend_on_other_adapters =
        noClasses()
            .that().resideInAPackage("..adapters.api..")
            .should().dependOnClassesThat()
            .resideInAnyPackage("..adapters.database..", "..adapters.messaging..", "..adapters.external..", "..adapters.kafka..")
            .allowEmptyShould(true)
            .because("API adapters should not depend on other adapter types (database, messaging, external, kafka, etc.)");

    @ArchTest
    static final ArchRule database_adapters_should_not_depend_on_other_adapters =
        noClasses()
            .that().resideInAPackage("..adapters.database..")
            .should().dependOnClassesThat()
            .resideInAnyPackage("..adapters.api..", "..adapters.messaging..", "..adapters.external..", "..adapters.kafka..")
            .allowEmptyShould(true)
            .because("Database adapters should not depend on other adapter types (api, messaging, external, kafka, etc.)");

    @ArchTest
    static final ArchRule messaging_adapters_should_not_depend_on_other_adapters =
        noClasses()
            .that().resideInAPackage("..adapters.messaging..")
            .should().dependOnClassesThat()
            .resideInAnyPackage("..adapters.api..", "..adapters.database..", "..adapters.external..", "..adapters.kafka..")
            .allowEmptyShould(true)
            .because("Messaging adapters should not depend on other adapter types (api, database, external, kafka, etc.)");

    @ArchTest
    static final ArchRule external_adapters_should_not_depend_on_other_adapters =
        noClasses()
            .that().resideInAPackage("..adapters.external..")
            .should().dependOnClassesThat()
            .resideInAnyPackage("..adapters.api..", "..adapters.database..", "..adapters.messaging..", "..adapters.kafka..")
            .allowEmptyShould(true)
            .because("External adapters should not depend on other adapter types (api, database, messaging, kafka, etc.)");

    @ArchTest
    static final ArchRule kafka_adapters_should_not_depend_on_other_adapters =
        noClasses()
            .that().resideInAPackage("..adapters.kafka..")
            .should().dependOnClassesThat()
            .resideInAnyPackage("..adapters.api..", "..adapters.database..", "..adapters.messaging..", "..adapters.external..")
            .allowEmptyShould(true)
            .because("Kafka adapters should not depend on other adapter types (api, database, messaging, external, etc.)");

}