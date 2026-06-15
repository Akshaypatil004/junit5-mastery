// StudentGradeTest.java
package com.akshay.phase02_assertions_and_test_lifecycle_test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.akshay.phase02_assertions_and_test_lifecycle.StudentGrade;

class StudentGradeTest {
	
	StudentGrade student;
	
	@BeforeEach // run fresh for every test
	void setup() {
		student = new StudentGrade("Akshay");
	}
	
	@Test
	@DisplayName("test: marks >= 90 give grade A")
	void testMarksToGetGradeA() {
		student.setTotalMarks(92);
		assertEquals("A Grade", student.getGrade());
	}
	
	@Test
	@DisplayName("test: marks between 75 to 89 give grade B ")
	void testMarkstoGetGradeB() {
		student.setTotalMarks(88);
		assertEquals("B Grade", student.getGrade());
	}
	
	@Test
	@DisplayName("test: marks between 60 to 74 give grade C ")
	void testMarkstoGetGradeC() {
		student.setTotalMarks(67);
		assertEquals("C Grade", student.getGrade());
	}
	
	@Test
	@DisplayName("test: marks below 60 give grade F")
	void testMarkstoGetGradeF() {
		student.setTotalMarks(55);
		assertEquals("F Grade", student.getGrade());
	}
	
	@Test
	@DisplayName("test: check student pass ?")
	void testCheckStudentPassOrNot() {
		student.setTotalMarks(60);
		assertTrue(student.isPassed());
	}
	
	@Test
	@DisplayName("test: empty name produced exception")
	void testNameEmptyThrowsException() {
		assertAll("empty name give exception",
				()-> assertThrows(IllegalArgumentException.class,
						() -> new StudentGrade(null)),
				()-> assertThrows(IllegalArgumentException.class,
						() -> new StudentGrade(""))
				
		);
		
	}
	
	@Test
	@DisplayName("test: invalid marks throws exception")
	void testInvalidMarksThrowsException() {
	    assertAll("invalid marks",
	        () -> assertThrows(IllegalArgumentException.class,
	                () -> student.setTotalMarks(-10)),   // negative
	        () -> assertThrows(IllegalArgumentException.class,
	                () -> student.setTotalMarks(110))    // greater than 100
	    );
	}
	
	@Test
	void testStudentProperties() {
	    student.setTotalMarks(85);
	    assertAll("student properties",
	        () -> assertEquals("Akshay", student.getName()),
	        () -> assertEquals(85, student.getTotalMarks()),
	        () -> assertEquals("B Grade", student.getGrade()),
	        () -> assertTrue(student.isPassed())
	    );
	}
	
	@Test
	@Disabled("Merit scholarship logic not implemented yet — ticket #201")
	void testMeritScholarship() {
	    // TODO
	}
	
	@Test
	void testStudentFailed() {
	    student.setTotalMarks(45);
	    assertFalse(student.isPassed());
	}
	
	@AfterEach
	void cleanup() {
		student = null;
		
	}

}
