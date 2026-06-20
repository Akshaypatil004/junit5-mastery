package com.akshay.phase07_mini_project.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("Library Service Tests")
class LibraryTest {

 private Library library;
 private Book effectiveJava;
 private Book cleanCode;
 private Book designPatterns;

 @BeforeEach
 void setUp() {
     // Fresh library for every test — no shared state
     library = new Library("City Central Library");

     // Reusable books — recreated fresh for every test
     effectiveJava  = new Book("978-0-13-468599-1", "Effective Java", "Joshua Bloch");
     cleanCode      = new Book("978-0-13-235088-4", "Clean Code", "Robert Martin");
     designPatterns = new Book("978-0-20-163361-5", "Design Patterns", "Gang of Four");
 }

 // GROUP 1: Constructor Validation

 @Nested
 @DisplayName("Constructor Validation")
 class ConstructorTests {

     @Test
     @DisplayName("Should create library with valid name")
     void shouldCreateLibraryWithValidName() {
         Library lib = new Library("My Library");
         assertEquals("My Library", lib.getName());
     }

     @ParameterizedTest(name = "Library name=''{0}'' should be rejected")
     @NullAndEmptySource
     @ValueSource(strings = {"   "})
     @DisplayName("Should throw exception for blank library name")
     void shouldThrowForBlankName(String invalidName) {
         assertThrows(IllegalArgumentException.class,
             () -> new Library(invalidName));
     }
 }

 // GROUP 2: addBook()
 

 @Nested
 @DisplayName("addBook()")
 class AddBookTests {

     @Test
     @DisplayName("Should add a valid book successfully")
     void shouldAddValidBook() {
         library.addBook(effectiveJava);
         assertEquals(1, library.getTotalBookCount());
     }

     @Test
     @DisplayName("Should add multiple different books")
     void shouldAddMultipleBooks() {
         library.addBook(effectiveJava);
         library.addBook(cleanCode);
         library.addBook(designPatterns);
         assertEquals(3, library.getTotalBookCount());
     }

     @Test
     @DisplayName("Should throw exception when adding null book")
     void shouldThrowWhenAddingNull() {
         assertThrows(IllegalArgumentException.class,
             () -> library.addBook(null));
     }

     @Test
     @DisplayName("Should throw exception when adding duplicate ISBN")
     void shouldThrowWhenAddingDuplicateIsbn() {
         // WHY test this? Because silently ignoring duplicates would
         // corrupt inventory counts. Failing loudly is safer.
         library.addBook(effectiveJava);

         Book duplicate = new Book(
             "978-0-13-468599-1", // same ISBN!
             "Effective Java 4th Ed",
             "Joshua Bloch"
         );

         IllegalStateException ex = assertThrows(
             IllegalStateException.class,
             () -> library.addBook(duplicate)
         );
         assertTrue(ex.getMessage().contains("978-0-13-468599-1"));
     }

     @Test
     @DisplayName("Total count should be zero for empty library")
     void shouldReturnZeroCountForEmptyLibrary() {
         assertEquals(0, library.getTotalBookCount());
     }
 }

 // GROUP 3: borrowBook()


 @Nested
 @DisplayName("borrowBook()")
 class BorrowBookTests {

     @BeforeEach
     void addBooksToLibrary() {
         // WHY a nested @BeforeEach?
         // Only borrow/return tests need books pre-added.
         // The outer @BeforeEach creates the library and book objects.
         // This inner one adds them — separation of concerns.
         // The outer @BeforeEach runs FIRST, then this one.
         library.addBook(effectiveJava);
         library.addBook(cleanCode);
     }

     @Test
     @DisplayName("Should borrow an available book successfully")
     void shouldBorrowAvailableBook() {
         Book borrowed = library.borrowBook("978-0-13-468599-1");

         // Verify the returned object IS the book we wanted
         assertEquals("Effective Java", borrowed.getTitle());
         // Verify it's now marked unavailable
         assertFalse(borrowed.isAvailable(),
             "Book should be unavailable after borrowing");
     }

     @Test
     @DisplayName("Available count should decrease after borrowing")
     void availableCountShouldDecreaseAfterBorrow() {
         long before = library.getAvailableBookCount(); // 2
         library.borrowBook("978-0-13-468599-1");
         long after = library.getAvailableBookCount();  // should be 1

         assertEquals(before - 1, after,
             "Available count should decrease by 1 after borrowing");
     }

     @Test
     @DisplayName("Should throw when borrowing a book not in library")
     void shouldThrowWhenBookNotFound() {
         IllegalArgumentException ex = assertThrows(
             IllegalArgumentException.class,
             () -> library.borrowBook("999-9999999999")
         );
         assertTrue(ex.getMessage().contains("not found"));
     }

     @Test
     @DisplayName("Should throw when borrowing an already borrowed book")
     void shouldThrowWhenBookAlreadyBorrowed() {
         library.borrowBook("978-0-13-468599-1"); // First borrow — OK

         // Second borrow — should fail
         IllegalStateException ex = assertThrows(
             IllegalStateException.class,
             () -> library.borrowBook("978-0-13-468599-1")
         );
         assertTrue(ex.getMessage().contains("not available"));
     }

     @ParameterizedTest(name = "ISBN=''{0}'' should be rejected")
     @NullAndEmptySource
     @ValueSource(strings = {"   "})
     @DisplayName("Should throw for null or blank ISBN")
     void shouldThrowForBlankIsbn(String invalidIsbn) {
         assertThrows(IllegalArgumentException.class,
             () -> library.borrowBook(invalidIsbn));
     }
 }
 
 // GROUP 4: returnBook()

