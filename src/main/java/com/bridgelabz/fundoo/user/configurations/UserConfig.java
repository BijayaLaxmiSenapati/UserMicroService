package com.bridgelabz.fundoo.user.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bridgelabz.fundoo.user.models.User;
import com.bridgelabz.fundoo.user.rabbitmq.Producer;
import com.bridgelabz.fundoo.user.rabbitmq.ProducerImpl;
import com.bridgelabz.fundoo.user.services.EmailService;
import com.bridgelabz.fundoo.user.services.EmailServiceImplementation;

@Configuration
public class UserConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	@Scope("prototype")
	public User user() {
		return new User();
	}

	@Bean
	@Scope("prototype")
	public EmailService emailService() {
		return new EmailServiceImplementation();
	}

	@Bean
	@Scope("prototype")
	public Producer producer() {
		return new ProducerImpl();
	}
	
	/*@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}*/
}