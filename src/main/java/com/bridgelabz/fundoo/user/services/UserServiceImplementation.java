package com.bridgelabz.fundoo.user.services;

import java.util.Optional;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bridgelabz.fundoo.user.exceptions.LoginException;
import com.bridgelabz.fundoo.user.exceptions.RegistrationException;
import com.bridgelabz.fundoo.user.exceptions.UnAuthorisedAccessException;
import com.bridgelabz.fundoo.user.models.Email;
import com.bridgelabz.fundoo.user.models.LoginDTO;
import com.bridgelabz.fundoo.user.models.ProfileDTO;
import com.bridgelabz.fundoo.user.models.RegistrationDTO;
import com.bridgelabz.fundoo.user.models.ResetPasswordDTO;
import com.bridgelabz.fundoo.user.models.User;
import com.bridgelabz.fundoo.user.repositories.RedisRepository;
import com.bridgelabz.fundoo.user.repositories.UserRepository;
import com.bridgelabz.fundoo.user.repositories.UserRepositoryES;
import com.bridgelabz.fundoo.user.utility.TokenProvider;
import com.bridgelabz.fundoo.user.utility.Utility;

@Service
public class UserServiceImplementation implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserRepositoryES userRepositoryES;

	@Autowired
	private PasswordEncoder passwordEncoder;

	/*
	 * @Autowired private Producer producer;
	 */

	@Autowired
	private RedisRepository redisRepository;

	@Autowired
	private ImageStorageService imageStorageService;

	@Autowired
	private MessageService messageService;

	private static final String SUFFIX = "/";

	@Value("${profilePictures}")
	private String profilePictures;

	@Override
	public String login(LoginDTO loginDTO) throws LoginException {
		Utility.validateUserWhileLogin(loginDTO);

		Optional<User> optional = userRepository.findByEmail(loginDTO.getEmail());
		
		System.out.println(optional);

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

		Optional<User> optional = userRepository.findByEmail(registrationDTO.getEmail());

		System.out.println(optional);
		
		if (optional.isPresent()) {

			throw new RegistrationException("Email id already exists");
		}

		User user = new User();
		user.setName(registrationDTO.getName());
		user.setPassword(passwordEncoder.encode(registrationDTO.getConfirmPassword()));
		user.setContactNumber(registrationDTO.getContactNumber());
		user.setEmail(registrationDTO.getEmail());

		userRepository.insert(user);
		userRepositoryES.save(user);

		optional = userRepository.findByEmail(registrationDTO.getEmail());
		String userId = optional.get().getId();

		TokenProvider tokenProvider = new TokenProvider();
		String token = tokenProvider.generateToken(userId);

		Email email = new Email();
		email.setTo(registrationDTO.getEmail());
		email.setSubject("Activation Link");
		email.setText("http://192.168.0.62:8080/fundoo/activate/?token=" + token);

		messageService.sendMessage(email);

	}

	@Override
	public void activateUser(String token) {

		TokenProvider tokenProvider = new TokenProvider();
		String idFromToken = tokenProvider.parseToken(token);

		Optional<User> optional = userRepository.findById(idFromToken);

		User user = optional.get();

		user.setActivationStatus(true);

		userRepository.save(user);
		userRepositoryES.save(user);

	}

	@Override
	public void forgotPassword(String emailId) throws MessagingException, LoginException {

		Utility.validateWhileForgotPassword(emailId);

		Optional<User> optional = userRepository.findByEmail(emailId);

		String userId = optional.get().getId();

		String uuid = Utility.generateUUID();

		redisRepository.save(uuid, userId);
		System.out.println("email sending------");
		Email email = new Email();
		email.setTo(emailId);
		email.setSubject("Link for forgot password");
		email.setText("http://192.168.0.62:8091/fundoo/resetPassword/?uuid=" + uuid);
		System.out.println("email sent-------");
		
		messageService.sendMessage(email);

	}

	@Override
	public void resetPassword(String uuid, ResetPasswordDTO resetPasswordDTO)
			throws LoginException, UnAuthorisedAccessException {

		Utility.validateWhileResetPassword(resetPasswordDTO);

		String userId = redisRepository.find(uuid);

		if (userId == null) {
			throw new UnAuthorisedAccessException("Unauthorised access for changing password is not acceptable");
		}

		Optional<User> optionalUser = userRepository.findById(userId);

		User user = optionalUser.get();
		user.setPassword(passwordEncoder.encode(resetPasswordDTO.getNewPassword()));
		userRepository.save(user);
		userRepositoryES.save(user);

		redisRepository.delete(uuid);
	}

	@Override
	public String addProfilePicture(String userId, MultipartFile multiPartImage) {

		String folder = userId + SUFFIX + profilePictures;

		imageStorageService.uploadFile(folder, multiPartImage);

		String picture = imageStorageService.getFile(folder, multiPartImage.getOriginalFilename());

		Optional<User> optionalUser = userRepository.findById(userId);

		User user = optionalUser.get();

		user.setProfilePicture(picture);

		userRepository.save(user);
		userRepositoryES.save(user);

		return picture;
	}

	@Override
	public void removeProfilePicture(String userId, String urlOfProfilePicture) {

		String folder = userId + SUFFIX + profilePictures;

		String[] arr = urlOfProfilePicture.split("/");
		String profilePictureName = arr[arr.length - 1];

		imageStorageService.deleteFile(folder, profilePictureName);

		Optional<User> optionalUser = userRepository.findById(userId);

		User user = optionalUser.get();

		user.setProfilePicture(null);

		userRepository.save(user);
		userRepositoryES.save(user);
	}

	@Override
	public ProfileDTO getProfilePicture(String userId) {

		System.out.println(userId);
		User user = userRepository.findById(userId).get();
		ProfileDTO profileDTO = new ProfileDTO();
		profileDTO.setEmail(user.getEmail());
		profileDTO.setName(user.getName());
		profileDTO.setProfilePicture(user.getProfilePicture());
		return profileDTO;
	}
}
