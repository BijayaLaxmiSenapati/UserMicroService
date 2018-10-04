package com.bridgelabz.fundoo.user.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.bridgelabz.fundoo.user.models.User;

public interface UserRepository extends MongoRepository<User, String> {

	public Optional<User> findByEmail(String email);
	public User save(User user);

}
