// EmailValidatorTest.java
package com.akshay.phase03_parameterized_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class EmailValidatorTest {
	@ParameterizedTest
	@CsvSource({
	    // Valid emails (5)
	    "john@gmail.com, true",
	    "user123@test.org, true",
	    "abc@company.co.in, true",
	    "first.last@example.com, true",
	    "name@sub.domain.com, true",

	    // Invalid emails (5)
	    "'', false",
	    "johngmail.com, false",
	    "@gmail.com, false",
	    "john@, false",
	    "john@gmail, false"
	})
	void validateEmails(String email, boolean expected) {
	    assertEquals(expected, EmailValidator.isValid(email));
	}

	// @Nested class for valid emails
	@Nested
	class ValidEmails {
		@Test
		@Tag("fast")
		void simple_valid_email() {
			assertTrue(EmailValidator.isValid("john@gmail.com"));
		}

		@Test
		@Tag("fast")
		void email_with_subdomain() {
			assertTrue(EmailValidator.isValid("john@mail.company.com"));
		}

		@Test
		@Tag("fast")
		void email_with_numbers() {
			assertTrue(EmailValidator.isValid("user123@test.org"));
		}
	}
	
	 @Nested
	    class InvalidEmails {

	        @Test
	        @Tag("fast")
	        void null_email() {
	            assertFalse(EmailValidator.isValid(null));
	        }

	        @Test
	        @Tag("fast")
	        void empty_email() {
	            assertFalse(EmailValidator.isValid(""));
	        }

	        @Test
	        @Tag("fast")
	        void missing_at_sign() {
	            assertFalse(EmailValidator.isValid("john.gmail.com"));
	        }

	        @Test
	        @Tag("fast")
	        void missing_domain() {
	            assertFalse(EmailValidator.isValid("john@gmail"));
	        }
	    }

}
