package com.bridgelabz.fundoo.user.services;

import javax.mail.MessagingException;

import com.bridgelabz.fundoo.user.exceptions.LoginException;
import com.bridgelabz.fundoo.user.exceptions.RegistrationException;
import com.bridgelabz.fundoo.user.exceptions.UnAuthorisedAccessException;
import com.bridgelabz.fundoo.user.models.LoginDTO;
import com.bridgelabz.fundoo.user.models.RegistrationDTO;
import com.bridgelabz.fundoo.user.models.ResetPasswordDTO;

public interface UserService {

	String login(LoginDTO loginDTO) throws LoginException;

	void register(RegistrationDTO registrationDTO) throws RegistrationException, MessagingException;

	void activateUser(String token);

	void forgotPassword(String emailId) throws MessagingException, LoginException;

	void resetPassword(String token, ResetPasswordDTO resetPasswordDTO) throws LoginException, UnAuthorisedAccessException;

}
