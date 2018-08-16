package com.bridgelabz.fundoo.user.services;

import java.util.Optional;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundoo.user.exceptions.LoginException;
import com.bridgelabz.fundoo.user.exceptions.RegistrationException;
import com.bridgelabz.fundoo.user.exceptions.UnAuthorisedAccessException;
import com.bridgelabz.fundoo.user.models.Email;
import com.bridgelabz.fundoo.user.models.LoginDTO;
import com.bridgelabz.fundoo.user.models.RegistrationDTO;
import com.bridgelabz.fundoo.user.models.ResetPasswordDTO;
import com.bridgelabz.fundoo.user.models.User;
import com.bridgelabz.fundoo.user.rabbitmq.Producer;
import com.bridgelabz.fundoo.user.repositories.RedisRepository;
import com.bridgelabz.fundoo.user.repositories.UserRepository;
import com.bridgelabz.fundoo.user.utility.TokenProvider;
import com.bridgelabz.fundoo.user.utility.Utility;

@Service
public class UserServiceImplementation implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private Producer producer;
	
	@Autowired
	RedisRepository redisRepository;

	@Override
	public String login(LoginDTO loginDTO) throws LoginException {
		Utility.validateUserWhileLogin(loginDTO);

		Optional<User> optional = userRepository.findByEmail(loginDTO.getEmail());///////////////checked with ES

		if (!optional.isPresent()) {

			throw new LoginException("Email id not present");

		}
		User dbUser = optional.get();

		if (!passwordEncoder.matches(loginDTO.getPassword(), dbUser.getPassword())) {
			throw new LoginException("Wrong Password given");
		}
		if (!dbUser.isActivationStatus()) {
			throw new LoginException(
					"given account is not yet activated. First activate your account from the inbox, with mail SUBJECT \"Activation Link\"");
		}

		TokenProvider tokenProvider = new TokenProvider();
		String token = tokenProvider.generateToken(dbUser.getId());
		return token;

	}

	@Override
	public void register(RegistrationDTO registrationDTO) throws RegistrationException, MessagingException {

		Utility.validateUserWhileRegistering(registrationDTO);

		Optional<User> optional = userRepository.findByEmail(registrationDTO.getEmail());///////////checked with ES

		if (optional.isPresent()) {

			throw new RegistrationException("Email id already exists");
		}

		User user = new User();
		user.setName(registrationDTO.getName());
		user.setPassword(passwordEncoder.encode(registrationDTO.getConfirmPassword()));
		user.setContactNumber(registrationDTO.getContactNumber());
		user.setEmail(registrationDTO.getEmail());

		userRepository.insert(user);

		optional = userRepository.findByEmail(registrationDTO.getEmail());
		String userId = optional.get().getId();

		TokenProvider tokenProvider = new TokenProvider();
		String token = tokenProvider.generateToken(userId);

		Email email = new Email();
		email.setTo(registrationDTO.getEmail());
		email.setSubject("Activation Link");
		email.setText("http://192.168.0.62:8080/fundoo/activate/?token=" + token);

		producer.produceMsg(email);

	}

	@Override
	public void activateUser(String token) {

		TokenProvider tokenProvider = new TokenProvider();
		String idFromToken = tokenProvider.parseToken(token);

		Optional<User> optional = userRepository.findById(idFromToken);

		User user = optional.get();

		user.setActivationStatus(true);

		userRepository.save(user);

	}

	@Override
	public void forgotPassword(String emailId) throws MessagingException, LoginException {

		Utility.validateWhileForgotPassword(emailId);

		Optional<User> optional = userRepository.findByEmail(emailId);
		String userId = optional.get().getId();

		String uuid=Utility.generateUUID();
		
		redisRepository.save(uuid, userId);

		Email email = new Email();
		email.setTo(emailId);
		email.setSubject("Link for forgot password");
		email.setText("http://192.168.0.62:8080/fundoo/resetPassword/?uuid=" + uuid);

		producer.produceMsg(email);

	}

	@Override
	public void resetPassword(String uuid, ResetPasswordDTO resetPasswordDTO) throws LoginException, UnAuthorisedAccessException {

		Utility.validateWhileResetPassword(resetPasswordDTO);

		String userId=redisRepository.find(uuid);
		
		if(userId==null) {
			throw new UnAuthorisedAccessException("Unauthorised access for changing password is not acceptable");
		}
		
		Optional<User> optionalUser = userRepository.findById(userId);

		User user = optionalUser.get();
		user.setPassword(passwordEncoder.encode(resetPasswordDTO.getNewPassword()));
		userRepository.save(user);
		
		redisRepository.delete(uuid);
	}

}

