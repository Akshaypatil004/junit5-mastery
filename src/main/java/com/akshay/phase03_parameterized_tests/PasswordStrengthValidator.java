// PasswordStrengthValidator.java
package com.akshay.phase03_parameterized_tests;

public final class PasswordStrengthValidator {

	private PasswordStrengthValidator() {
	}

	public static String passwordValidator(String password) {

		if (password == null) {
			 throw new IllegalArgumentException("Password cannot be null");
		}

		boolean hasLower = password.matches(".*[a-z].*");
		boolean hasUpper = password.matches(".*[A-Z].*");
		boolean hasDigit = password.matches(".*\\d.*");
		boolean hasSpecial = password.matches(".*[^a-zA-Z0-9].*");

		if (password.isEmpty()) {
			throw new IllegalArgumentException("Password cannot be empty");
		}

		if (password.length() < 8) {
			throw new IllegalArgumentException("Password legth must be 8 character long");
		}

		if (hasLower && hasUpper && hasDigit && hasSpecial) {
			return "Strong Password";
		} else if (hasLower && hasUpper && hasDigit) {
			return "Medium Password";
		} else {
			return "Weak Password";
		}

	}
}
