// BankAccountTest.java
package com.akshay.phase02_assertions_and_test_lifecycle_test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.akshay.phase02_assertions_and_test_lifecycle.BankAccount;

class BankAccountTest {

	private BankAccount account;

	// @BeforeEach - fresh account before every single test
	@BeforeEach
	void setup() {
		account = new BankAccount(1000);
	}

	@Test
	@DisplayName("deposit positive amount to check balance increases ")
	void testDepositIncreaseBalance() {
		account.deposit(500);
		assertEquals(1500, account.getBalance(), "Balance should be 1500 after depositing 500");
	}

	@Test
	@DisplayName("withdraw positve amount to check decrease balanace")
	void testWithdrawDecreaseBalance() {
		account.withdraw(300);
		assertEquals(700, account.getBalance(), "Balance should be 700 after withdraw 300");
	}

	@Test
	@DisplayName("multiple operation test")
	void testAccountAfterMultipleOperations() {
		account.deposit(200);
		account.withdraw(500);

		assertAll("Account state afer opeations", 
				() -> assertEquals(700, account.getBalance(), "Balance mismatch"),
				() -> assertNotNull(account, "Account should not be null"),
				() -> assertTrue(account.getBalance() > 0, "Balance should be positive"),
				() -> assertTrue(account.getBalance() > 1000, "Balance should be greater than 1000"));
	}

	@Test
	@DisplayName("withdraw amount greater than balance throws exception")
	void testWithdrawBeyondThrowsException() {
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> account.withdraw(5000));

		assertEquals("Insufficient funds", ex.getMessage());

	}

	@Test
	@DisplayName("deposit negative amount throws exception")
	void testNegativeDepositThrowsException() {
		assertThrows(IllegalArgumentException.class, () -> account.deposit(-100));
	}

	@Test
	@DisplayName("setting initial balance negative throws Exception")
	void testNegativeInitialBalanceThrowsException() {
		assertThrows(IllegalArgumentException.class, () -> new BankAccount(-1000));

	}
	
	@Test
	@Disabled("UPI integration not ready yet — see ticket #123")
	void testUpiTransfer() {
    // TODO document why this method is empty
 }
	
	@AfterEach
	void cleaup() {
		account = null;
	}

}
