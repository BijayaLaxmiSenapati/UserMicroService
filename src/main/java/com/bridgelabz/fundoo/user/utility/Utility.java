package com.bridgelabz.fundoo.user.utility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.web.multipart.MultipartFile;

import com.bridgelabz.fundoo.user.exceptions.LoginException;
import com.bridgelabz.fundoo.user.exceptions.RegistrationException;
import com.bridgelabz.fundoo.user.models.LoginDTO;
import com.bridgelabz.fundoo.user.models.RegistrationDTO;
import com.bridgelabz.fundoo.user.models.ResetPasswordDTO;

public class Utility {

	private static final String EMAIL_REGEX = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	private static final String PASSWORD_REGEX = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=?])(?=\\S+$).{8,32}";

	private static final String CONTACT_REGEX = "^[0-9]{10}$";
	private static Pattern pattern;

	private static Matcher matcher;

	/**
	 * @param email
	 * @return
	 */
	public static boolean validateEmail(String email) {
		pattern = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);
		matcher = pattern.matcher(email);
		return matcher.matches();
	}

	public static boolean validatePassword(String password) {
		pattern = Pattern.compile(PASSWORD_REGEX, Pattern.CASE_INSENSITIVE);
		matcher = pattern.matcher(password);
		return matcher.matches();
	}
	
	public static boolean validateContactNumber(String contactNumber) {
		pattern = Pattern.compile(CONTACT_REGEX, Pattern.CASE_INSENSITIVE);
		matcher = pattern.matcher(contactNumber);
		return matcher.matches();
	}
	/**
	 * @param registrationDTO
	 * @throws RegistrationException
	 */
	public static void validateUserWhileRegistering(RegistrationDTO registrationDTO) throws RegistrationException {

		if (registrationDTO.getName() == null || registrationDTO.getName().trim().length() == 0
				|| registrationDTO.getContactNumber() == null || registrationDTO.getContactNumber().trim().length() == 0
				|| registrationDTO.getPassword() == null || registrationDTO.getPassword().trim().length() == 0
				|| registrationDTO.getConfirmPassword() == null
				|| registrationDTO.getConfirmPassword().trim().length() == 0 || registrationDTO.getEmail() == null
				|| registrationDTO.getEmail().trim().length() == 0) {
			throw new RegistrationException("All fields should be filled");
		} else if (registrationDTO.getName().length() <= 3 || registrationDTO.getName().length() >=30) {
			throw new RegistrationException("Name should have atleast 3 charecters or less than 31 charecters");
		} else if (!validateContactNumber(registrationDTO.getContactNumber())) {//////////
			throw new RegistrationException("Contact number should have 10 digits");
		} else if (!validatePassword(registrationDTO.getPassword())) {
			throw new RegistrationException("Password should have atleast 8 alpha numeric charecters and less than 32 alpha numeric charecters");
		} else if (!registrationDTO.getPassword().equals(registrationDTO.getConfirmPassword())) {
			throw new RegistrationException("Both 'password' and 'confirmPassword' field should have same value");
		}
		if (!validateEmail(registrationDTO.getEmail())) {
			throw new RegistrationException("Email Format not correct");
		}

	}

	/**
	 * @param loginDTO
	 * @throws LoginException
	 */
	public static void validateUserWhileLogin(LoginDTO loginDTO) throws LoginException {
		if (loginDTO.getEmail() == null || loginDTO.getEmail().equals("") || loginDTO.getPassword() == null
				|| loginDTO.getPassword().equals("")) {
			throw new LoginException("All fields should be filled");
		} else if (loginDTO.getPassword().length() < 8 || loginDTO.getPassword().length() > 32) {
			throw new LoginException("Password should have atleast 8 charecters and less than 32 charecters");
		}
		if (!validateEmail(loginDTO.getEmail())) {
			throw new LoginException("Email Format not correct");
		}
	}

	/**
	 * @param resetPasswordDTO
	 * @throws LoginException
	 */
	public static void validateWhileResetPassword(ResetPasswordDTO resetPasswordDTO) throws LoginException {
		if (resetPasswordDTO.getNewPassword() == null || resetPasswordDTO.getConfirmNewPassword() == null) {
			throw new LoginException("Both \"newPassword\" and \"confirmPassword\" fields should not be empty");
		} else if (resetPasswordDTO.getNewPassword().length() < 8
				|| resetPasswordDTO.getConfirmNewPassword().length() < 8) {
			throw new LoginException("Password should have atleast 8 charecters");
		}
		if (!resetPasswordDTO.getNewPassword().equals(resetPasswordDTO.getConfirmNewPassword())) {
			throw new LoginException("Both 'newPassword' and 'confirmNewPassword' field value should be same");
		}
	}

	/**
	 * @param emailid
	 * @throws LoginException
	 */
	public static void validateWhileForgotPassword(String emailid) throws LoginException {
		if (!validateEmail(emailid)) {
			throw new LoginException("Email Format not correct");
		}
	}

	public static String generateUUID() {

		UUID gfg = UUID.randomUUID();
		return gfg.toString();
	}

	public static File convert(MultipartFile file) {
		File convFile = new File(file.getOriginalFilename());
		try {
			convFile.createNewFile();
			FileOutputStream fos;
			fos = new FileOutputStream(convFile);
			fos.write(file.getBytes());
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return convFile;
	}

}
