// UserService.java
package com.akshay.phase04_mockito;

public class UserService {
	
	private final IUserRepository repository;
	private final IEmailService emailService;
	
	public UserService(IUserRepository repository, IEmailService emailService) {
		super();
		this.repository = repository;
		this.emailService = emailService;
	}
	
	public User register(String name, String email) {
		if(repository.existsByEmail(email)) {
			throw new IllegalArgumentException("Email already registered: " + email);
		}
		
		User newUser = new User(null,name,email);
		User saveUser = repository.save(newUser);
		
		emailService.sendWelcomeEmail(email);
		
		return saveUser;
	}
}
