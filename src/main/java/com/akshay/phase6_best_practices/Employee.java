package com.akshay.phase6_best_practices;

import java.time.LocalDate;

public class Employee {
	
	private String name;
	private double salary;
	private LocalDate joinDate;
	
	public Employee(String name, double salary, LocalDate joinDate) {
		super();
		this.name = name;
		this.salary = salary;
		this.joinDate = joinDate;
	}

	public String getName() {
		return name;
	}

	public double getSalary() {
		return salary;
	}

	public LocalDate getJoinDate() {
		return joinDate;
	}
	
	
	

}