 @Nested
 @DisplayName("returnBook()")
 class ReturnBookTests {

     @BeforeEach
     void setUp() {
         library.addBook(effectiveJava);
         library.borrowBook("978-0-13-468599-1"); // pre-borrow for return tests
     }

     @Test
     @DisplayName("Should return a borrowed book successfully")
     void shouldReturnBorrowedBook() {
         library.returnBook("978-0-13-468599-1");

         // Verify it's available again
         assertTrue(
             library.findByIsbn("978-0-13-468599-1").get().isAvailable(),
             "Book should be available after returning"
         );
     }

     @Test
     @DisplayName("Available count should increase after returning")
     void availableCountShouldIncreaseAfterReturn() {
         long before = library.getAvailableBookCount(); // 0 (it's borrowed)
         library.returnBook("978-0-13-468599-1");
         long after = library.getAvailableBookCount();  // should be 1

         assertEquals(before + 1, after);
     }

     @Test
     @DisplayName("Should throw when returning a book not in library")
     void shouldThrowWhenBookNotInLibrary() {
         assertThrows(IllegalArgumentException.class,
             () -> library.returnBook("999-9999999999"));
     }

     @Test
     @DisplayName("Should throw when returning a book that was not borrowed")
     void shouldThrowWhenBookNotBorrowed() {
         library.returnBook("978-0-13-468599-1"); // return it first

         // Trying to return again — should fail
         IllegalStateException ex = assertThrows(
             IllegalStateException.class,
             () -> library.returnBook("978-0-13-468599-1")
         );
         assertTrue(ex.getMessage().contains("not borrowed"));
     }
 }

 // GROUP 5: searchByTitle() and searchByAuthor()

 @Nested
 @DisplayName("Search Operations")
 class SearchTests {

     @BeforeEach
     void populateLibrary() {
         library.addBook(effectiveJava);
         library.addBook(cleanCode);
         library.addBook(designPatterns);
     }

     @ParameterizedTest(name = "Search title=''{0}'' should find Effective Java")
     @ValueSource(strings = {
         "Effective Java",   // exact match
         "effective java",   // all lowercase
         "EFFECTIVE JAVA",   // all uppercase
         "effective",        // partial match
         "java"              // single word
     })
     @DisplayName("searchByTitle should be case-insensitive and support partial match")
     void searchByTitleShouldBeCaseInsensitive(String searchTerm) {
         List<Book> results = library.searchByTitle(searchTerm);
         assertFalse(results.isEmpty(), "Should find at least one result for: " + searchTerm);
         assertTrue(
             results.stream().anyMatch(b -> b.getTitle().equals("Effective Java")),
             "Should find 'Effective Java' for search term: " + searchTerm
         );
     }

     @ParameterizedTest(name = "Search author=''{0}'' should find Clean Code")
     @ValueSource(strings = {
         "Robert Martin",
         "robert martin",
         "ROBERT",
         "martin"
     })
     @DisplayName("searchByAuthor should be case-insensitive and support partial match")
     void searchByAuthorShouldBeCaseInsensitive(String searchTerm) {
         List<Book> results = library.searchByAuthor(searchTerm);
         assertFalse(results.isEmpty());
         assertTrue(
             results.stream().anyMatch(b -> b.getTitle().equals("Clean Code"))
         );
     }

     @Test
     @DisplayName("searchByTitle should return empty list when no match found")
     void searchByTitleShouldReturnEmptyListWhenNoMatch() {
         List<Book> results = library.searchByTitle("Nonexistent Book XYZ");
         assertTrue(results.isEmpty());
     }

     @Test
     @DisplayName("searchByAuthor should return empty list when no match found")
     void searchByAuthorShouldReturnEmptyListWhenNoMatch() {
         List<Book> results = library.searchByAuthor("Unknown Author XYZ");
         assertTrue(results.isEmpty());
     }

     @ParameterizedTest
     @NullAndEmptySource
     @ValueSource(strings = {"   "})
     @DisplayName("searchByTitle should throw for null or blank input")
     void searchByTitleShouldThrowForBlankInput(String input) {
         assertThrows(IllegalArgumentException.class,
             () -> library.searchByTitle(input));
     }

     @ParameterizedTest
     @NullAndEmptySource
     @ValueSource(strings = {"   "})
     @DisplayName("searchByAuthor should throw for null or blank input")
     void searchByAuthorShouldThrowForBlankInput(String input) {
         assertThrows(IllegalArgumentException.class,
             () -> library.searchByAuthor(input));
     }
 }
 // GROUP 6: getAvailableBookCount()

 @Nested
 @DisplayName("Available Book Count")
 class AvailableCountTests {

     @Test
     @DisplayName("Should return 0 for empty library")
     void shouldReturnZeroForEmptyLibrary() {
         assertEquals(0, library.getAvailableBookCount());
     }

     @Test
     @DisplayName("Should correctly count available books across borrow/return cycle")
     void shouldCorrectlyCountAcrossBorrowReturnCycle() {
         library.addBook(effectiveJava);
         library.addBook(cleanCode);
         library.addBook(designPatterns);

         assertEquals(3, library.getAvailableBookCount(), "All 3 should be available");

         library.borrowBook("978-0-13-468599-1");
         assertEquals(2, library.getAvailableBookCount(), "2 should be available after 1 borrow");

         library.borrowBook("978-0-13-235088-4");
         assertEquals(1, library.getAvailableBookCount(), "1 should be available after 2 borrows");

         library.returnBook("978-0-13-468599-1");
         assertEquals(2, library.getAvailableBookCount(), "2 should be available after 1 return");
     }
 }
}
