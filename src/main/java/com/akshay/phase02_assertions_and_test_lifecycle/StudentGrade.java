// StudentGrade.java
package com.akshay.phase02_assertions_and_test_lifecycle;

public class StudentGrade {
	
	private String name;
	private int totalMarks;
	
	public StudentGrade(String name) {
		if(name == null || name.isEmpty()) {
			throw new IllegalArgumentException("Student name cannot be empty");
		}
		this.name = name;
	}
	
	public void setTotalMarks(int totalMarks) {
		if(totalMarks < 0 || totalMarks > 100) {
			throw new IllegalArgumentException("Marks must be between 0 to 100");
		}
		this.totalMarks = totalMarks;
	}
	
	public String getGrade() {
		if(totalMarks >= 90) {
			return "A Grade";
		}
		else if(totalMarks >= 75) {
			return "B Grade";
		}
		else if(totalMarks >= 60) {
			return "C Grade";
		}
		else {
			return "F Grade";
		}
	}
	
	public boolean isPassed() {
		return totalMarks >= 60;
	}
	
	public String getName() {
		return name;
	}
	
	public int getTotalMarks() {
		return totalMarks;
	}

}
