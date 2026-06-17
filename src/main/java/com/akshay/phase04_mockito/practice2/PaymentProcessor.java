package com.akshay.phase04_mockito.practice2;

public class PaymentProcessor {

	private final IPaymentGateway paymentGateway;
	private final INotifier notifier;

	public PaymentProcessor(IPaymentGateway paymentGateway, INotifier notifier) {
		this.paymentGateway = paymentGateway;
		this.notifier = notifier;
	}

	public boolean process(String customerId, double amount) {
		boolean success = paymentGateway.processPayment(customerId, amount);

		if (success) {
			notifier.notifyCustomer(customerId, amount);
		}

		return success;
	}
}
