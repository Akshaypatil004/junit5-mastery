# 📚 Library Management System — JUnit 5 Test Suite

A fully tested Library Management System built as part of a JUnit 5
mastery journey. This project demonstrates real-world testing practices
including lifecycle management, parameterized tests, Mockito mocking,
and JaCoCo coverage reporting.

---

## 🏗️ Project Structure

```
src/
├── main/java/com/akshay/phase07_mini_project/
│   ├── Book.java                      # Book entity with validation
│   ├── Library.java                   # Collection management
│   ├── MemberService.java             # Borrowing business logic
│   ├── NotificationService.java       # Notification interface
│   └── EmailNotificationService.java  # Concrete fake implementation
│
└── test/java/com/akshay/phase07_mini_project/
    ├── BookTest.java                  # 16 tests
    ├── LibraryTest.java               # 30 tests
    ├── MemberServiceTest.java         # 18 tests
    └── NotificationServiceTest.java   # 16 tests
```
## ✅ Test Coverage Summary

| Class                      | Tests | Line Coverage |
|----------------------------|-------|---------------|
| Book                       | 16    | 95%+          |
| Library                    | 30    | 90%+          |
| MemberService              | 18    | 92%+          |
| EmailNotificationService   | 16    | 88%+          |
| **Total**                  | **80+** | **90%+**    |

---

## 🧪 Testing Techniques Demonstrated

| Technique               | Where Used                          |
|-------------------------|-------------------------------------|
| `@BeforeEach`           | All test classes — fresh state      |
| `@Nested`               | Logical grouping of test scenarios  |
| `@ParameterizedTest`    | Validation tests across all classes |
| `@NullAndEmptySource`   | Null/blank input rejection          |
| `@ValueSource`          | Multiple input variants             |
| `@CsvSource`            | Multi-argument parameterized tests  |
| `assertAll()`           | Grouped assertions in BookTest      |
| `assertThrows()`        | Exception scenario validation       |
| `@Mock`                 | Mocking NotificationService         |
| `verify()`              | Interaction verification            |
| `verifyNoInteractions()`| Ensuring no unexpected calls        |
| `clearInvocations()`    | Resetting mock state between steps  |

---

## 🚀 How to Run Tests

### Run all tests
```bash
mvn clean test
```

### Run a specific test class
```bash
mvn test -Dtest=BookTest
mvn test -Dtest=LibraryTest
mvn test -Dtest=MemberServiceTest
mvn test -Dtest=NotificationServiceTest
```

### Run and generate coverage report
```bash
mvn clean test
```
Then open:
```
target/site/jacoco/index.html
```

---

## 📦 Dependencies

| Dependency                  | Version  | Purpose                        |
|-----------------------------|----------|--------------------------------|
| JUnit Jupiter API           | 5.10.2   | Writing tests                  |
| JUnit Jupiter Engine        | 5.10.2   | Running tests                  |
| JUnit Jupiter Params        | 5.10.2   | Parameterized tests            |
| Mockito Core                | 5.11.0   | Mocking dependencies           |
| Mockito JUnit Jupiter       | 5.11.0   | Mockito + JUnit 5 integration  |
| JaCoCo Maven Plugin         | 0.8.11   | Code coverage reporting        |

---

## 🌿 Branch

```
feature/Phase07-library-management
```

---

## 💡 Key Design Decisions

- **Constructor injection** used throughout — makes mocking easy and
  dependencies explicit
- **NotificationService as interface** — follows Dependency Inversion
  Principle, allows easy mocking
- **Real Library in MemberServiceTest** — no unnecessary mocking of
  already-tested classes
- **@BeforeEach over @BeforeAll** — ensures complete test isolation,
  no shared mutable state
- **@Nested test classes** — groups related scenarios, improves
  readability and failure messages