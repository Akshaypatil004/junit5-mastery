// EmailValidator.java
package com.akshay.phase03_parameterized_tests;

public final class EmailValidator {
	
	private EmailValidator() {}
	
	public static boolean isValid(String email) {
		if(email == null || email.isEmpty()) {
			return false;
		}
		else if(!email.contains("@")) {
			return false;
		}
		else if(email.startsWith("@") || email.endsWith("@")) {
			return false;
		}
		else if(email.indexOf('.', email.indexOf("@")) == -1) {
			return false;
		}
		
		return true;
		
	}

}
