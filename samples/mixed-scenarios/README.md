# Mixed Scenarios Sample

This sample demonstrates a **realistic codebase** with both good hexagonal architecture and legacy violations, perfect for showcasing **ArchUnit freeze functionality**.

## 📁 Structure

```
mixed-scenarios/
├── src/main/java/com/example/
│   ├── MixedApplication.java                       # ✅ Spring Boot app in root
│   ├── core/product/                               # ✅ Good hexagonal architecture
│   │   ├── model/Product.java                      # ✅ Pure domain entity
│   │   ├── port/ProductRepository.java             # ✅ Port interface
│   │   └── usecase/ProductService.java             # ✅ Business logic
│   ├── adapters/                                   # ✅ Proper adapters
│   │   ├── api/adapter/ProductController.java      # ✅ Controller in right place
│   │   └── database/
│   │       ├── adapter/ProductRepositoryAdapter.java # ✅ Repository adapter
│   │       └── entity/ProductEntity.java           # ✅ Entity in right place
│   ├── infrastructure/config/ApplicationConfig.java # ✅ Configuration
│   └── legacy/                                     # ❌ Legacy violations
│       ├── controllers/LegacyOrderController.java  # ❌ Controller in wrong place
│       └── services/LegacyOrderService.java        # ❌ Service in wrong place
└── src/test/java/com/example/archunit/            # ✅ ArchUnit tests
    ├── LayeredArchitectureTest.java
    ├── CoreDomainArchitectureTest.java
    ├── AdapterStructureTest.java
    ├── ApiAdapterArchitectureTest.java
    ├── DatabaseAdapterArchitectureTest.java
    └── InfrastructureArchitectureTest.java
```

## 🎯 Purpose

- **Realistic Scenario**: Shows how real codebases mix good and legacy code
- **Freeze Demonstration**: Perfect for showcasing ArchUnit freeze functionality
- **Migration Strategy**: Template for gradually improving legacy codebases

## 🧪 Running the Tests

### Without Freeze (Tests Will Fail)
```bash
cd samples/mixed-scenarios
mvn test
```

### With Freeze (Recommended)
```bash
cd samples/mixed-scenarios

# Enable freeze in test files by adding .as("rule_name") to failing rules
# See freeze configuration section below
mvn test
```

## ❌ Expected Results (Without Freeze)

Some tests should **FAIL** due to legacy violations:

```
[ERROR] Tests run: 37, Failures: 3, Errors: 0, Skipped: 0

[ERROR] ApiAdapterArchitectureTest.controllers_should_be_in_api_adapters:
Architecture Violation: Controllers should be in API adapters package
- Found: LegacyOrderController in legacy.controllers package

[ERROR] InfrastructureArchitectureTest.services_should_be_in_adapters_only:
Architecture Violation: @Service should only be in adapters
- Found: @Service on LegacyOrderService in legacy.services
```

## 🧊 Freeze Configuration Example

Update test files to freeze legacy violations:

```java
// In ApiAdapterArchitectureTest.java
@ArchTest
static final ArchRule controllers_should_be_in_api_adapters =
    classes().that().areAnnotatedWith(RestController.class)
        .should().resideInAPackage("..adapters.api.adapter..")
        .because("controllers should be in API adapters package")
        .as("controllers_in_api_adapters_rule");  // 🧊 Enable freeze

// In InfrastructureArchitectureTest.java
@ArchTest
static final ArchRule services_should_be_in_adapters_only =
    classes().that().areAnnotatedWith(Service.class)
        .should().resideInAPackage("..adapters..")
        .because("@Service annotation should only be in adapters")
        .as("services_in_adapters_rule");  // 🧊 Enable freeze

// Add freeze configuration
static {
    ArchConfiguration.get().setProperty("freeze.store.default.path",
        "src/test/resources/archunit_store");
}
```

## ✅ Results After Freeze

```
[INFO] Tests run: 37, Failures: 0, Errors: 0, Skipped: 0

# Freeze files created:
src/test/resources/archunit_store/
├── controllers_in_api_adapters_rule.txt
└── services_in_adapters_rule.txt
```

## 🔍 Architecture Analysis

### ✅ Good Architecture (Products)
- **Core domain purity**: No framework dependencies
- **Proper package structure**: `core.product.{model,port,usecase}`
- **Correct adapter placement**: `adapters.{api,database}.{adapter,entity}`
- **Clean dependencies**: Core ← Adapters ← Infrastructure

### ❌ Legacy Violations (Orders)
- **Controllers in wrong package**: `legacy.controllers` instead of `adapters.api.adapter`
- **Services in wrong package**: `legacy.services` instead of `adapters`
- **Business logic in wrong place**: Should be in core domain

### 🧊 Freeze Strategy
1. **Freeze legacy violations** to prevent new ones
2. **Gradually refactor** legacy code to proper structure
3. **Remove freeze entries** as violations are fixed
4. **Maintain clean architecture** for new features

## 💡 Migration Workflow

1. **Enable freeze** for all current violations
2. **New features** must follow hexagonal architecture (tests will fail for new violations)
3. **Gradual refactoring** of legacy code
4. **Remove freeze entries** as legacy code is cleaned up

## 🚀 Use Case

This sample is perfect for:
- **Legacy codebase migration** to hexagonal architecture
- **Demonstrating freeze functionality** in realistic scenarios
- **Training teams** on gradual architecture improvement
- **CI/CD integration** with freeze to prevent regression