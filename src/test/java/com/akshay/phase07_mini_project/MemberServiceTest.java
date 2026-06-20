package com.akshay.phase07_mini_project;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
* Test suite for MemberService.
*
* WHY @ExtendWith(MockitoExtension.class)?
* This plugs Mockito into JUnit 5's lifecycle.
* It automatically:
*   - Creates mocks for fields annotated with @Mock
*   - Validates mock usage after each test (detects unused stubs)
*   - Cleans up mocks after each test
* Without this, @Mock annotations do nothing.
*
* WHY mock NotificationService but use REAL Library?
* - NotificationService has external side effects (email/SMS)
*   We verify it's CALLED correctly, not what it actually does
* - Library is a pure in-memory class we already trust
*   Using real Library makes tests more meaningful
*/
@ExtendWith(MockitoExtension.class)
@DisplayName("MemberService Tests")
class MemberServiceTest {

 // ── Mocked dependency ────────────────────────────────────────────────
 // @Mock tells Mockito: "create a fake NotificationService"
 // It records all calls made to it — we verify them later
 @Mock
 private NotificationService notificationService;

 // ── Real dependencies ────────────────────────────────────────────────
 private Library library;
 private MemberService memberService;

 // Test data
 private static final String MEMBER_ID   = "M001";
 private static final String MEMBER_ID_2 = "M002";
 private static final String ISBN_1 = "978-0-13-468599-1";
 private static final String ISBN_2 = "978-0-13-235088-4";
 private static final String ISBN_3 = "978-0-20-163361-5";
 private static final String ISBN_4 = "978-0-59-651798-1";

 @BeforeEach
 void setUp() {
     // Real Library — no mock needed
     library = new Library("Test Library");

     // Add books to library
     library.addBook(new Book(ISBN_1, "Effective Java", "Joshua Bloch"));
     library.addBook(new Book(ISBN_2, "Clean Code", "Robert Martin"));
     library.addBook(new Book(ISBN_3, "Design Patterns", "Gang of Four"));
     library.addBook(new Book(ISBN_4, "Refactoring", "Martin Fowler"));

     // Inject REAL library + MOCK notification into MemberService
     // This is constructor injection in action
     memberService = new MemberService(library, notificationService);

     // Register a default member for most tests
     memberService.registerMember(MEMBER_ID);
 }

 // ════════════════════════════════════════════════════════════════════
 // GROUP 1: Constructor Validation
 // ════════════════════════════════════════════════════════════════════

 @Nested
 @DisplayName("Constructor Validation")
 class ConstructorTests {

     @Test
     @DisplayName("Should throw when Library is null")
     void shouldThrowWhenLibraryIsNull() {
         assertThrows(IllegalArgumentException.class,
             () -> new MemberService(null, notificationService));
     }

     @Test
     @DisplayName("Should throw when NotificationService is null")
     void shouldThrowWhenNotificationServiceIsNull() {
         assertThrows(IllegalArgumentException.class,
             () -> new MemberService(library, null));
     }
 }

 // ════════════════════════════════════════════════════════════════════
 // GROUP 2: registerMember()
 // ════════════════════════════════════════════════════════════════════

 @Nested
 @DisplayName("registerMember()")
 class RegisterMemberTests {

     @Test
     @DisplayName("Should register a new member successfully")
     void shouldRegisterNewMember() {
         memberService.registerMember(MEMBER_ID_2);
         assertTrue(memberService.isMemberRegistered(MEMBER_ID_2));
     }

     @Test
     @DisplayName("Should throw when registering duplicate member")
     void shouldThrowForDuplicateMember() {
         // MEMBER_ID already registered in @BeforeEach
         IllegalStateException ex = assertThrows(
             IllegalStateException.class,
             () -> memberService.registerMember(MEMBER_ID)
         );
         assertTrue(ex.getMessage().contains(MEMBER_ID));
     }

     @ParameterizedTest
     @NullAndEmptySource
     @ValueSource(strings = {"   "})
     @DisplayName("Should throw for null or blank member ID")
     void shouldThrowForBlankMemberId(String invalidId) {
         assertThrows(IllegalArgumentException.class,
             () -> memberService.registerMember(invalidId));
     }

     @Test
     @DisplayName("Unregistered member should not be found")
     void unregisteredMemberShouldNotBeFound() {
         assertFalse(memberService.isMemberRegistered("GHOST_MEMBER"));
     }
 }

 // ════════════════════════════════════════════════════════════════════
 // GROUP 3: borrowBook() — the most important group
 // This is where Mockito verify() shines
 // ════════════════════════════════════════════════════════════════════

 @Nested
 @DisplayName("borrowBook()")
 class BorrowBookTests {

     @Test
     @DisplayName("Should borrow a book and notify member")
     void shouldBorrowBookAndNotifyMember() {
         memberService.borrowBook(MEMBER_ID, ISBN_1);

         // Verify the book is tracked under this member
         assertEquals(1, memberService.getBorrowedBookCount(MEMBER_ID));
         assertTrue(memberService.getBorrowedBooks(MEMBER_ID).contains(ISBN_1));

         // WHY verify()?
         // We mocked NotificationService — it doesn't DO anything.
         // But we MUST ensure MemberService CALLED it correctly.
         // This is the whole point of mocking — verify interactions.
         verify(notificationService, times(1))
             .notifyBookBorrowed(MEMBER_ID, "Effective Java");
     }

     @Test
     @DisplayName("Should track multiple borrowed books correctly")
     void shouldTrackMultipleBorrowedBooks() {
         memberService.borrowBook(MEMBER_ID, ISBN_1);
         memberService.borrowBook(MEMBER_ID, ISBN_2);

         assertEquals(2, memberService.getBorrowedBookCount(MEMBER_ID));

         // Verify notification was called for each borrow
         verify(notificationService).notifyBookBorrowed(MEMBER_ID, "Effective Java");
         verify(notificationService).notifyBookBorrowed(MEMBER_ID, "Clean Code");
     }

