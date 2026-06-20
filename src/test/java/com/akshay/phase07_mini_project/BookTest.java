// BookTest.java
package com.akshay.phase07_mini_project;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

/** 
* WHY @DisplayName everywhere?
* Tests are documentation. A failing test should tell you
* EXACTLY what broke without reading the code.
*/

@DisplayName("Book Model Test")
class BookTest {
	
	private Book book;
	
	 // ─── THOUGHT PROCESS ───────────────────────────────────────────────
    // Why @BeforeEach here instead of @BeforeAll?
    //
    // @BeforeAll runs ONCE for all tests — the same object is shared.
    // If one test changes book.available, the next test sees that change.
    // That's a SIDE EFFECT — tests become order-dependent
    //
    // @BeforeEach gives each test a FRESH Book — complete isolation.
    // Test 5 cannot be affected by what Test 3 did. This is fundamental.
    // ────────────────────────────────────────────────────────────────────
	
	@BeforeEach
	void setUp() {
		book = new Book("978-0-13-468599-1","Effective Java","Joshua Bloch");
	}
	
	// group 1 - constructor valid inputs

	@Nested
	@DisplayName("Constructor - Valid inputs")
	class ConstructorValidTests{
		
		@Test
		@DisplayName("Should create a book with correct ISBN")
		void shouldCreateBookWithCorrectIsbn() {
			assertEquals("978-0-13-468599-1", book.getIsbn());
		}
		
		@Test
		@DisplayName("Should create a book with correct Title")
		void shouldCreateBookWithCorrectTitle() {
			assertEquals("Effective Java", book.getTitle());
		}
		
		@Test
		@DisplayName("Should create a book with correct Author")
		void shouldCreateBookWithCorrectAuthor() {
			assertEquals("Joshua Bloch", book.getAuthor());
		}
		
		@Test
		@DisplayName("Should be available by default when created")
		void shouldBeAvailableByDefault() {
			// "A newly added book is available" — if someone changes
            // the constructor default, this test catches it immediately
			assertTrue(book.isAvailable(),"A new book should be available by default");
		}
		
		@Test
		@DisplayName("Should trim whitespace from ISBN, title, and author")
		void shouldTrimWhitespaceFromFields() {
			// WHY test trimming? Because real data is messy.
            // Users type "  Effective Java  " — we should handle it.
			 Book bookWithSpaces = new Book(
	                    "  978-1234567890  ",
	                    "  Clean Code  ",
	                    "  Robert Martin  "
	            );
			 
			 assertAll(
					 ()-> assertEquals("978-1234567890", bookWithSpaces.getIsbn()),
					 ()-> assertEquals("Clean Code", bookWithSpaces.getTitle()),
					 ()-> assertEquals("Robert Martin", bookWithSpaces.getAuthor()));
		}
		
	}
	
	// group 2 - constructor validation ( invalid inputs)
	@Nested
	@DisplayName("Constructor validation - invalid inputs")
	class ConstructorValidationTest{
		
		@ParameterizedTest(name ="ISBN=''{0}'' should be rejected")
		@NullAndEmptySource // Covers null and ""
		@ValueSource(strings = {"   ","\t","\n"}) // covers blank Strings
		@DisplayName("Should throw exception for invalid ISBN")
		void shouldThrowExceptionForInvalidIsbn(String invalidIsbn) {
			IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
					()-> new Book(invalidIsbn,"Some Title","Some Author"),
					"Expected IllegalArgumentException for ISBN: " + invalidIsbn);
			
			assertTrue(ex.getMessage().contains("ISBN"),"Exceptio message should mention 'ISBN'");
		}
		
		@ParameterizedTest(name ="Title=''{0}'' should be rejected")
		@NullAndEmptySource
		@ValueSource(strings = {"  "})
		@DisplayName("Should throw exception for invalid title")
		void shouldThrowExceptionForInvalidTitle(String invalidTitle) {
			 IllegalArgumentException exception = assertThrows(
	                    IllegalArgumentException.class,
	                    () -> new Book("978-1234567890", invalidTitle, "Some Author")
	            );
	            assertTrue(exception.getMessage().contains("Title"));
		}
		
		@ParameterizedTest(name = "Author=''{0}'' should be rejected")
        @NullAndEmptySource
        @ValueSource(strings = {"   "})
        @DisplayName("Should throw exception for invalid author")
        void shouldThrowExceptionForInvalidAuthor(String invalidAuthor) {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> new Book("978-1234567890", "Some Title", invalidAuthor)
            );
            assertTrue(exception.getMessage().contains("Author"));
        }
	}
	
	@Nested
	@DisplayName("Availability state")
	class AvailabilityTests{
		
		@Test
		@DisplayName("Should mark book as unavailable")
		void shouldMarkBookUnavailable() {
			 book.setAvailable(false);
			 assertFalse(book.isAvailable()); 
		}
		
		void shouldMarkBookAsAvailableAfterReturn() {
			book.setAvailable(false); // borrow it
			book.setAvailable(true); // return it
			assertTrue(book.isAvailable(),"Book should be available after being returned") ;
		}
	}
	
	// group 4 - equals() & hascode()
	

    @Nested
    @DisplayName("Equality and HashCode")
    class EqualityTests {

        @Test
        @DisplayName("Two books with the same ISBN should be equal")
        void booksWithSameIsbnShouldBeEqual() {
            Book book1 = new Book("978-1234567890", "Title A", "Author A");
            Book book2 = new Book("978-1234567890", "Title B", "Author B");
         
            assertEquals(book1, book2);
        }

        @Test
        @DisplayName("Two books with different ISBNs should not be equal")
        void booksWithDifferentIsbnsShouldNotBeEqual() {
            Book book1 = new Book("978-1111111111", "Title", "Author");
            Book book2 = new Book("978-2222222222", "Title", "Author");
            assertNotEquals(book1, book2);
        }

        @Test
        @DisplayName("Equal books should have equal hash codes")
        void equalBooksShouldHaveEqualHashCodes() {
            Book book1 = new Book("978-1234567890", "Title A", "Author A");
            Book book2 = new Book("978-1234567890", "Title B", "Author B");
       
            assertEquals(book1.hashCode(), book2.hashCode());
        }

        @Test
        @DisplayName("Book should equal itself (reflexivity)")
        void bookShouldEqualItself() {
            assertEquals(book, book);
        }

        @Test
        @DisplayName("Book should not equal null")
        void bookShouldNotEqualNull() {
            assertNotEquals(null, book);
        }
    }

 
    // group 5 - toString()
  
    @Nested
    @DisplayName("String Representation")
    class ToStringTests {

        @Test
        @DisplayName("toString should contain ISBN, title, author, and availability")
        void toStringShouldContainKeyFields() {
            String result = book.toString();
            assertAll("toString should contain all key fields",
                    () -> assertTrue(result.contains("978-0-13-468599-1"), "Should contain ISBN"),
                    () -> assertTrue(result.contains("Effective Java"), "Should contain title"),
                    () -> assertTrue(result.contains("Joshua Bloch"), "Should contain author"),
                    () -> assertTrue(result.contains("true"), "Should contain availability")
            );
        }
    }
}
