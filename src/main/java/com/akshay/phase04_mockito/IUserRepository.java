// IUserRepository.java (interface)
package com.akshay.phase04_mockito;

public interface IUserRepository {
	User save(User user);
	boolean existsByEmail(String email);

}
