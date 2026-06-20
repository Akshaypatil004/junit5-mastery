# JUnit 5 Mastery 🧪

> Hands-on JUnit 5 reference covering every major testing pattern — organized as a progressive 7-phase learning path.

A complete, self-paced JUnit 5 learning repository built by working through real Java examples phase by phase. Each phase has its own feature branch with dedicated practice classes and test classes committed separately to maintain clean history.

---

## 📋 What's Inside

| Phase | Topic | Key Concepts |
|-------|-------|-------------|
| [Phase 1](#phase-1--foundations) | Foundations | Unit testing, Testing pyramid, JUnit 5 architecture, @Test, assertEquals |
| [Phase 2](#phase-2--assertions--test-lifecycle) | Assertions & Lifecycle | All assertion types, @BeforeEach, @AfterEach, @BeforeAll, @AfterAll |
| [Phase 3](#phase-3--parameterized--advanced-tests) | Parameterized & Advanced | @ParameterizedTest, @CsvSource, @MethodSource, @Nested, @Tag |
| [Phase 4](#phase-4--mocking-with-mockito) | Mocking with Mockito | @Mock, @InjectMocks, when/thenReturn, verify(), @Spy |
| [Phase 5](#phase-5--tdd--code-coverage) | TDD & Code Coverage | Red→Green→Refactor, FIRST principles, JaCoCo coverage |
| [Phase 6](#phase-6--testing-best-practices) | Best Practices | AAA pattern, naming conventions, AssertJ, Clock injection |
| [Phase 7](#phase-7--mini-project) | Mini Project | Library Management System — full test suite, 48+ tests |

---

## 🚀 How to Run Tests

**Prerequisites:** Java 17+ · Maven 3.8+

```bash
# Run all tests
mvn test

# Run a specific test class
mvn test -Dtest=BookTest

# Run only fast/unit tagged tests
mvn test -Dgroups=fast

# Generate JaCoCo coverage report
mvn test jacoco:report
# Report at: target/site/jacoco/index.html
```

---

## 📦 Key Dependencies

```xml
<!-- JUnit 5 -->
<dependency>
  <groupId>org.junit.jupiter</groupId>
  <artifactId>junit-jupiter</artifactId>
  <version>5.10.2</version>
  <scope>test</scope>
</dependency>

<!-- Mockito -->
<dependency>
  <groupId>org.mockito</groupId>
  <artifactId>mockito-junit-jupiter</artifactId>
  <version>5.11.0</version>
  <scope>test</scope>
</dependency>

<!-- AssertJ -->
<dependency>
  <groupId>org.assertj</groupId>
  <artifactId>assertj-core</artifactId>
  <version>3.25.3</version>
  <scope>test</scope>
</dependency>
```

---

## Phase 1 — Foundations

**Branch:** `feature/Phase01-foundations`

What unit testing is, the testing pyramid, JUnit 5 three-component architecture (Platform + Jupiter + Vintage), and the first test.

**Practice:** `Calculator.java` → `CalculatorTest.java`

Key insight: without `@Test`, JUnit silently ignores the method — no error, no output, it simply never runs.

```java
@Test
@DisplayName("Adding 2 + 5 should return 7")
void add_twoPositiveNumbers_shouldReturnSum() {
    assertEquals(7, calculator.add(2, 5));
}
```

---

## Phase 2 — Assertions & Test Lifecycle

**Branch:** `feature/Phase02-assertions-lifecycle`

**Practice:** `BankAccount.java` → `BankAccountTest.java`

| Annotation | When it runs | Key rule |
|-----------|-------------|---------|
| `@BeforeAll` | Once before all tests | Must be static |
| `@BeforeEach` | Before every single test | Promotes isolation |
| `@AfterEach` | After every single test | Cleanup per test |
| `@AfterAll` | Once after all tests | Must be static |

`assertAll` vs multiple `assertEquals`: multiple assertions stop at the first failure. `assertAll` runs every assertion and reports ALL failures at once.

---

## Phase 3 — Parameterized & Advanced Tests

**Branch:** `feature/Phase03-parameterized-advanced`

**Practice:** `EmailValidator.java` → `EmailValidatorTest.java`

Key insight: a loop inside `@Test` reports 1 test. `@ParameterizedTest` gives each input its own independently trackable, rerunnable test entry.

```java
@ParameterizedTest(name = "[{index}] {0} should be {1}")
@CsvSource({
    "a@b.com,        true",
    "invalid-email,  false",
    "user@test.org,  true"
})
void email_validation(String email, boolean expected) {
    assertEquals(expected, EmailValidator.isValid(email));
}
```

`@Nested` groups organize tests into an IDE tree: ValidEmails / InvalidEmails / EdgeCases.

`@Tag("fast")` + `mvn test -Dgroups=fast` enables CI-level test filtering per commit/PR/nightly.

---

## Phase 4 — Mocking with Mockito

**Branch:** `feature/Phase04-mockito`

**Practice:** `UserService.java` → `UserServiceTest.java`

```java
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock UserRepository userRepository;  // fake dependency
    @Mock EmailService emailService;       // fake dependency
    @InjectMocks UserService userService;  // real class under test
}
```

Critical rule — never mix matchers and literals:
```java
// WRONG
verify(emailService).send("hello", anyString());

// RIGHT — wrap literal with eq()
verify(emailService).send(eq("hello"), anyString());
```

`@Mock` vs `@Spy`: Mock = full fake, no real code runs. Spy = wraps real object, real code runs by default. Always use `doReturn()` not `when()` with a Spy.

---

## Phase 5 — TDD & Code Coverage

**Branch:** `feature/Phase05-tdd-coverage`

**Practice:** `StringCalculator.java` built entirely test-first.

| Phase | Color | Rule |
|-------|-------|------|
| Write failing test first | 🔴 RED | Compile error counts as red |
| Write minimum code to pass | 🟢 GREEN | Even hardcoded return values are valid |
| Clean up the code | 🔵 REFACTOR | Never add new behaviour here |

FIRST principles — every good test is:
**F**ast · **I**solated · **R**epeatable · **S**elf-validating · **T**imely

JaCoCo generates an HTML coverage report. 100% is not the goal — meaningful branch coverage is.

---

## Phase 6 — Testing Best Practices

**Branch:** `feature/Phase06-best-practices`

**Naming convention:** `methodName_stateUnderTest_expectedBehavior`

```java
// Bad
void test1() { }

// Good
void withdraw_withInsufficientBalance_shouldThrowException() { }
```

**AAA pattern — every test follows this structure:**
```java
@Test
void withdraw_withSufficientBalance_shouldReduceBalance() {
    // Arrange
    BankAccount account = new BankAccount("ACC001");
    account.deposit(1000.0);
    // Act
    account.withdraw(300.0);
    // Assert
    assertEquals(700.0, account.getBalance());
}
```

**Clock injection** for time-dependent code — inject `Clock` as a parameter instead of calling `LocalTime.now()` directly. Test passes a `Clock.fixed(...)` for deterministic results.

Private methods: never test them directly. Test through the public API. The urge to break encapsulation is a design smell, not a testing problem.

---

## Phase 7 — Mini Project

**Branch:** `feature/Phase07-library-management`

Library Management System — applies every concept from Phases 1–6 in one cohesive project.

**Project structure:**
```
src/
├── main/java/com/akshay/phase07_mini_project/
│   ├── Book.java                    # Entity with validation + immutability
│   ├── Library.java                 # Collection + Optional returns
│   ├── MemberService.java           # Business logic + constructor injection
│   ├── NotificationService.java     # Interface — mocked in tests
│   └── EmailNotificationService.java
└── test/java/com/akshay/phase07_mini_project/
    ├── BookTest.java
    ├── LibraryTest.java
    ├── MemberServiceTest.java
    └── NotificationServiceTest.java
```

**Test coverage:**

| Class | Tests | Patterns Used |
|-------|-------|---------------|
| `BookTest` | 12 | @ParameterizedTest, @NullAndEmptySource, assertThrows, assertAll |
| `LibraryTest` | 18 | @Nested, two-level @BeforeEach, state verification |
| `MemberServiceTest` | 14 | @Mock, @InjectMocks, verify(), thenThrow() |
| `NotificationServiceTest` | 4 | Interface contract tests |
| **Total** | **48+** | All major JUnit 5 patterns |

**Why `NotificationService` is an interface:** Follows Dependency Inversion (SOLID). `MemberService` depends on the contract, not the implementation. Mockito mocks interfaces cleanly. Swap `EmailNotificationService` for `SmsNotificationService` tomorrow — `MemberService` needs zero changes.

---

## 🗂 Branch Structure

```
main
├── feature/Phase01-foundations
├── feature/Phase02-assertions-lifecycle
├── feature/Phase03-parameterized-advanced
├── feature/Phase04-mockito
├── feature/Phase05-tdd-coverage
├── feature/Phase06-best-practices
└── feature/Phase07-library-management
```

Commit convention followed throughout:
- `feat: add ClassName with [description]` — main class
- `test: add ClassNameTest covering [description]` — test class

---

## 🎯 Top Interview Questions Covered

1. What is a unit test? How is it different from integration and E2E testing?
2. Explain JUnit 5 architecture — Platform, Jupiter, Vintage
3. What does `@Test` do? What happens if you forget it?
4. Why does `assertEquals` take expected before actual?
5. When would you use `assertAll` vs multiple `assertEquals` calls?
6. What is the difference between `@BeforeEach` and `@BeforeAll`?
7. Why use `@ParameterizedTest` over a loop inside one test method?
8. What is the difference between `@Mock` and `@Spy` in Mockito?
9. What does `@InjectMocks` do? What are the three injection strategies?
10. Why can't you mix matchers and literals without `eq()`?
11. Explain Red → Green → Refactor with a real example
12. What are the FIRST principles of unit testing?
13. Should you ever test private methods directly?
14. What is the AAA pattern and why does it matter?
15. How do you test time-dependent code without making tests flaky?

---

## 👤 Author

**Akshay Patil** — Java Developer
JUnit 5 Learning Journey — 7 phases · 8 branches · 55 commits
