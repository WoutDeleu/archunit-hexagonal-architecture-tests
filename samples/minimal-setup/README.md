# Minimal Setup Sample

This sample demonstrates the **smallest possible setup** to get started with ArchUnit tests in a Spring Boot project.

## 📁 Structure

```
minimal-setup/
├── pom.xml                                         # ✅ Basic Maven config + ArchUnit
├── src/main/java/com/example/
│   └── MinimalApplication.java                     # ✅ Spring Boot app in root
└── src/test/java/com/example/archunit/            # ✅ ArchUnit tests
    ├── LayeredArchitectureTest.java
    ├── CoreDomainArchitectureTest.java
    ├── AdapterStructureTest.java
    ├── ApiAdapterArchitectureTest.java
    ├── DatabaseAdapterArchitectureTest.java
    └── InfrastructureArchitectureTest.java
```

## 🎯 Purpose

- **Minimal Start**: Get ArchUnit running with absolute minimum code
- **New Project Template**: Perfect starting point for greenfield projects
- **CI/CD Integration**: Verify ArchUnit tests work in empty projects

## 🧪 Running the Tests

```bash
cd samples/minimal-setup
mvn test
```

## ✅ Expected Results

Most tests should **PASS** with some being naturally empty:

```
[INFO] Tests run: 37, Failures: 0, Errors: 0, Skipped: 0
```

## 🔍 Key Observations

### ✅ What Passes
- **Spring Boot Application**: Correctly placed in root package
- **Empty Should Rules**: All rules with `.allowEmptyShould(true)` pass gracefully
- **No False Positives**: Tests don't fail on missing architecture

### 📝 What Gets Skipped
- **Adapter Structure**: No adapters exist, so adapter-specific rules pass without checking
- **Core Domain**: No core package exists, so core-specific rules pass without checking

## 💡 Growth Path

This minimal setup can grow into full hexagonal architecture:

1. **Add Core Domain**: Create `core.{domain}.{model,port,usecase,exceptions}`
2. **Add API Adapter**: Create `adapters.api.{adapter,entity}`
3. **Add Database Adapter**: Create `adapters.database.{adapter,entity}`
4. **Add Infrastructure**: Create `infrastructure.{config,util}`

## 🚀 Use Case

This sample is perfect for:
- **New Spring Boot projects** planning to use hexagonal architecture
- **Teams learning ArchUnit** who want to start simple
- **CI/CD validation** that tests work before adding business logic
- **Proof of concept** that ArchUnit doesn't break empty builds