// PasswordValidatorTest.java
package com.akshay.phase05_tdd_coverage_test;


/**
 * TEST CLASS — PasswordValidator
 * Phase 5 Practice Session 2
 *
 * Tests written following TDD:
 * 1. empty/blank throws exception          (first RED-GREEN committed separately)
 * 2. isValid — length boundary tests
 * 3. getFailureReasons — each rule
 * 4. getStrengthScore — scoring levels
 */

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.akshay.phase05_tdd_coverage.PasswordValidator;

class PasswordValidatorTest {
	
	@ParameterizedTest
	@ValueSource(strings = {
		// same character only
		"12345678",
		"abcdefgh",
		"ABCDEFGH",
		"@#$*@!#@",
		
		// mix character upper,lower,special,digit 
		"Xyz@1234",
		
		//upper,lower chars only
		"AbcdEfgh",
		
		//upper,lower,digit
		"Abc1234D"
	})
	void add_valid_password_returns_true(String password) {
		assertTrue(PasswordValidator.isValid(password));
	}
	
	@Test
	void add_invalid_password_returns_false() {
		assertFalse(PasswordValidator.isValid("1234567"));
	}
	
	@Test
	void add_empty_password_throws_exception() {
		assertAll(
			() -> assertThrows(IllegalArgumentException.class, () -> PasswordValidator.isValid("")),
			() -> assertThrows(IllegalArgumentException.class, () -> PasswordValidator.isValid("      "))
		);
	}
	
	 @Test
	    void getFailureReasons_emptyPassword_throwsException() {
	        assertThrows(IllegalArgumentException.class,
	            () -> PasswordValidator.getFailureReasons(""));
	    }

	    @Test
	    void getFailureReasons_tooShort_containsLengthReason() {
	        List<String> reasons = PasswordValidator.getFailureReasons("Ab1@");
	        assertTrue(reasons.stream().anyMatch(r -> r.contains("Too short")));
	    }

	    @Test
	    void getFailureReasons_noUppercase_containsUppercaseReason() {
	        List<String> reasons = PasswordValidator.getFailureReasons("abc12345");
	        assertTrue(reasons.stream().anyMatch(r -> r.contains("uppercase")));
	    }

	    @Test
	    void getFailureReasons_noLowercase_containsLowercaseReason() {
	        List<String> reasons = PasswordValidator.getFailureReasons("ABC12345");
	        assertTrue(reasons.stream().anyMatch(r -> r.contains("lowercase")));
	    }

	    @Test
	    void getFailureReasons_noDigit_containsDigitReason() {
	        List<String> reasons = PasswordValidator.getFailureReasons("Abcdefgh");
	        assertTrue(reasons.stream().anyMatch(r -> r.contains("digit")));
	    }

	    @Test
	    void getFailureReasons_noSpecialChar_containsSpecialReason() {
	        List<String> reasons = PasswordValidator.getFailureReasons("Abcd1234");
	        assertTrue(reasons.stream().anyMatch(r -> r.contains("special")));
	    }

	    @Test
	    void getFailureReasons_strongPassword_returnsEmptyList() {
	        List<String> reasons = PasswordValidator.getFailureReasons("Xyz@1234");
	        assertTrue(reasons.isEmpty());
	    }

	    // ─── getStrengthScore() tests ────────────────────────────────────

	    @Test
	    void getStrengthScore_emptyPassword_throwsException() {
	        assertThrows(IllegalArgumentException.class,
	            () -> PasswordValidator.getStrengthScore(""));
	    }

	    @Test
	    void getStrengthScore_allCriteriaMet_returns5() {
	        assertEquals(5, PasswordValidator.getStrengthScore("Xyz@1234"));
	    }


	    @Test
	    void getStrengthScore_lengthAndUpperAndLower_returns3() {
	        assertEquals(3, PasswordValidator.getStrengthScore("Abcdefgh"));
	    }

	    @Test
	    void getStrengthScore_shortPassword_returns0() {
	        // under 8 chars, no upper, no special → low score
	        assertEquals(2, PasswordValidator.getStrengthScore("abc1")); // lower + digit only
	    }
	
		
}
