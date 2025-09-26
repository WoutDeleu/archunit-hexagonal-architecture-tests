# 🏛️ ArchUnit Hexagonal Architecture Tests

[![Java](https://img.shields.io/badge/Java-17%2B-orange)](https://openjdk.org/)
[![Maven](https://img.shields.io/badge/Maven-3.6%2B-blue)](https://maven.apache.org/)
[![ArchUnit](https://img.shields.io/badge/ArchUnit-1.2.1-green)](https://www.archunit.org/)
[![License: BSD-3-Clause](https://img.shields.io/badge/License-BSD%203--Clause-blue.svg)](https://opensource.org/licenses/BSD-3-Clause)

A comprehensive collection of **ArchUnit tests** for enforcing **hexagonal architecture** (ports and adapters) patterns in Spring Boot applications. These tests ensure clean architecture boundaries and prevent architectural violations at build time.

> **✨ Latest Updates**: Generic cross-adapter isolation rules, cleaned duplicate tests, and comprehensive package structure validation with self-dependency support!

## 🎯 Overview

This project provides a battle-tested set of reusable ArchUnit tests that validate hexagonal architecture patterns in Java applications. The tests enforce proper separation of concerns between core business logic, adapters, and infrastructure layers, helping maintain clean architecture principles.

## 🆕 Recent Improvements

- **🔄 Generic Cross-Adapter Isolation**: Comprehensive rules preventing any adapter type from depending on others (api, database, messaging, external, kafka, etc.) while allowing self-dependencies
- **🧹 Eliminated Duplicates**: Removed all duplicate test rules and organized into focused, logical test files
- **📦 Mandatory Package Structure**: Enforced structure for core (model, port, usecase, exceptions) and adapters (adapter, entity)
- **🌍 Flexible Root Packages**: SpringBoot application support for any depth (com.x.y.z.team.nameapp)
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
- **`core_interfaces_should_be_implemented_in_adapters`** - Dependency inversion
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
- **`each_adapter_package_should_have_at_least_one_adapter_class`** - Mandatory adapter classes

### 🏛️ Layered Architecture Tests (`LayeredArchitectureTest.java`)
Validates overall architectural boundaries and cross-adapter isolation:

- **`hexagonal_architecture_is_respected`** - Layered architecture enforcement
- **`spring_boot_application_should_be_in_root`** - Main class placement *(mandatory)*
- **`adapters_should_depend_on_core`** - Adapters use core interfaces
- **`api_adapters_should_not_depend_on_other_adapters`** - API isolation from database/messaging/external/kafka
- **`database_adapters_should_not_depend_on_other_adapters`** - Database isolation from api/messaging/external/kafka
- **`messaging_adapters_should_not_depend_on_other_adapters`** - Messaging isolation from api/database/external/kafka
- **`external_adapters_should_not_depend_on_other_adapters`** - External service isolation from api/database/messaging/kafka
- **`kafka_adapters_should_not_depend_on_other_adapters`** - Kafka isolation from api/database/messaging/external

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
- **`core_interfaces_should_be_implemented_in_adapters`** - Dependency inversion
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


## 🚀 Quick Start

### 1. Add Dependencies

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

### 2. Copy Test Classes

Copy all test classes from `src/test/java/com/archunit/hexagonal/` to your project.

### 3. Update Package Configuration

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

### 4. Run Tests

```bash
mvn test
```

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