package com.akshay.phase04_mockito.practice2_test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.akshay.phase04_mockito.practice2.INotifier;
import com.akshay.phase04_mockito.practice2.IPaymentGateway;
import com.akshay.phase04_mockito.practice2.PaymentProcessor;

@ExtendWith(MockitoExtension.class)
class PaymentProcessorTest {

	@Mock
	IPaymentGateway paymentGateway;

	@Mock
	INotifier notifier;

	@InjectMocks
	PaymentProcessor paymentProcessor;

	// Test 1: Payment succeeds → customer is notified
	@Test
	void process_shouldNotifyCustomer_whenPaymentSucceeds() {
		when(paymentGateway.processPayment("CUST001", 500.0)).thenReturn(true);

		boolean result = paymentProcessor.process("CUST001", 500.0);

		assertTrue(result);
		verify(notifier, times(1)).notifyCustomer("CUST001", 500.0);
	}

	// Test 2: Payment fails → customer is NOT notified
	@Test
	void process_shouldNotNotifyCustomer_whenPaymentFails() {
		when(paymentGateway.processPayment("CUST002", 300.0)).thenReturn(false);

		boolean result = paymentProcessor.process("CUST002", 300.0);

		assertFalse(result);
		verify(notifier, never()).notifyCustomer(anyString(), anyDouble());
	}

	// Test 3: Gateway throws exception → exception propagates, no notification
	@Test
	void process_shouldPropagateException_whenGatewayFails() {
		when(paymentGateway.processPayment(anyString(), anyDouble()))
				.thenThrow(new RuntimeException("Gateway timeout"));

		assertThrows(RuntimeException.class, () -> paymentProcessor.process("CUST003", 999.0));

		verify(notifier, never()).notifyCustomer(anyString(), anyDouble());
	}

	// Test 4: Verify gateway was called with correct amount
	@Test
	void process_shouldCallGatewayWithCorrectAmount() {
		when(paymentGateway.processPayment("CUST004", 150.0)).thenReturn(true);

		paymentProcessor.process("CUST004", 150.0);

		verify(paymentGateway, times(1)).processPayment("CUST004", 150.0);
	}
}
