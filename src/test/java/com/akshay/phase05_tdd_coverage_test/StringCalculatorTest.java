// StringCalculatorTest.java
package com.akshay.phase05_tdd_coverage_test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.akshay.phase05_tdd_coverage.StringCalculator;

/**
 * TEST CLASS — StringCalculator
 * Phase 5 Practice Session 1
 *
 * Tests written BEFORE implementation (TDD order):
 * 1. add_emptyString_returnsZero
 * 2. add_singleNumber_returnsThatNumber
 * 3. add_twoNumbers_returnsSum
 * 4. add_newlineAsDelimiter_returnsSum
 * 5. add_negativeNumber_throwsException
 */

class StringCalculatorTest {
	
	private final StringCalculator cal = new StringCalculator();
	
	@Test
	void add_empty_string_return_zero() {
		assertEquals(0, cal.add(""));
	}
	
	@Test
	void add_single_number_returs_that_number() {
		assertEquals(1, cal.add("1"));
		assertEquals(5, cal.add("5"));
	}
	
	@Test
	void add_two_numbers_returns_sum() {
		assertEquals(3, cal.add("1,2"));
		assertEquals(10, cal.add("4,6"));
	}
	
	@Test
	void add_newline_as_delimeter_returns_sum() {
		assertEquals(6,cal.add("1\n2,3")); // mix of \n and ,
	} 
	
	@Test
	void add_negativeNumber_throwsException() {
	    Exception ex = assertThrows(IllegalArgumentException.class,
	        () -> cal.add("-1,2"));
	    assertTrue(ex.getMessage().contains("-1"));
	}
	
}