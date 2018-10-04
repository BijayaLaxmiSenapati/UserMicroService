package com.bridgelabz.fundoo.user.repositories;

import java.util.Optional;
import com.bridgelabz.fundoo.user.models.User;

public interface UserRepositoryES {

	public User save(User user);

	public Optional<User> findById(String id);
	
	public Optional<User> findByEmail(String emailId);
}
