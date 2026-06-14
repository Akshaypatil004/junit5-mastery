// Calculator.java 
package com.akshay.phase01_foundations;

/**
 * This class contains methods to perform 
 * simple arithmetic operation like add, subtract, multiply, divide etc.
 */

public class Calculator {
	
	public int add(int a, int b) {
		return a+b;
	}
	
	public int subtract(int a, int b) {
		return a-b;
	}
	
	public int multiply(int a, int b) {
		return a*b;
	}
	
	public double divide(int a, int b) {
		if(b == 0) {
			throw new ArithmeticException("Cannot divide a number by zero!");
		}
		return a/b;
	}

}
