// PasswordStrengthValidatorTest.java
package com.akshay.phase03_parameterized_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class PasswordStrengthValidatorTest {
	
	@ParameterizedTest
	@DisplayName("Check password strengh")
	@CsvSource({
	    // Strong
	    "Abc123#b, Strong Password",
	    "Password1!, Strong Password",
	    "Java2025$, Strong Password",
	    "Test@123A, Strong Password",

	    // Medium
	    "Abc123abc, Medium Password",
	    "Password1, Medium Password",
	    "Java2025A, Medium Password",
	    "TESTabc1, Medium Password",

	    // Weak - only digits
	    "12345678, Weak Password",

	    // Weak - only lowercase
	    "abcdefgh, Weak Password",

	    // Weak - only uppercase
	    "ABCDEFGH, Weak Password",

	    // Weak - upper + lower only
	    "AbCdEfGh, Weak Password",

	    // Weak - lower + digit
	    "abc12345, Weak Password",

	    // Weak - upper + digit
	    "ABC12345, Weak Password",

	    // Weak - special only
	    "@#$%^&*!, Weak Password",

	    // Weak - letters + special, no digit
	    "Abcdef@#, Weak Password",

	    // Weak - digit + special, no letters
	    "1234@#$%, Weak Password"
	})
	@Tag("fast")
	void shouldReturnPasswordStrength(String password,String expected) {
		assertEquals(expected, PasswordStrengthValidator.passwordValidator(password));
	}
	
	
	@Nested
	class ExceptionThrows{
		@Test
		@Tag("fast")
		@DisplayName("null password throws Exception")
		void null_password_should_throw_exception() {
		    assertThrows(IllegalArgumentException.class,
		        () -> PasswordStrengthValidator.passwordValidator(null));
		}
		
		@Test
		@DisplayName("Empty password throws Exception")
		@Tag("fast")
		void testEmptyPasswordShouldThroException() {
			assertThrows(IllegalArgumentException.class,()->PasswordStrengthValidator.passwordValidator(""));
		}
		
		@ParameterizedTest
		@DisplayName("Invalid password length should throw exception")
		@Tag("fast")
		@ValueSource(strings = {"1234567","abc","akshay2"})
		void testInvalidStrengthThrowException(String password) {
			assertThrows(IllegalArgumentException.class,() -> PasswordStrengthValidator.passwordValidator(password) );
		}
	}
	
	

}
