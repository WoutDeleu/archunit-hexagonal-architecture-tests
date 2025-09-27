# 🏛️ ArchUnit Hexagonal Architecture Tests

[![Java](https://img.shields.io/badge/Java-17%2B-orange)](https://openjdk.org/)
[![Maven](https://img.shields.io/badge/Maven-3.6%2B-blue)](https://maven.apache.org/)
[![ArchUnit](https://img.shields.io/badge/ArchUnit-1.2.1-green)](https://www.archunit.org/)
[![License: BSD-3-Clause](https://img.shields.io/badge/License-BSD%203--Clause-blue.svg)](https://opensource.org/licenses/BSD-3-Clause)

A comprehensive collection of **ArchUnit tests** for enforcing **hexagonal architecture** (ports and adapters) patterns in Spring Boot applications. These tests ensure clean architecture boundaries and prevent architectural violations at build time.

> **✨ Latest Updates**: Generic cross-adapter isolation rules, cleaned duplicate tests, comprehensive package structure validation with self-dependency support, and empty repository compatibility!

## 🚀 Quick Start

### 1. Install in Existing Repository

For an existing Java Maven project, run this single command in your project root:

```bash
bash <(curl -s https://raw.githubusercontent.com/WoutDeleu/archunit-hexagonal-architecture-tests/main/setup-archunit-tests.sh)
```

This command will:
- Auto-detect your project's package structure
- Install all ArchUnit test files with correct package names
- Add ArchUnit and JUnit 5 dependencies to your pom.xml
- Create Maven Surefire plugin configuration if needed
- Generate a README with instructions
- Clean up temporary files automatically

**Advanced usage:**
```bash
# Install with custom package
bash <(curl -s https://raw.githubusercontent.com/WoutDeleu/archunit-hexagonal-architecture-tests/main/setup-archunit-tests.sh) -p com.mycompany.tests

# Update existing tests
bash <(curl -s https://raw.githubusercontent.com/WoutDeleu/archunit-hexagonal-architecture-tests/main/setup-archunit-tests.sh) -u
```

### 2. Manual Installation

If you prefer manual setup, follow these steps:

#### Add Dependencies

```xml
<dependencies>
    <!-- ArchUnit for architecture testing -->
    <dependency>
        <groupId>com.tngtech.archunit</groupId>
        <artifactId>archunit-junit5</artifactId>
        <version>1.2.1</version>
        <scope>test</scope>
    </dependency>

    <!-- JUnit 5 -->
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter</artifactId>
        <version>5.10.0</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

#### Copy Test Classes

Copy all test classes from `src/test/java/com/archunit/` to your project.

#### Update Package Configuration

Modify the `@AnalyzeClasses` annotation in each test class:

```java
@AnalyzeClasses(
    packages = "com.yourcompany.yourapp", // 👈 Update this
    importOptions = {
        ImportOption.DoNotIncludeTests.class,
        ImportOption.DoNotIncludeJars.class,
        ImportOption.DoNotIncludeArchives.class
    }
)
```

#### Run Tests

```bash
mvn test
```

## 🧊 ArchUnit Freeze Functionality (Recommended!)

One of ArchUnit's most powerful features is the **freeze functionality** that allows you to gradually improve your architecture without breaking existing builds.

### 🎯 Why Use Freeze?

When introducing ArchUnit tests to an existing codebase, you might have hundreds of violations. The freeze functionality allows you to:

- **✅ Lock in current violations** - Prevent new violations while allowing existing ones
- **✅ Gradual improvement** - Fix violations over time without breaking CI/CD
- **✅ Team adoption** - Introduce architecture rules without disrupting development
- **✅ Prevent regression** - Ensure existing violations don't get worse

### 🔧 How to Enable Freeze

Add this to your test classes:

```java
@AnalyzeClasses(packages = "com.yourcompany.yourapp")
public class YourArchitectureTest {

    @ArchTest
    static final ArchRule hexagonal_architecture_is_respected =
        layeredArchitecture("Hexagonal Architecture")
            .consideringAllDependencies()
            .layer("Core").definedBy("..core..")
            .layer("Adapters").definedBy("..adapters..")
            .layer("Infrastructure").definedBy("..infrastructure..")
            .whereLayer("Core").mayNotAccessAnyLayer()
            .whereLayer("Adapters").mayOnlyAccessLayers("Core", "Infrastructure")
            .whereLayer("Infrastructure").mayOnlyAccessLayers("Core")
            .because("hexagonal architecture must be respected")
            .allowEmptyShould(true)
            // 🧊 Add freeze functionality
            .as("hexagonal_architecture_is_respected");

    // Enable freeze store (choose one approach)
    static {
        ArchConfiguration.get().setProperty("freeze.store.default.path",
            "src/test/resources/archunit_store");
    }
}
```

### 📁 Freeze Store Options

#### 1. **File-based Store (Recommended for most projects)**
```java
@ArchTest
static final ArchRule rule = someRule()
    .as("my_architecture_rule"); // Rule name used for freeze file

static {
    ArchConfiguration.get().setProperty("freeze.store.default.path",
        "src/test/resources/archunit_store");
}
```

#### 2. **VCS Integration (Git-based)**
```java
static {
    ArchConfiguration.get().setProperty("freeze.store.default.allowStoreCreation", "true");
    ArchConfiguration.get().setProperty("freeze.store.default.path", "archunit_store");
}
```

### 🏃‍♂️ Workflow with Freeze

1. **Initial Setup**: Run tests - ArchUnit creates freeze files with current violations
2. **Development**: New violations cause test failures, existing ones are "frozen"
3. **Improvement**: Fix violations and delete corresponding freeze entries
4. **Team Adoption**: Everyone benefits from preventing new violations

### 📋 Example: Gradual Architecture Improvement

```java
// Before: 50 violations, build fails ❌
@ArchTest
static final ArchRule adapters_should_not_depend_on_each_other =
    noClasses().that().resideInAPackage("..adapters..")
        .should().dependOnClassesThat().resideInAPackage("..adapters..")
        .because("adapters should not depend on each other");

// After: Enable freeze ✅
@ArchTest
static final ArchRule adapters_should_not_depend_on_each_other =
    noClasses().that().resideInAPackage("..adapters..")
        .should().dependOnClassesThat().resideInAPackage("..adapters..")
        .because("adapters should not depend on each other")
        .as("adapters_isolation_rule"); // Enables freeze for this rule

// Result: Existing 50 violations frozen, new violations still fail build!
```

### ⚙️ Freeze Configuration

Add to your `archunit.properties` file:

```properties
# Enable freeze functionality
freeze.store.default.allowStoreCreation=true
freeze.store.default.path=src/test/resources/archunit_store

# Freeze behavior
freeze.refreeze=false                    # Don't automatically refreeze
freeze.store.default.allowStoreUpdate=true  # Allow manual updates
```

### 💡 Best Practices with Freeze

1. **Start with freeze enabled** when introducing ArchUnit to existing codebases
2. **Commit freeze files** to version control for team consistency
3. **Review freeze files** during code reviews to prevent new violations
4. **Set team goals** to gradually reduce frozen violations
5. **Use descriptive rule names** with `.as("rule_name")` for clear freeze files

## 🎯 Overview

This project provides a battle-tested set of reusable ArchUnit tests that validate hexagonal architecture patterns in Java applications. The tests enforce proper separation of concerns between core business logic, adapters, and infrastructure layers, helping maintain clean architecture principles.

## 🆕 Recent Improvements

- **🔄 Generic Cross-Adapter Isolation**: Comprehensive rules preventing any adapter type from depending on others (api, database, messaging, external, kafka, etc.) while allowing self-dependencies
- **🧹 Eliminated Duplicates**: Removed all duplicate test rules and organized into focused, logical test files
- **📦 Mandatory Package Structure**: Enforced structure for core (model, port, usecase, exceptions) and adapters (adapter, entity)
- **🌍 Flexible Root Packages**: SpringBoot application support for any depth (com.x.y.z.team.nameapp)
- **🔬 Empty Repository Compatible**: All tests now support empty codebases with `.allowEmptyShould(true)` for graceful defensive validation
- **🏷️ Attribution License**: BSD 3-Clause with proper creator attribution (Wout Deleu)
- **📋 37 Focused Tests**: Organized across 6 logical test files instead of scattered duplicates

## 🏗️ Architecture Layers

### 🔵 Core Layer (`..core..`)
The heart of your application containing pure business logic:
- **Domain entities and value objects** - Pure business objects
- **Use cases and business rules** - Application logic
- **Port interfaces** - Contracts for external dependencies
- **Domain exceptions** - Business-specific errors
- ✅ **Framework-agnostic** - No external dependencies
- ✅ **Database-agnostic** - No persistence concerns

### 🔄 Adapters Layer (`..adapters..`)
Implements communication with external systems:
- **API adapters** (`..adapters.api..`) - REST controllers, web endpoints
- **Database adapters** (`..adapters.database..`) - Repository implementations, JPA entities
- **Messaging adapters** (`..adapters.messaging..`) - Message handlers, publishers
- **External service adapters** - Third-party integrations
- ✅ **Implements core ports** - Follows dependency inversion
- ✅ **Isolated from each other** - No cross-adapter dependencies

### 🔧 Infrastructure Layer (`..infrastructure..`)
Cross-cutting concerns and configuration:
- **Configuration classes** - Spring Boot setup
- **Utilities** - Helper classes
- **Security configuration** - Authentication/authorization
- **Monitoring and logging** - Observability setup

## 📋 Enforced Package Structure

Each **core domain package** must follow this exact structure:

```
core/
├── {domain}/
│   ├── model/          ✅ Domain entities, value objects
│   ├── port/           ✅ Interface contracts
│   ├── usecase/        ✅ Business logic, application services
│   └── exceptions/     ✅ Domain-specific exceptions
```

Each **adapter type package** must follow this structure:

```
adapters/
├── {type}/
│   ├── adapter/        ✅ Implementation classes (*Adapter)
│   └── entity/         ✅ DTOs, external data structures
```

## 🧪 Test Categories

### 🔵 Core Layer Tests (`CoreLayerTest.java`)
Ensures core layer purity and framework independence:

- **`core_should_not_depend_on_spring_framework`** - No Spring dependencies
- **`core_should_not_depend_on_jpa`** - No JPA annotations
- **`core_should_not_depend_on_jackson`** - No JSON serialization
- **`core_should_not_depend_on_web_frameworks`** - No web dependencies
- **`core_should_not_depend_on_spring_util`** - No Spring utility classes
- **`core_should_not_use_spring_annotations`** - No @Component, @Service, etc.
- **`core_should_not_use_jpa_annotations`** - No @Entity, @Table, etc.
- **`core_entities_should_not_extend_adapter_classes`** - Domain isolation
- **`core_should_only_contain_business_logic`** - Limited external dependencies
- **`core_interfaces_should_be_implemented_in_adapters`** - Dependency inversion (allows infrastructure configuration)
- **`core_should_not_have_external_annotations`** - No framework annotations

### 🏛️ Hexagonal Architecture Tests (`HexagonalArchitectureTest.java`)
Validates overall architectural boundaries:

- **`hexagonal_architecture_is_respected`** - Layered architecture enforcement
- **`core_should_not_depend_on_adapters`** - Core isolation from adapters
- **`core_should_not_depend_on_infrastructure_except_stereotypes`** - Limited infrastructure access
- **`adapters_should_depend_on_core`** - Adapters use core interfaces
- **`adapters_should_not_depend_on_each_other`** - Adapter independence
- **`adapters_may_depend_on_infrastructure_for_utilities`** - Utility access allowed
- **`infrastructure_should_only_contain_config_and_utils`** - Infrastructure organization
- **`infrastructure_should_not_contain_business_logic`** - No business logic leakage

### 🔄 Adapter Tests (`AdapterTest.java`)
Enforces adapter layer structure and dependencies:

- **`adapters_should_implement_core_interfaces`** - Port implementation requirement
- **`api_adapters_should_be_in_api_package`** - Controller placement
- **`database_adapters_should_be_in_database_package`** - Repository placement
- **`repositories_should_not_be_in_core`** - Repository isolation
- **`controllers_should_not_be_in_core`** - Controller isolation
- **`adapters_should_not_directly_depend_on_each_other`** - Layer separation
- **`database_adapters_should_not_access_api_adapters`** - Prevents tight coupling
- **`adapters_should_only_access_core_through_interfaces`** - Interface compliance
- **`api_adapters_should_only_access_core_and_infrastructure`** - Dependency limits
- **`documentation_controller_should_be_in_infrastructure`** - Special controller handling

### 🔄 Adapter Structure Tests (`AdapterStructureTest.java`)
Validates adapter package organization:

- **`adapter_classes_should_implement_core_ports`** - Port interface implementation
- **`adapter_packages_contain_appropriate_classes`** - Adapter packages should contain classes ending with 'Adapter', 'Controller', or 'Repository'

### 🏛️ Layered Architecture Tests (`LayeredArchitectureTest.java`)
Validates overall architectural boundaries and cross-adapter isolation:

- **`hexagonal_architecture_is_respected`** - Layered architecture enforcement
- **`spring_boot_application_should_be_in_root`** - Main class placement *(mandatory)*
- **`adapters_should_depend_on_core`** - Adapters use core interfaces
- **`adapters_should_not_depend_on_other_adapter_types`** - Generic cross-adapter isolation (api, database, messaging, external, kafka, etc.) with self-dependency support

### 🧠 Core Domain Architecture Tests (`CoreDomainArchitectureTest.java`)
Ensures core layer purity and framework independence:

- **`core_should_not_depend_on_adapters`** - Core isolation from adapters
- **`core_should_not_depend_on_infrastructure_except_stereotypes`** - Limited infrastructure access
- **`core_should_not_depend_on_spring_framework`** - No Spring dependencies
- **`core_should_not_depend_on_jpa`** - No JPA annotations
- **`core_should_not_depend_on_jackson`** - No JSON serialization
- **`core_should_not_depend_on_web_frameworks`** - No web dependencies
- **`core_should_not_depend_on_spring_util`** - No Spring utility classes
- **`core_should_not_use_spring_annotations`** - No @Component, @Service, etc.
- **`core_should_not_use_jpa_annotations`** - No @Entity, @Table, etc.
- **`core_should_only_contain_business_logic`** - Limited external dependencies
- **`core_interfaces_should_be_implemented_in_adapters`** - Dependency inversion (allows infrastructure configuration)
- **`core_should_not_have_external_annotations`** - No framework annotations

### 🌐 API Adapter Architecture Tests (`ApiAdapterArchitectureTest.java`)
Validates API adapter implementations and web layer concerns:

- **`controllers_should_be_in_api_adapters`** - Spring controller placement
- **`controllers_should_not_be_in_core`** - Controller isolation
- **`api_adapters_should_not_use_database_adapters_directly`** - No direct database access
- **`controllers_should_use_core_port_interfaces`** - Interface usage (excludes DocumentationController)
- **`controllers_must_depend_on_port_interfaces`** - Dependency inversion (excludes DocumentationController)

### 🗄️ Database Adapter Architecture Tests (`DatabaseAdapterArchitectureTest.java`)
Validates database adapter implementations and persistence layer concerns:

- **`repositories_should_be_in_database_adapters`** - Spring repository placement
- **`repositories_should_not_be_in_core`** - Repository isolation
- **`jpa_entities_should_be_in_database_adapters`** - JPA entity placement
- **`entities_should_not_use_spring_annotations`** - JPA entity purity
- **`database_adapters_should_implement_core_interfaces`** - Database adapters implement core ports

### 🔧 Infrastructure Architecture Tests (`InfrastructureArchitectureTest.java`)
Validates infrastructure layer organization and configuration:

- **`infrastructure_should_only_contain_config_and_utils`** - Infrastructure organization (config, util, stereotype, web)
- **`infrastructure_should_not_contain_business_logic`** - No business logic in infrastructure
- **`services_should_be_in_adapters_only`** - @Service annotation only in adapters
- **`components_should_not_be_in_core`** - @Component restrictions
- **`autowired_should_not_be_used_in_core`** - No field injection in core
- **`documentation_controller_should_be_in_infrastructure`** - DocumentationController placement exception

## 📁 Recommended Package Structure

```
com.yourcompany.yourapp/
├── 📱 YourAppApplication.java           // @SpringBootApplication
│
├── 🔵 core/                             // Pure business logic
│   ├── user/
│   │   ├── model/                       // User, UserId, UserProfile
│   │   ├── port/                        // UserRepository, UserService
│   │   ├── usecase/                     // CreateUserUseCase, UpdateUserUseCase
│   │   └── exceptions/                  // UserNotFoundException
│   ├── product/
│   │   ├── model/                       // Product, ProductId, Price
│   │   ├── port/                        // ProductRepository, ProductService
│   │   ├── usecase/                     // CreateProductUseCase
│   │   └── exceptions/                  // ProductNotFoundException
│   └── order/
│       ├── model/                       // Order, OrderId, OrderStatus
│       ├── port/                        // OrderRepository, PaymentService
│       ├── usecase/                     // PlaceOrderUseCase
│       └── exceptions/                  // OrderValidationException
│
├── 🔄 adapters/                         // External communication
│   ├── api/
│   │   ├── adapter/                     // UserController, ProductController
│   │   └── entity/                      // UserRequest, ProductResponse
│   ├── database/
│   │   ├── adapter/                     // UserRepositoryImpl, ProductRepositoryImpl
│   │   └── entity/                      // UserEntity, ProductEntity (JPA)
│   ├── messaging/
│   │   ├── adapter/                     // OrderEventHandler, NotificationAdapter
│   │   └── entity/                      // OrderEventDto, NotificationDto
│   └── external/
│       ├── adapter/                     // PaymentServiceAdapter, EmailServiceAdapter
│       └── entity/                      // PaymentRequest, EmailRequest
│
└── 🔧 infrastructure/                   // Configuration & utilities
    ├── config/                          // DatabaseConfig, SecurityConfig
    ├── stereotype/                      // Custom annotations
    ├── util/                            // DateUtils, ValidationUtils
    └── web/                             // Global exception handlers
```

## ⚙️ Configuration Options

### Customizing Rules

You can customize the tests by:

**1. Excluding specific classes:**
```java
.and().doNotHaveSimpleName("DocumentationController")
```

**2. Allowing empty rules:**
```java
.allowEmptyShould(true)
```

**3. Adding custom package exclusions:**
```java
.and().resideOutsideOfPackages("com.sun..", "java..", "javax..")
```

### Framework Exclusions

The tests automatically exclude:
- ☑️ **System packages**: `java.*`, `javax.*`, `jakarta.*`
- ☑️ **Sun packages**: `com.sun.*`, `sun.*`
- ☑️ **Spring Data**: `org.springframework.data.*`
- ☑️ **GitHub packages**: `com.github.*`

## 🎯 Benefits

### 🛡️ **Architectural Integrity**
- Prevents accidental violations of clean architecture
- Enforces dependency direction (core ← adapters ← infrastructure)
- Maintains framework-agnostic core

### 🚨 **Early Detection**
- Catches violations at build time
- Fails CI/CD pipeline on architectural violations
- Prevents architectural debt accumulation

### 📚 **Living Documentation**
- Tests serve as executable architecture documentation
- Clear rules about what belongs where
- Self-documenting codebase structure

### 👥 **Team Consistency**
- Ensures all team members follow same patterns
- Reduces onboarding time for new developers
- Prevents architectural discussions during code reviews

### 🔄 **Refactoring Safety**
- Safe to refactor knowing architecture is protected
- Confidence when making large changes
- Automated validation of architectural decisions

## 🧪 Running Tests

### Run All Architecture Tests
```bash
mvn test
```

### Run Specific Test Categories
```bash
# Core domain purity tests
mvn test -Dtest=CoreDomainArchitectureTest

# API adapter tests
mvn test -Dtest=ApiAdapterArchitectureTest

# Database adapter tests
mvn test -Dtest=DatabaseAdapterArchitectureTest

# Infrastructure layer tests
mvn test -Dtest=InfrastructureArchitectureTest

# Overall layered architecture validation
mvn test -Dtest=LayeredArchitectureTest

# Adapter structure validation
mvn test -Dtest=AdapterStructureTest

# Run all adapter-related tests
mvn test -Dtest="*AdapterArchitectureTest"

# Run all tests (clean, organized, no duplicates)
mvn test
```

### CI/CD Integration

Add to your CI/CD pipeline:

```yaml
# GitHub Actions example
- name: Run Architecture Tests
  run: mvn test

# Fail build on architecture violations
- name: Check Test Results
  run: |
    if [ $? -ne 0 ]; then
      echo "❌ Architecture tests failed!"
      exit 1
    fi
    echo "✅ Architecture tests passed!"
```

## 🧪 Testing the Samples

This repository includes comprehensive samples that demonstrate the ArchUnit tests in action. You can experiment with both **valid hexagonal architecture** and **architectural violations** to see how the tests work.

### 📁 Sample Structure

The repository contains a complete **e-commerce application** following hexagonal architecture:

```
src/main/java/com/example/ecommerce/
├── EcommerceApplication.java              # ✅ Spring Boot app in root
├── core/                                  # ✅ Domain logic
│   ├── product/
│   │   ├── model/Product.java            # ✅ Pure domain entities
│   │   ├── port/ProductRepository.java   # ✅ Interface contracts
│   │   ├── usecase/ProductService.java   # ✅ Business logic
│   │   └── exceptions/                   # ✅ Domain exceptions
│   └── order/
│       ├── model/Order.java
│       ├── port/OrderRepository.java
│       ├── usecase/OrderService.java
│       └── exceptions/
├── adapters/                             # ✅ External interface implementations
│   ├── api/
│   │   ├── adapter/ProductController.java # ✅ REST controllers
│   │   └── entity/ProductDto.java         # ✅ API DTOs
│   ├── database/
│   │   ├── adapter/ProductRepositoryAdapter.java # ✅ JPA implementations
│   │   └── entity/ProductEntity.java      # ✅ JPA entities
│   ├── messaging/
│   │   └── adapter/NotificationServiceAdapter.java
│   └── external/
│       └── adapter/PaymentServiceAdapter.java
├── infrastructure/                       # ✅ Cross-cutting concerns
│   ├── config/DatabaseConfig.java        # ✅ Configuration
│   ├── util/DateTimeUtil.java           # ✅ Utilities
│   └── DocumentationController.java      # ✅ Special case (allowed)
└── violations/                           # ❌ Intentional violations for testing
    ├── BadCoreWithSpringAnnotations.java # ❌ Core with @Service
    ├── BadEntityWithSpringAnnotations.java # ❌ Entity with @Repository
    ├── BadControllerInCore.java          # ❌ Controller in wrong package
    ├── BadRepositoryInCore.java          # ❌ Repository in core
    └── BadCrossDependency.java           # ❌ Cross-adapter dependency
```

### 🧪 Running Sample Tests

#### 1. **Test Valid Architecture** ✅

Test the properly structured hexagonal architecture:

```bash
# Clone and test the current codebase
git clone https://github.com/WoutDeleu/archunit-hexagonal-architecture-tests.git
cd archunit-hexagonal-architecture-tests

# Run all tests - should PASS with some expected violations from 'violations/' package
mvn test

# Expected: Some tests fail due to intentional violations in 'violations/' package
```

#### 2. **Test Empty Repository** ✅

Demonstrate that tests work on empty projects:

```bash
# Test with empty codebase
cd empty-repo-test
mvn test

# Expected: All tests PASS ✅ (22 tests, 0 failures)
# This proves .allowEmptyShould(true) works correctly
```

#### 3. **Test Clean Architecture** ✅

Remove violations to see all tests pass:

```bash
# Temporarily remove violations
mv src/main/java/com/example/ecommerce/violations src/main/java/com/example/ecommerce/violations.backup

# Run tests
mvn test

# Expected: All tests PASS ✅
# Restore violations
mv src/main/java/com/example/ecommerce/violations.backup src/main/java/com/example/ecommerce/violations
```

### 📊 Expected Test Results

#### **With Violation Samples** (Default):
```bash
mvn test
# Results:
[ERROR] LayeredArchitectureTest.hexagonal_architecture_is_respected: 11 violations
- Cross-adapter dependencies detected ❌
- Core classes using Spring annotations ❌
- Controllers in wrong packages ❌
- Entities with Spring annotations ❌
```

#### **Empty Repository**:
```bash
cd empty-repo-test && mvn test
# Results:
[INFO] Tests run: 22, Failures: 0, Errors: 0, Skipped: 0 ✅
# All tests pass gracefully when no code exists
```

#### **Clean Architecture** (no violations):
```bash
# After removing violations/ directory
mvn test
# Results:
[INFO] Tests run: 22, Failures: 0, Errors: 0, Skipped: 0 ✅
# All tests pass with proper hexagonal architecture
```

### 🎯 Interactive Testing

Try these experiments to see the tests in action:

#### **1. Create a Violation**
```java
// Add this to core/product/model/Product.java
@Service  // ❌ This will be caught!
public class Product {
    // ...
}
```

Run: `mvn test -Dtest=CoreDomainArchitectureTest`
**Expected**: `core_should_not_use_spring_annotations` fails ❌

#### **2. Fix a Violation**
```java
// Remove @Repository from violations/BadEntityWithSpringAnnotations.java
// @Repository  // ✅ Remove this line
@Entity
public class BadEntityWithSpringAnnotations {
    // ...
}
```

Run: `mvn test -Dtest=DatabaseAdapterArchitectureTest`
**Expected**: `entities_should_not_use_spring_annotations` passes ✅

#### **3. Test Cross-Adapter Dependencies**
```java
// Try adding this import to any API adapter
import com.example.ecommerce.adapters.database.entity.ProductEntity; // ❌ Violation!
```

Run: `mvn test -Dtest=LayeredArchitectureTest`
**Expected**: `adapters_should_not_depend_on_other_adapter_types` fails ❌

### 🔍 Understanding Test Output

When tests fail, you'll see detailed violation reports:

```
Architecture Violation [Priority: MEDIUM] - Rule 'adapters should not depend on other adapter types' was violated (1 times):
Method <com.example.BadCrossDependency.convertEntity(ProductEntity)> has parameter of type <ProductEntity> in (BadCrossDependency.java:0)
                                                                       ^^^^^^^^^^^
                                          This shows exactly where the violation occurs
```

### 🚀 Integration with Your Project

To use these tests in your own project:

1. **Copy test files** to your `src/test/java/`
2. **Update pom.xml** with ArchUnit dependencies
3. **Run tests** to see current violations
4. **Fix violations** one by one
5. **Add to CI/CD** to prevent future violations

```bash
# Quick setup for existing project
cp -r src/test/java/com/archunit your-project/src/test/java/
# Update your pom.xml with ArchUnit dependencies
# Run: mvn test
```

The samples provide a complete reference implementation and testing environment to understand how hexagonal architecture should be structured and validated! 🎯

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-rule`)
3. Commit your changes (`git commit -m 'Add amazing architecture rule'`)
4. Push to the branch (`git push origin feature/amazing-rule`)
5. Open a Pull Request

## 📄 License

This project is licensed under the BSD 3-Clause License with Attribution Requirement - see the [LICENSE](LICENSE) file for details.

**Attribution Required**: When using this software, please include attribution to Wout Deleu as the original author.

## 👨‍💻 Author

**Wout Deleu** - *Original Author & Creator*

This comprehensive ArchUnit test suite was created to help development teams maintain clean hexagonal architecture in their Spring Boot applications.

## 🙏 Acknowledgments

- [ArchUnit](https://www.archunit.org/) - Fantastic architecture testing framework
- [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html) - Robert C. Martin's architectural principles
- [Hexagonal Architecture](https://alistair.cockburn.us/hexagonal-architecture/) - Alistair Cockburn's ports and adapters pattern

---

**💡 Tip**: Start with a small subset of rules and gradually add more as your team gets comfortable with the architectural constraints!