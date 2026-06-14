// CalculatorTest.java 
package com.akshay.phase01_foundations_test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.akshay.phase01_foundations.Calculator;

/**
 * This is test class for unit testing , testing calculator class method
 * individually and check whether they perform correct
 */

public class CalculatorTest {

	@Test
	@DisplayName("Adding 2 + 5 return 7")
	public void addTwoPositiveNumber() {
		Calculator cal = new Calculator();
		assertEquals(7, cal.add(2, 5));
	}

	@Test
	@DisplayName("Adding -3 + -2 return -5")
	public void addTwoNegativeNumber() {
		Calculator cal = new Calculator();
		assertEquals(-5, cal.add(-3, -2));
	}

	@Test
	@DisplayName("Adding 0 + 0 return 0")
	public void addTwoZeros() {
		Calculator cal = new Calculator();
		assertEquals(0, cal.add(0, 0));
	}

	@Test
	@DisplayName("Adding -3 + 2 return -1")
	public void addPostiveAndNegative() {
		Calculator cal = new Calculator();
		assertEquals(-1, cal.add(-3, 2));
	}

	@Test
	@DisplayName("Subtract: 10 - 5 = 5")
	public void subtractTwoPostiveNumber() {
		assertEquals(5, new Calculator().subtract(10, 5));
	}

	@Test
	@DisplayName("Subtract: 3 - 5 = -2")
	public void subtractResultNegative() {
		assertEquals(-2, new Calculator().subtract(3, 5));
	}

	@Test
	@DisplayName("Multiply: 3 * 4 = 12")
	public void multiplyBasic() {
		assertEquals(12, new Calculator().multiply(3, 4));
	}

	@Test
	@DisplayName("Multiply by zero always return zero")
	public void multiplyByZero() {
		assertEquals(0, new Calculator().multiply(90, 0));
	}

	@Test
	@DisplayName("Divide by zero should throw ArithmeticException")
	public void divideByZeroThrowsException() {
		Calculator cal = new Calculator();
		assertThrows(ArithmeticException.class, () -> cal.divide(10, 0));
	}

}
