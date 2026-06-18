// PasswordValidatorTest.java
package com.akshay.phase05_tdd_coverage_test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

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

}
