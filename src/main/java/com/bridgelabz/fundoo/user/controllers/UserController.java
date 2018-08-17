package com.bridgelabz.fundoo.user.controllers;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.fundoo.user.configurations.MessagePropertyConfig;
import com.bridgelabz.fundoo.user.exceptions.LoginException;
import com.bridgelabz.fundoo.user.exceptions.RegistrationException;
import com.bridgelabz.fundoo.user.exceptions.UnAuthorisedAccessException;
import com.bridgelabz.fundoo.user.models.LoginDTO;
import com.bridgelabz.fundoo.user.models.RegistrationDTO;
import com.bridgelabz.fundoo.user.models.ResetPasswordDTO;
import com.bridgelabz.fundoo.user.models.ResponseDTO;
import com.bridgelabz.fundoo.user.services.FacebookService;
import com.bridgelabz.fundoo.user.services.UserService;

@RestController
@RequestMapping("/fundoo")
public class UserController {

	@Autowired
	MessagePropertyConfig messagePropertyConfig;

	@Autowired
	private UserService userService;
	
	@Autowired
	private FacebookService facebookService;

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<ResponseDTO> login(@RequestBody LoginDTO loginDTO, HttpServletResponse response)
			throws LoginException {

		String tokenValue = userService.login(loginDTO);
		ResponseDTO dto = new ResponseDTO();
		dto.setMessage(messagePropertyConfig.getLoginMsg());
		dto.setStatus(messagePropertyConfig.getSuccessfulStatus());

		response.setHeader("token", tokenValue);
		return new ResponseEntity<>(dto, HttpStatus.OK);

	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseEntity<ResponseDTO> register(@RequestBody RegistrationDTO registrationDTO)
			throws RegistrationException, MessagingException {

		userService.register(registrationDTO);
		ResponseDTO dto = new ResponseDTO();
		dto.setMessage(messagePropertyConfig.getRegisterMsg());
		dto.setStatus(messagePropertyConfig.getSuccessfulStatus());

		return new ResponseEntity<>(dto, HttpStatus.CREATED);

	}

	/**
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/activate", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> activateUser(@RequestParam(value = "token") String token) {

		userService.activateUser(token);
		ResponseDTO dto = new ResponseDTO();
		dto.setMessage(messagePropertyConfig.getActivateMsg());
		dto.setStatus(messagePropertyConfig.getSuccessfulStatus());

		return new ResponseEntity<>(dto, HttpStatus.OK);
	}

	/**
	 * @param emailId
	 * @return
	 * @throws MessagingException
	 * @throws LoginException
	 */
	@RequestMapping(value = "/forgotpassword", method = RequestMethod.POST)
	public ResponseEntity<ResponseDTO> forgotPassword(@RequestBody String emailId)
			throws MessagingException, LoginException {

		userService.forgotPassword(emailId);
		ResponseDTO dto = new ResponseDTO();
		dto.setMessage(messagePropertyConfig.getForgotPasswordMsg());
		dto.setStatus(messagePropertyConfig.getSuccessfulStatus());

		return new ResponseEntity<>(dto, HttpStatus.OK);
	}

	/**
	 * @param token
	 * @param resetPasswordDTO
	 * @return
	 * @throws LoginException
	 * @throws UnAuthorisedAccessException 
	 */
	@RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
	public ResponseEntity<ResponseDTO> resetPassword(@RequestParam(value = "uuid") String uuid,
			@RequestBody ResetPasswordDTO resetPasswordDTO) throws LoginException, UnAuthorisedAccessException {

		userService.resetPassword(uuid, resetPasswordDTO);

		ResponseDTO dto = new ResponseDTO();
		dto.setMessage(messagePropertyConfig.getResetPasswordMsg());
		dto.setStatus(messagePropertyConfig.getSuccessfulStatus());

		return new ResponseEntity<>(dto, HttpStatus.OK);
	}
	
}

