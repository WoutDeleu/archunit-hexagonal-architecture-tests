package com.example.archunit;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.Dependency;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;
import static com.tngtech.archunit.base.DescribedPredicate.describe;

@AnalyzeClasses(
    packages = "com.example",
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
            .allowEmptyShould(true)
            .because("@SpringBootApplication class must exist and be in root package (e.g., com.x.y.z.team.nameapp) but outside hexagonal architecture layers");

    @ArchTest
    static void hexagonal_architecture_is_respected(JavaClasses classes) {
        // Check if we have classes in both core and adapters layers
        boolean hasCoreClasses = classes.stream().anyMatch(javaClass ->
            javaClass.getPackageName().contains(".core."));
        boolean hasAdapterClasses = classes.stream().anyMatch(javaClass ->
            javaClass.getPackageName().contains(".adapters."));
        boolean hasInfrastructureClasses = classes.stream().anyMatch(javaClass ->
            javaClass.getPackageName().contains(".infrastructure."));

        // Only apply layered architecture rules if we have classes in at least two layers
        if ((hasCoreClasses && hasAdapterClasses) ||
            (hasCoreClasses && hasInfrastructureClasses) ||
            (hasAdapterClasses && hasInfrastructureClasses)) {
            layeredArchitecture()
                .consideringAllDependencies()
                .layer("Core").definedBy("..core..")
                .layer("Adapters").definedBy("..adapters..")
                .layer("Infrastructure").definedBy("..infrastructure..")
                .whereLayer("Core").mayOnlyBeAccessedByLayers("Adapters", "Infrastructure")
                .whereLayer("Adapters").mayNotBeAccessedByAnyLayer()
                .ignoreDependency(
                    describe("Application root classes", clazz -> clazz.getPackageName().equals("com.example")),
                    describe("Core classes", clazz -> clazz.getPackageName().contains(".core."))
                )
                .check(classes);
        }
    }

    @ArchTest
    static final ArchRule adapters_should_depend_on_core =
        classes()
            .that().resideInAPackage("..adapters..")
            .should().dependOnClassesThat()
            .resideInAPackage("..core..")
            .orShould().dependOnClassesThat()
            .resideOutsideOfPackages("..adapters..", "..infrastructure..")
            .allowEmptyShould(true);

    @ArchTest
    static final ArchRule adapters_should_not_depend_on_other_adapter_types =
        classes()
            .that().resideInAPackage("..adapters..")
            .should(notDependOnOtherAdapterTypes())
            .allowEmptyShould(true)
            .because("Adapters should not depend on other adapter types - each adapter type (api, database, messaging, external, etc.) should only depend on classes within their own adapter type, core, infrastructure, and standard libraries");

    // Custom condition to check cross-adapter dependencies
    private static ArchCondition<JavaClass> notDependOnOtherAdapterTypes() {
        return new ArchCondition<JavaClass>("not depend on other adapter types") {
            @Override
            public void check(JavaClass javaClass, ConditionEvents events) {
                String sourceAdapterType = extractAdapterType(javaClass.getPackageName());

                for (Dependency dependency : javaClass.getDirectDependenciesFromSelf()) {
                    JavaClass targetClass = dependency.getTargetClass();
                    String targetPackage = targetClass.getPackageName();

                    // Skip if target is not in adapters package
                    if (!targetPackage.contains(".adapters.")) {
                        continue;
                    }

                    String targetAdapterType = extractAdapterType(targetPackage);

                    // Allow same adapter type dependencies
                    if (sourceAdapterType != null && sourceAdapterType.equals(targetAdapterType)) {
                        continue;
                    }

                    // Violation: different adapter types
                    if (sourceAdapterType != null && targetAdapterType != null &&
                        !sourceAdapterType.equals(targetAdapterType)) {
                        events.add(SimpleConditionEvent.violated(dependency,
                            String.format("Class %s in adapter type '%s' depends on class %s in adapter type '%s'",
                                javaClass.getName(), sourceAdapterType,
                                targetClass.getName(), targetAdapterType)));
                    }
                }
            }
        };
    }

    // Helper method to extract adapter type from package name
    private static String extractAdapterType(String packageName) {
        if (!packageName.contains(".adapters.")) return null;

        String afterAdapters = packageName.substring(packageName.indexOf(".adapters.") + 10);
        int dotIndex = afterAdapters.indexOf('.');

        return dotIndex > 0 ? afterAdapters.substring(0, dotIndex) : afterAdapters;
    }

}