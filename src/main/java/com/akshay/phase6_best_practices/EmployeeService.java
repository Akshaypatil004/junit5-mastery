package com.akshay.phase6_best_practices;

import java.time.Clock;
import java.time.LocalDate;

public class EmployeeService {
	
	private final Clock clock;
	
	public EmployeeService(Clock clock) {
		this.clock = clock;
	}
	
	  /**
     * Returns true if the employee is eligible for annual bonus.
     * Eligibility: joined at least 1 year ago AND salary > 50000.
     */
	
	public boolean isEligibleForBonus(Employee employee) {
		if(employee == null) {
			throw new IllegalArgumentException("Employee must not be null");
		}
		
		LocalDate today = LocalDate.now(clock);
		LocalDate oneYearAgo = today.minusYears(1);
		
		boolean hasOneYearTenure = employee.getJoinDate().isBefore(oneYearAgo) || employee.getJoinDate().isEqual(oneYearAgo);
		
		boolean hasHighSalary = employee.getSalary() > 50000;
		
		return hasOneYearTenure && hasHighSalary;
	}
	
	 /**
     * Calculates tax for an employee.
     * Salary up to 500000 → 20% tax
     * Salary above 500000 → 30% tax
     */
	
	public double calculateTax(Employee employee) {
		if(employee == null) {
			throw new IllegalArgumentException("Employee must not be null");
		}
		
		if(employee.getSalary() <= 0) {
			throw new IllegalArgumentException("Salary must be positive");
		}
		
		if(employee.getSalary() <= 500000) {
			return employee.getSalary() * 0.20;
		}
		
		return employee.getSalary() * 0.30;
	}

}
