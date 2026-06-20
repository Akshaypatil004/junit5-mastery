// MemberService.java
package com.akshay.phase07_mini_project;

import java.util.*;

public class MemberService {

 public static final int MAX_BORROW_LIMIT = 3; // tests reference this constant

 private final Library library;
 private final NotificationService notificationService;

 // memberId → Set of ISBNs currently borrowed by that member
 private final Map<String, Set<String>> borrowedBooks;

 // registered member IDs
 private final Set<String> registeredMembers;

 public MemberService(Library library, NotificationService notificationService) {
     if (library == null) {
         throw new IllegalArgumentException("Library cannot be null");
     }
     if (notificationService == null) {
         throw new IllegalArgumentException("NotificationService cannot be null");
     }
     this.library = library;
     this.notificationService = notificationService;
     this.borrowedBooks = new HashMap<>();
     this.registeredMembers = new HashSet<>();
 }

 /**
  * Registers a new member.
  */
 public void registerMember(String memberId) {
     if (memberId == null || memberId.isBlank()) {
         throw new IllegalArgumentException("Member ID cannot be null or blank");
     }
     if (registeredMembers.contains(memberId)) {
         throw new IllegalStateException("Member " + memberId + " is already registered");
     }
     registeredMembers.add(memberId.trim());
     borrowedBooks.put(memberId.trim(), new HashSet<>());
 }

 /**
  * Allows a registered member to borrow a book from the library.
  */
 public void borrowBook(String memberId, String isbn) {
     validateMember(memberId);

     Set<String> memberBooks = borrowedBooks.get(memberId);

     // Check borrowing limit BEFORE attempting to borrow
     if (memberBooks.size() >= MAX_BORROW_LIMIT) {
         notificationService.notifyBorrowingLimitReached(memberId);
         throw new IllegalStateException(
             "Member " + memberId + " has reached the borrowing limit of " + MAX_BORROW_LIMIT
         );
     }

     // Delegate actual borrowing to Library
     Book book = library.borrowBook(isbn);

     // Track it
     memberBooks.add(isbn);

     // Notify — we don't care HOW this works, just that it's called
     notificationService.notifyBookBorrowed(memberId, book.getTitle());
 }

 /**
  * Allows a registered member to return a borrowed book.
  */
 public void returnBook(String memberId, String isbn) {
     validateMember(memberId);

     Set<String> memberBooks = borrowedBooks.get(memberId);

     if (!memberBooks.contains(isbn)) {
         throw new IllegalStateException(
             "Member " + memberId + " did not borrow book with ISBN " + isbn
         );
     }

     // Delegate actual return to Library
     library.returnBook(isbn);

     // Stop tracking it
     memberBooks.remove(isbn);

     // Find book title for notification
     String bookTitle = library.findByIsbn(isbn)
                               .map(Book::getTitle)
                               .orElse("Unknown");

     notificationService.notifyBookReturned(memberId, bookTitle);
 }

 /**
  * Returns how many books a member currently has borrowed.
  */
 public int getBorrowedBookCount(String memberId) {
     validateMember(memberId);
     return borrowedBooks.get(memberId).size();
 }

 /**
  * Returns the set of ISBNs currently borrowed by a member.
  */
 public Set<String> getBorrowedBooks(String memberId) {
     validateMember(memberId);
     return Collections.unmodifiableSet(borrowedBooks.get(memberId));
 }

 /**
  * Checks if a member is registered.
  */
 public boolean isMemberRegistered(String memberId) {
     return registeredMembers.contains(memberId);
 }

 // ── Private helpers ──────────────────────────────────────────────────

 private void validateMember(String memberId) {
     if (memberId == null || memberId.isBlank()) {
         throw new IllegalArgumentException("Member ID cannot be null or blank");
     }
     if (!registeredMembers.contains(memberId)) {
         throw new IllegalStateException("Member " + memberId + " is not registered");
     }
 }
}
