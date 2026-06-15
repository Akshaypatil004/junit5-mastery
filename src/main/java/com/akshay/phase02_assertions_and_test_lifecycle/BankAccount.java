// BankAccount.java 
package com.akshay.phase02_assertions_and_test_lifecycle;

public class BankAccount {
	
	private double balance;
	
	public BankAccount(double initialBalance) {
		if(initialBalance <= 0) {
			throw new IllegalArgumentException("Initial balance cannot be negative!");
		}
		this.balance = initialBalance;
	}
	
	public void deposit(double amount) {
		if(amount <= 0) {
			throw new IllegalArgumentException("Deposit amount must be positive");
		}
		this.balance += amount;
	}
	
	public void withdraw(double amount) {
		if(amount <= 0) {
			throw new IllegalArgumentException("Withdrawl amount must be positive");
		}
		if(amount > this.balance) {
			throw new IllegalArgumentException("Insufficient funds");
		}
		this.balance -= amount;
	}
	
	public double getBalance() {
		return this.balance;
	}

}