     @Test
     @DisplayName("Should throw and notify when borrowing limit is reached")
     void shouldThrowWhenBorrowingLimitReached() {
         // Borrow up to the limit
         memberService.borrowBook(MEMBER_ID, ISBN_1);
         memberService.borrowBook(MEMBER_ID, ISBN_2);
         memberService.borrowBook(MEMBER_ID, ISBN_3);

         // 4th borrow should fail
         IllegalStateException ex = assertThrows(
             IllegalStateException.class,
             () -> memberService.borrowBook(MEMBER_ID, ISBN_4)
         );
         assertTrue(ex.getMessage().contains("borrowing limit"));

         // WHY verify this?
         // The business rule says: notify the member when limit is hit.
         // If we don't verify, we can't be sure the notification was sent.
         verify(notificationService, times(1))
             .notifyBorrowingLimitReached(MEMBER_ID);
     }

     @Test
     @DisplayName("Should not send borrow notification when limit is reached")
     void shouldNotSendBorrowNotificationWhenLimitReached() {
         memberService.borrowBook(MEMBER_ID, ISBN_1);
         memberService.borrowBook(MEMBER_ID, ISBN_2);
         memberService.borrowBook(MEMBER_ID, ISBN_3);

         assertThrows(IllegalStateException.class,
             () -> memberService.borrowBook(MEMBER_ID, ISBN_4));

         // notifyBookBorrowed should only have been called 3 times
         // (for the 3 successful borrows), NOT 4
         verify(notificationService, times(3))
             .notifyBookBorrowed(eq(MEMBER_ID), anyString());

         // notifyBorrowingLimitReached called exactly once
         verify(notificationService, times(1))
             .notifyBorrowingLimitReached(MEMBER_ID);
     }

     @Test
     @DisplayName("Should throw when unregistered member tries to borrow")
     void shouldThrowForUnregisteredMember() {
         assertThrows(IllegalStateException.class,
             () -> memberService.borrowBook("GHOST", ISBN_1));

         // Verify NO notification was sent for invalid member
         verifyNoInteractions(notificationService);
     }

     @Test
     @DisplayName("Should throw when borrowing non-existent book")
     void shouldThrowWhenBookNotInLibrary() {
         assertThrows(IllegalArgumentException.class,
             () -> memberService.borrowBook(MEMBER_ID, "999-NONEXISTENT"));

         // No notification should be sent if book doesn't exist
         verifyNoInteractions(notificationService);
     }
 }

 // ════════════════════════════════════════════════════════════════════
 // GROUP 4: returnBook()
 // ════════════════════════════════════════════════════════════════════

 @Nested
 @DisplayName("returnBook()")
 class ReturnBookTests {

     @BeforeEach
     void borrowFirst() {
         // Pre-borrow so return tests have something to return
         memberService.borrowBook(MEMBER_ID, ISBN_1);

         // Reset mock interaction history after setup
         // WHY? We don't want the borrow notification from setUp
         // to interfere with return test verifications
         clearInvocations(notificationService);
     }

     @Test
     @DisplayName("Should return borrowed book and notify member")
     void shouldReturnBorrowedBookAndNotifyMember() {
         memberService.returnBook(MEMBER_ID, ISBN_1);

         assertEquals(0, memberService.getBorrowedBookCount(MEMBER_ID));
         assertFalse(memberService.getBorrowedBooks(MEMBER_ID).contains(ISBN_1));

         verify(notificationService, times(1))
             .notifyBookReturned(MEMBER_ID, "Effective Java");
     }

     @Test
     @DisplayName("Should throw when returning book not borrowed by member")
     void shouldThrowWhenBookNotBorrowedByMember() {
         // MEMBER_ID borrowed ISBN_1, not ISBN_2
         IllegalStateException ex = assertThrows(
             IllegalStateException.class,
             () -> memberService.returnBook(MEMBER_ID, ISBN_2)
         );
         assertTrue(ex.getMessage().contains(MEMBER_ID));

         // No return notification should fire for invalid return
         verify(notificationService, never())
             .notifyBookReturned(anyString(), anyString());
     }

     @Test
     @DisplayName("Should allow borrowing again after returning")
     void shouldAllowBorrowingAfterReturning() {
         memberService.returnBook(MEMBER_ID, ISBN_1);

         // Should be able to borrow again
         assertDoesNotThrow(() -> memberService.borrowBook(MEMBER_ID, ISBN_1));
     }
 }

 // ════════════════════════════════════════════════════════════════════
 // GROUP 5: getBorrowedBookCount()
 // ════════════════════════════════════════════════════════════════════

 @Nested
 @DisplayName("getBorrowedBookCount()")
 class BorrowedCountTests {

     @Test
     @DisplayName("Should return 0 for member with no borrowed books")
     void shouldReturnZeroForNewMember() {
         assertEquals(0, memberService.getBorrowedBookCount(MEMBER_ID));
     }

     @Test
     @DisplayName("Should return correct count after borrowing")
     void shouldReturnCorrectCountAfterBorrowing() {
         memberService.borrowBook(MEMBER_ID, ISBN_1);
         memberService.borrowBook(MEMBER_ID, ISBN_2);
         assertEquals(2, memberService.getBorrowedBookCount(MEMBER_ID));
     }

     @Test
     @DisplayName("Should throw for unregistered member")
     void shouldThrowForUnregisteredMember() {
         assertThrows(IllegalStateException.class,
             () -> memberService.getBorrowedBookCount("GHOST"));
     }
 }
}
