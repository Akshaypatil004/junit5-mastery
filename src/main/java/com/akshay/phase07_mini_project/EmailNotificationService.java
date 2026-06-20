package com.akshay.phase07_mini_project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EmailNotificationService implements NotificationService {

 // In-memory log of all notifications sent
 // WHY store them? So tests can assert what was sent
 private final List<String> sentNotifications = new ArrayList<>();

 @Override
 public void notifyBookBorrowed(String memberId, String bookTitle) {
     validateInputs(memberId, bookTitle);
     String message = String.format(
         "BORROWED: Member [%s] borrowed book [%s]", memberId, bookTitle
     );
     sentNotifications.add(message);
 }

 @Override
 public void notifyBookReturned(String memberId, String bookTitle) {
     validateInputs(memberId, bookTitle);
     String message = String.format(
         "RETURNED: Member [%s] returned book [%s]", memberId, bookTitle
     );
     sentNotifications.add(message);
 }

 @Override
 public void notifyBorrowingLimitReached(String memberId) {
     if (memberId == null || memberId.isBlank()) {
         throw new IllegalArgumentException("Member ID cannot be null or blank");
     }
     String message = String.format(
         "LIMIT REACHED: Member [%s] has reached borrowing limit", memberId
     );
     sentNotifications.add(message);
 }

 // ── Helper methods for tests ─────────────────────────────────────────

 public List<String> getSentNotifications() {
     return Collections.unmodifiableList(sentNotifications);
 }

 public int getNotificationCount() {
     return sentNotifications.size();
 }

 public void clearNotifications() {
     sentNotifications.clear();
 }

 public boolean hasNotification(String keyword) {
     return sentNotifications.stream()
             .anyMatch(n -> n.contains(keyword));
 }

 // ── Private ──────────────────────────────────────────────────────────

 private void validateInputs(String memberId, String bookTitle) {
     if (memberId == null || memberId.isBlank()) {
         throw new IllegalArgumentException("Member ID cannot be null or blank");
     }
     if (bookTitle == null || bookTitle.isBlank()) {
         throw new IllegalArgumentException("Book title cannot be null or blank");
     }
 }
}
