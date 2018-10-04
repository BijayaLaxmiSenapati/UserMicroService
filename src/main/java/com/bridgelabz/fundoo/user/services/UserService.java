package com.bridgelabz.fundoo.user.services;

import java.io.IOException;

import javax.mail.MessagingException;

import org.springframework.web.multipart.MultipartFile;

import com.bridgelabz.fundoo.user.exceptions.LoginException;
import com.bridgelabz.fundoo.user.exceptions.RegistrationException;
import com.bridgelabz.fundoo.user.exceptions.UnAuthorisedAccessException;
import com.bridgelabz.fundoo.user.models.LoginDTO;
import com.bridgelabz.fundoo.user.models.ProfileDTO;
import com.bridgelabz.fundoo.user.models.RegistrationDTO;
import com.bridgelabz.fundoo.user.models.ResetPasswordDTO;

public interface UserService {

	String login(LoginDTO loginDTO) throws LoginException;

	void register(RegistrationDTO registrationDTO) throws RegistrationException, MessagingException, IOException;

	void activateUser(String token);

	void forgotPassword(String emailId) throws MessagingException, LoginException;

	void resetPassword(String token, ResetPasswordDTO resetPasswordDTO) throws LoginException, UnAuthorisedAccessException;

	String addProfilePicture(String token, MultipartFile multiPartImage);

	void removeProfilePicture(String userId, String urlOfProfilePicture);

	ProfileDTO getProfilePicture(String header);

}
