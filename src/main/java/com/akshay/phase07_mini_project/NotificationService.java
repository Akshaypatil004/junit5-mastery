// NotificationService.java
package com.akshay.phase07_mini_project;

public interface NotificationService {
	/**
	 * Notifies a member that they successfully borrowed a book.
	 */
	void notifyBookBorrowed(String memberId, String bookTitle);

	/**
	 * Notifies a member that they successfully returned a book.
	 */
	void notifyBookReturned(String memberId, String bookTitle);

	/**
	 * Notifies a member that they have reached their borrowing limit.
	 */
	void notifyBorrowingLimitReached(String memberId);

}
