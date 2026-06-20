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


@DisplayName("NotificationService Tests")
class NotificationServiceTest {

 // ════════════════════════════════════════════════════════════════════
 // PART 1: Testing the real EmailNotificationService implementation
 // ════════════════════════════════════════════════════════════════════

 @Nested
 @DisplayName("EmailNotificationService - Real Implementation Tests")
 class EmailNotificationServiceTests {

     private EmailNotificationService notificationService;

     @BeforeEach
     void setUp() {
         notificationService = new EmailNotificationService();
     }

     @Test
     @DisplayName("Should send borrow notification with correct content")
     void shouldSendBorrowNotificationWithCorrectContent() {
         notificationService.notifyBookBorrowed("M001", "Effective Java");

         assertEquals(1, notificationService.getNotificationCount());
         assertTrue(notificationService.hasNotification("M001"),
             "Notification should contain member ID");
         assertTrue(notificationService.hasNotification("Effective Java"),
             "Notification should contain book title");
         assertTrue(notificationService.hasNotification("BORROWED"),
             "Notification should indicate borrow action");
     }

     @Test
     @DisplayName("Should send return notification with correct content")
     void shouldSendReturnNotificationWithCorrectContent() {
         notificationService.notifyBookReturned("M001", "Clean Code");

         assertEquals(1, notificationService.getNotificationCount());
         assertTrue(notificationService.hasNotification("M001"));
         assertTrue(notificationService.hasNotification("Clean Code"));
         assertTrue(notificationService.hasNotification("RETURNED"));
     }

     @Test
     @DisplayName("Should send borrowing limit notification with correct content")
     void shouldSendLimitNotificationWithCorrectContent() {
         notificationService.notifyBorrowingLimitReached("M001");

         assertEquals(1, notificationService.getNotificationCount());
         assertTrue(notificationService.hasNotification("M001"));
         assertTrue(notificationService.hasNotification("LIMIT REACHED"));
     }

     @Test
     @DisplayName("Should accumulate multiple notifications")
     void shouldAccumulateMultipleNotifications() {
         notificationService.notifyBookBorrowed("M001", "Effective Java");
         notificationService.notifyBookBorrowed("M001", "Clean Code");
         notificationService.notifyBookReturned("M001", "Effective Java");

         assertEquals(3, notificationService.getNotificationCount());
     }

     @Test
     @DisplayName("Should clear all notifications")
     void shouldClearAllNotifications() {
         notificationService.notifyBookBorrowed("M001", "Effective Java");
         notificationService.notifyBookBorrowed("M002", "Clean Code");

         notificationService.clearNotifications();

         assertEquals(0, notificationService.getNotificationCount());
     }

     @Test
     @DisplayName("Should return unmodifiable notification list")
     void shouldReturnUnmodifiableList() {
         notificationService.notifyBookBorrowed("M001", "Effective Java");

         assertThrows(UnsupportedOperationException.class,
             () -> notificationService.getSentNotifications().add("FAKE"));
     }

     // ── Validation tests ─────────────────────────────────────────────

     @Nested
     @DisplayName("Input Validation")
     class ValidationTests {

         @ParameterizedTest
         @NullAndEmptySource
         @ValueSource(strings = {"   "})
         @DisplayName("notifyBookBorrowed should throw for blank memberId")
         void borrowedShouldThrowForBlankMemberId(String invalidId) {
             assertThrows(IllegalArgumentException.class,
                 () -> notificationService.notifyBookBorrowed(invalidId, "Some Book"));
         }

         @ParameterizedTest
         @NullAndEmptySource
         @ValueSource(strings = {"   "})
         @DisplayName("notifyBookBorrowed should throw for blank bookTitle")
         void borrowedShouldThrowForBlankBookTitle(String invalidTitle) {
             assertThrows(IllegalArgumentException.class,
                 () -> notificationService.notifyBookBorrowed("M001", invalidTitle));
         }

         @ParameterizedTest
         @NullAndEmptySource
         @ValueSource(strings = {"   "})
         @DisplayName("notifyBookReturned should throw for blank memberId")
         void returnedShouldThrowForBlankMemberId(String invalidId) {
             assertThrows(IllegalArgumentException.class,
                 () -> notificationService.notifyBookReturned(invalidId, "Some Book"));
         }

         @ParameterizedTest
         @NullAndEmptySource
         @ValueSource(strings = {"   "})
         @DisplayName("notifyBorrowingLimitReached should throw for blank memberId")
         void limitShouldThrowForBlankMemberId(String invalidId) {
             assertThrows(IllegalArgumentException.class,
                 () -> notificationService.notifyBorrowingLimitReached(invalidId));
         }
     }
 }

 // ════════════════════════════════════════════════════════════════════
 // PART 2: Contract testing via Mockito
 // WHY test the interface with Mockito here?
 // To verify the CONTRACT is well-designed and callable correctly.
 // Any future implementation must honor these calling conventions.
 // ════════════════════════════════════════════════════════════════════

 @Nested
 @ExtendWith(MockitoExtension.class)
 @DisplayName("NotificationService - Contract Tests via Mockito")
 class NotificationServiceContractTests {

     @Mock
     private NotificationService notificationService;

     @Test
     @DisplayName("Contract: notifyBookBorrowed should be callable with valid args")
     void contractBorrowedCallableWithValidArgs() {
         // Just verify the contract can be called — no exception
         assertDoesNotThrow(() ->
             notificationService.notifyBookBorrowed("M001", "Effective Java"));

         verify(notificationService).notifyBookBorrowed("M001", "Effective Java");
     }

     @Test
     @DisplayName("Contract: notifyBookReturned should be callable with valid args")
     void contractReturnedCallableWithValidArgs() {
         assertDoesNotThrow(() ->
             notificationService.notifyBookReturned("M001", "Clean Code"));

         verify(notificationService).notifyBookReturned("M001", "Clean Code");
     }

     @Test
     @DisplayName("Contract: notifyBorrowingLimitReached should be callable")
     void contractLimitReachedCallable() {
         assertDoesNotThrow(() ->
             notificationService.notifyBorrowingLimitReached("M001"));

         verify(notificationService).notifyBorrowingLimitReached("M001");
     }

     @Test
     @DisplayName("Contract: no notification method should be called unexpectedly")
     void contractNoUnexpectedCalls() {
         // Verify that when we do nothing, nothing fires
         verifyNoInteractions(notificationService);
     }
 }
}
