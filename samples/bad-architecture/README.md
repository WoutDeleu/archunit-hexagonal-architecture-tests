# Bad Architecture Sample

This sample demonstrates **architectural violations** that will cause ArchUnit tests to fail, showing exactly what NOT to do.

## 📁 Structure

```
bad-architecture/
├── src/main/java/com/example/
│   ├── services/
│   │   ├── BadApplication.java                     # ❌ Spring Boot app NOT in root
│   │   └── BusinessLogicInInfrastructure.java      # ❌ Business logic in wrong place
│   ├── core/user/
│   │   ├── User.java                               # ❌ Core entity with Spring/JPA annotations
│   │   └── UserService.java                       # ❌ Core service with Spring annotations
│   ├── controllers/
│   │   └── UserController.java                    # ❌ Controller NOT in adapters.api
│   └── repositories/
│       └── UserRepository.java                    # ❌ Repository NOT in adapters.database
└── src/test/java/com/example/archunit/            # ✅ ArchUnit tests (will fail!)
    ├── LayeredArchitectureTest.java
    ├── CoreDomainArchitectureTest.java
    ├── AdapterStructureTest.java
    ├── ApiAdapterArchitectureTest.java
    ├── DatabaseAdapterArchitectureTest.java
    └── InfrastructureArchitectureTest.java
```

## 🎯 Purpose

- **Educational**: Shows common architectural mistakes
- **Validation**: Proves that ArchUnit tests catch violations
- **Training**: Helps teams understand what to avoid

## 🧪 Running the Tests

```bash
cd samples/bad-architecture
mvn test
```

## ❌ Expected Results

Many tests should **FAIL** with output showing violations:

```
[ERROR] Tests run: 37, Failures: 12, Errors: 0, Skipped: 0

[ERROR] LayeredArchitectureTest.spring_boot_application_should_be_in_root:
Architecture Violation: Spring Boot application should be in root package
- Found: com.example.services.BadApplication

[ERROR] CoreDomainArchitectureTest.core_should_not_use_spring_annotations:
Architecture Violation: Core should not use Spring annotations
- Found: @Component on User
- Found: @Service on UserService

[ERROR] ApiAdapterArchitectureTest.controllers_should_be_in_api_adapters:
Architecture Violation: Controllers should be in API adapters package
- Found: UserController in controllers package
```

## 🚨 Violations Demonstrated

### ❌ Wrong Package Structure
- **Spring Boot app in services/** instead of root package
- **Controller in controllers/** instead of `adapters.api.adapter`
- **Repository in repositories/** instead of `adapters.database.adapter`

### ❌ Core Domain Violations
- **Spring annotations in core** (`@Component`, `@Service`, `@Autowired`)
- **JPA annotations in core** (`@Entity`, `@Table`, `@Id`)
- **Core depending on adapters** (UserService using UserRepository directly)
- **Framework dependencies in core** (Spring, JPA imports)

### ❌ Cross-Adapter Dependencies
- **Controller directly accessing repository** (API adapter → Database adapter)
- **No proper port interfaces** (direct implementation dependencies)

### ❌ Infrastructure Violations
- **Business logic in infrastructure** package
- **@Service annotations outside adapters**

## 🔍 Key Anti-Patterns

### ❌ Contaminated Core
```java
@Service  // ❌ Spring annotation in core!
@Entity   // ❌ JPA annotation in core!
public class User {
    @Autowired  // ❌ Field injection in core!
    private UserRepository repo;  // ❌ Core depending on adapter!
}
```

### ❌ Wrong Package Organization
```java
// ❌ Controller in wrong package
package com.example.controllers;  // Should be: adapters.api.adapter

// ❌ Repository in wrong package
package com.example.repositories;  // Should be: adapters.database.adapter
```

### ❌ Cross-Adapter Dependencies
```java
public class UserController {
    private UserRepository repository;  // ❌ API → Database direct dependency!
}
```

## 💡 Use Case

This sample is perfect for:
- **Understanding violations** - See exactly what ArchUnit catches
- **Team training** - Show developers what NOT to do
- **Testing ArchUnit** - Verify that rules work correctly
- **Before/after comparison** - Compare with good-hexagonal sample