package com.akshay.phase04_mockito.practice2;

public interface IPaymentGateway {
	boolean processPayment(String customerId, double amount);
}