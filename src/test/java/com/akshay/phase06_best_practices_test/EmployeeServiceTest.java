// EmployeeServiceTest.java
package com.akshay.phase06_best_practices_test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.akshay.phase6_best_practices.Employee;
import com.akshay.phase6_best_practices.EmployeeService;

class EmployeeServiceTest {
	
	private EmployeeService employeeService;
	private Clock fixedClock;
	
	@BeforeEach
	void setUp() {
		fixedClock = Clock.fixed(Instant.parse("2024-06-15T10:00:00Z"), ZoneOffset.UTC);
		employeeService = new EmployeeService(fixedClock);
	}
	
	// --- isEligibleForBonus tests ---
	
	@Test // Naming convention to name test method - methodName - stateUnderTest - expectedBehavior -followed 
	void isEligibleForBonus_whenEmployeeHasOneYearTenureAndHighSalary_shouldReturnTrue() {
		// AAA-Pattern followed
		
		// Arrange
		Employee employee = new Employee("Alice	", 60000, LocalDate.of(2023,6, 15));
		
		// Act
		boolean result = employeeService.isEligibleForBonus(employee);
		
		// Assert
		assertThat(result).isTrue(); // One logical assertion for the test
	}
	
	@Test
	void isEligibleForBonus_whenEmployeeJoinedRecentlyWithHighSalary_shouldReturnFalse() {
		// Arrange
		Employee employee = new Employee("Bob",80000, LocalDate.of(2024, 1, 1));
		
		// Act
		boolean result = employeeService.isEligibleForBonus(employee);
		
		// Assert
		assertThat(result).isFalse();
	}
	
	@Test
	void isEligibleForBonus_whenEmployeeHasOneYearTenureButLessSalary_shouldReturnFalse() {
		// Arrange
		Employee employee = new Employee("Martin", 40000, LocalDate.of(2022, 6, 15));
		
		// Act
		boolean result = employeeService.isEligibleForBonus(employee);
		
		// Assert
		assertThat(result).isFalse();
	}
	
	@Test
	void isEligibleForBonus_whenEmployeeIsNull_shouldThrowIllegalArgumentException() {
		//Arrange + Act + Assert combined for exception test
		assertThatThrownBy(()-> employeeService.isEligibleForBonus(null))
		.isInstanceOf(IllegalArgumentException.class)
		.hasMessageContaining("must not be null");
	}
	
	 // --- calculateTax tests ---
	
	@Test
	void calculateTax_withSalaryBelowThreshold_shouldApplyTweentyPercentTax() {
		// Arrange
		Employee employee = new Employee("John", 400000, LocalDate.of(2020, 1, 1));
		
		// Act
		double tax = employeeService.calculateTax(employee);
		
		// Assert
		assertThat(tax).isEqualTo(80000.0);
	}
	
	@Test
    void calculateTax_withSalaryAboveThreshold_shouldApplyThirtyPercentTax() {
        // Arrange
        Employee employee = new Employee("Eve", 600000, LocalDate.of(2020, 1, 1));

        // Act
        double tax = employeeService.calculateTax(employee);

        // Assert
        assertThat(tax).isEqualTo(180000.0);
    }
	
	@Test
	void calculateTax_withZeroSalary_shouldThrowIllegalArgumentException() {
		// Arrange
		Employee employee = new Employee("Jack", 0, LocalDate.of(2020, 1, 1));
		
		//Act + assert
		assertThatThrownBy(()->employeeService.calculateTax(employee))
		.isInstanceOf(IllegalArgumentException.class)
		.hasMessageContaining("Salary must be positive");
	}
	
    // --- Soft assertions example ---
	
	void employee_shouldHaveAllCorrectFieldsAfterCreation() {
		// Arrange
		LocalDate joinDate = LocalDate.of(2022, 3, 10);
		Employee employee = new Employee("Grace", 75000,joinDate);
		
		// Assert with soft assertion - all checked at once
		SoftAssertions softly = new SoftAssertions();
		
		softly.assertThat(employee.getName()).isEqualTo("Grace");
		softly.assertThat(employee.getSalary()).isEqualTo(75000);
		softly.assertThat(employee.getJoinDate()).isEqualTo(joinDate);
		softly.assertAll();
	}
	
	
}
