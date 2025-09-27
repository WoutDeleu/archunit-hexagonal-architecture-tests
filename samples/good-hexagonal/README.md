# Good Hexagonal Architecture Sample

This sample demonstrates a **perfect implementation** of hexagonal architecture that passes all ArchUnit tests.

## 📁 Structure

```
good-hexagonal/
├── src/main/java/com/example/
│   ├── BookstoreApplication.java                    # ✅ Spring Boot app in root
│   ├── core/book/                                   # ✅ Pure domain logic
│   │   ├── model/
│   │   │   ├── Book.java                           # ✅ Pure domain entity
│   │   │   └── BookId.java                         # ✅ Value object
│   │   ├── port/
│   │   │   ├── BookRepository.java                 # ✅ Output port interface
│   │   │   ├── BookCommandHandler.java             # ✅ Input port interface
│   │   │   └── BookQueryHandler.java               # ✅ Input port interface
│   │   ├── usecase/
│   │   │   └── BookService.java                    # ✅ Business logic
│   │   └── exceptions/
│   │       ├── BookNotFoundException.java          # ✅ Domain exception
│   │       └── DuplicateIsbnException.java         # ✅ Domain exception
│   ├── adapters/                                   # ✅ External interfaces
│   │   ├── api/
│   │   │   ├── adapter/
│   │   │   │   └── BookController.java             # ✅ REST controller
│   │   │   └── entity/
│   │   │       ├── BookDto.java                    # ✅ API DTO
│   │   │       └── CreateBookRequest.java          # ✅ API DTO
│   │   └── database/
│   │       ├── adapter/
│   │       │   ├── BookRepositoryAdapter.java      # ✅ Repository implementation
│   │       │   └── JpaBookRepository.java          # ✅ Spring Data repository
│   │       └── entity/
│   │           └── BookEntity.java                 # ✅ JPA entity
│   └── infrastructure/                             # ✅ Cross-cutting concerns
│       ├── config/
│       │   └── BookConfig.java                     # ✅ Configuration
│       └── util/
│           └── ValidationUtils.java                # ✅ Utilities
└── src/test/java/com/example/archunit/            # ✅ ArchUnit tests
    ├── LayeredArchitectureTest.java
    ├── CoreDomainArchitectureTest.java
    ├── AdapterStructureTest.java
    ├── ApiAdapterArchitectureTest.java
    ├── DatabaseAdapterArchitectureTest.java
    └── InfrastructureArchitectureTest.java
```

## 🎯 Purpose

- **Perfect Reference**: Shows exactly how hexagonal architecture should be implemented
- **All Tests Pass**: Demonstrates compliance with every ArchUnit rule
- **Best Practices**: Examples of proper package organization and dependency direction

## 🧪 Running the Tests

```bash
cd samples/good-hexagonal
mvn test
```

## ✅ Expected Results

All tests should **PASS** with output similar to:

```
[INFO] Tests run: 37, Failures: 0, Errors: 0, Skipped: 0
```

## 🔍 Key Architecture Highlights

### ✅ Core Domain Purity
- **No framework dependencies** in core package
- **Pure business logic** in `BookService`
- **Domain entities** with no annotations except validation
- **Port interfaces** defined in core, implemented in adapters

### ✅ Proper Adapter Implementation
- **Controllers implement input ports** defined in core (BookCommandHandler, BookQueryHandler)
- **Controllers in API adapters** with `@RestController`
- **Repository implementations in database adapters** with `@Service`
- **JPA entities in database entity package** with only JPA annotations
- **No cross-adapter dependencies**

### ✅ Clean Infrastructure
- **Configuration classes** in infrastructure/config
- **Utility classes** in infrastructure/util
- **No business logic** in infrastructure layer

### ✅ Dependency Direction & Port Usage
- **Core defines input ports** (BookCommandHandler, BookQueryHandler) and output ports (BookRepository)
- **API adapters implement input ports** (controller implements command/query handlers)
- **Database adapters implement output ports** (repository adapter implements repository interface)
- **Core depends on nothing** (except standard Java libraries)
- **Adapters depend only on core and infrastructure**
- **Infrastructure depends only on core**
- **Spring Boot main class in root package**

## 💡 Use Case

This sample is perfect for:
- **Learning hexagonal architecture** best practices
- **Reference implementation** for new projects
- **Validation** that ArchUnit tests work correctly
- **Training teams** on proper architecture patterns