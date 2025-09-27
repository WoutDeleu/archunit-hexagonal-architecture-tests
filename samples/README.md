# ArchUnit Hexagonal Architecture Samples

This directory contains **6 comprehensive samples** demonstrating different scenarios with ArchUnit tests and hexagonal architecture patterns.

## 📚 Sample Overview

| Sample | Purpose | Test Results | Use Case |
|--------|---------|--------------|----------|
| 🏗️ **[empty-repo](empty-repo/)** | Empty repository validation | ✅ All Pass | New projects, CI/CD safety |
| ✅ **[good-hexagonal](good-hexagonal/)** | Perfect architecture | ✅ All Pass | Reference implementation |
| ❌ **[bad-architecture](bad-architecture/)** | Common violations | ❌ Many Fail | Training, violation examples |
| 🚀 **[minimal-setup](minimal-setup/)** | Minimal Spring Boot setup | ✅ All Pass | Getting started, templates |
| 🔄 **[mixed-scenarios](mixed-scenarios/)** | Realistic codebase | ⚠️ Some Fail | Freeze demo, legacy migration |

## 🎯 Quick Start

Choose your scenario:

```bash
# 1. See tests pass on empty repository
cd empty-repo && mvn test

# 2. See perfect hexagonal architecture
cd good-hexagonal && mvn test

# 3. See violations and failures
cd bad-architecture && mvn test

# 4. Start from minimal setup
cd minimal-setup && mvn test

# 5. Try freeze functionality
cd mixed-scenarios && mvn test
```

## 📋 Sample Details

### 🏗️ Empty Repository Sample
**Perfect for**: New projects, CI/CD integration

- **Structure**: Only ArchUnit tests, no source code
- **Results**: All tests pass with `.allowEmptyShould(true)`
- **Key Learning**: Tests don't break on empty projects

### ✅ Good Hexagonal Sample
**Perfect for**: Learning best practices, reference implementation

- **Structure**: Complete e-commerce bookstore with perfect hexagonal architecture
- **Results**: All 37 tests pass ✅
- **Key Learning**: How hexagonal architecture should be implemented

**Features**:
- Pure core domain (Book, BookId, BookService)
- Proper adapters (API, Database)
- Clean infrastructure (Configuration, Utilities)
- No cross-layer violations

### ❌ Bad Architecture Sample
**Perfect for**: Training, understanding violations

- **Structure**: Intentionally broken architecture with multiple violations
- **Results**: Many tests fail ❌
- **Key Learning**: What NOT to do

**Violations Demonstrated**:
- Spring Boot app in wrong package
- Core entities with Spring/JPA annotations
- Controllers in wrong package
- Cross-adapter dependencies
- Business logic in infrastructure

### 🚀 Minimal Setup Sample
**Perfect for**: Getting started, project templates

- **Structure**: Minimal Spring Boot app with only ArchUnit tests
- **Results**: All tests pass ✅
- **Key Learning**: Minimal setup to get started

**Growth Path**:
1. Add core domain packages
2. Add adapter packages
3. Add infrastructure packages
4. Tests ensure proper structure

### 🔄 Mixed Scenarios Sample
**Perfect for**: Freeze demonstration, legacy migration

- **Structure**: Realistic mix of good hexagonal architecture and legacy violations
- **Results**: Some tests fail without freeze ⚠️
- **Key Learning**: How to use freeze for gradual improvement

**Architecture Mix**:
- ✅ **Good**: Product domain with proper hexagonal architecture
- ❌ **Legacy**: Order functionality with package violations
- 🧊 **Freeze**: Prevent new violations while allowing existing ones

## 🧊 Freeze Functionality Demonstration

The mixed-scenarios sample is perfect for demonstrating ArchUnit's freeze functionality:

### Without Freeze
```bash
cd mixed-scenarios
mvn test
# Result: Tests fail due to legacy violations ❌
```

### With Freeze
```java
// Add to test classes:
@ArchTest
static final ArchRule controllers_should_be_in_api_adapters =
    classes().that().areAnnotatedWith(RestController.class)
        .should().resideInAPackage("..adapters.api.adapter..")
        .as("controllers_in_api_adapters_rule");  // 🧊 Enable freeze

static {
    ArchConfiguration.get().setProperty("freeze.store.default.path",
        "src/test/resources/archunit_store");
}
```

```bash
mvn test
# Result: All tests pass ✅ (legacy violations frozen)
```

## 🎓 Learning Path

1. **Start with empty-repo** - Understand basic setup
2. **Study good-hexagonal** - Learn proper patterns
3. **Analyze bad-architecture** - Understand violations
4. **Try minimal-setup** - Practice from scratch
5. **Experiment with mixed-scenarios** - Learn freeze functionality

## 🔧 Running All Samples

Test all samples at once:

```bash
# Run tests for all samples
for sample in empty-repo good-hexagonal bad-architecture minimal-setup mixed-scenarios; do
    echo "Testing $sample..."
    cd $sample && mvn test && cd ..
done
```

## 📝 Key Takeaways

### ✅ What Good Architecture Looks Like
- **Core domain**: Pure business logic, no framework dependencies
- **Adapters**: Implement core ports, isolated from each other
- **Infrastructure**: Configuration and utilities only
- **Dependencies**: Core ← Adapters ← Infrastructure

### ❌ Common Violations to Avoid
- Framework annotations in core domain
- Controllers outside API adapters
- Repositories outside database adapters
- Cross-adapter dependencies
- Business logic in infrastructure

### 🧊 Freeze Best Practices
- Enable freeze for legacy codebases
- Prevent new violations while allowing existing ones
- Gradually fix violations and remove freeze entries
- Use descriptive rule names with `.as("rule_name")`

## 🚀 Next Steps

1. **Copy relevant sample** to your project structure
2. **Run ArchUnit tests** to see current state
3. **Enable freeze** for existing violations if needed
4. **Implement hexagonal architecture** for new features
5. **Gradually refactor** legacy code to proper structure

Each sample includes detailed README with specific instructions and expected results!