// UserServiceTest.java
package com.akshay.phase04_mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
	
	@Mock
	IUserRepository repository;
	
	@Mock
	IEmailService emailService;
	
	@InjectMocks
	UserService userService;
	
	// Test 1 - successfull registration
	@Test
	void register_shouldSaveUserAndSendEmail_whenEmaiIsNew() {
		// arrange
		String name = "Alice";
		String email = "alice@example.com";
		User savedUser = new User(1L,name,email);
		
		when(repository.existsByEmail(email)).thenReturn(false);
		when(repository.save(any(User.class))).thenReturn(savedUser);
		
		// Act
		User result = userService.register(name, email);
		
		//assert
		assertEquals("Alice", result.getName());
		assertEquals(1L,result.getId());
		
		// verify emailService was called exactly once
		verify(emailService,times(1)).sendWelcomeEmail(email);
		
	}
	
	// Test 2 - Duplicate Email should throw exception
	@Test
	void register_shouleThrowException_whenEmailAlreadyExists() {
		// arrange
		when(repository.existsByEmail("alice@example.com")).thenReturn(true);
		
		// act + assert
		IllegalArgumentException ex  = assertThrows(IllegalArgumentException.class,
				() -> userService.register("Alice", "alice@example.com"));
		
		assertEquals("Email already registered: alice@example.com", ex.getMessage());
		
		// verify email was never sent
		verify(emailService,never()).sendWelcomeEmail("alice@example.com");
	}
	
	// Test-3 database throws exception
	@Test
	void register_shouldPropogateException_whenRepositoryFails() {
		// Arrange
		when(repository.existsByEmail(anyString())).thenReturn(false);
		when(repository.save(any(User.class))).thenThrow(new RuntimeException("DB Connection lost"));
		
		// act + assert
		RuntimeException ex = assertThrows(RuntimeException.class,()->userService.register("Bob","bob@example.com"));
		
		assertEquals("DB Connection lost", ex.getMessage());
		
		 // Email must NOT have been sent if save failed
		verify(emailService,never()).sendWelcomeEmail("bob@example.com");
	}
	

}
