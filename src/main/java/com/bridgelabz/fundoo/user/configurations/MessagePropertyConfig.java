package com.bridgelabz.fundoo.user.configurations;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Configuration
@PropertySource("classpath:message.properties")
@ConfigurationProperties(prefix = "message.controller")
@Component
public class MessagePropertyConfig {
	private int successfulStatus;
	
	private String loginMsg;

	private String registerMsg;

	private String activateMsg;

	private String forgotPasswordMsg;

	private String resetPasswordMsg;

	public String getLoginMsg() {
		return loginMsg;
	}

	public void setLoginMsg(String loginMsg) {
		this.loginMsg = loginMsg;
	}

	public String getRegisterMsg() {
		return registerMsg;
	}

	public void setRegisterMsg(String registerMsg) {
		this.registerMsg = registerMsg;
	}

	public String getActivateMsg() {
		return activateMsg;
	}

	public void setActivateMsg(String activateMsg) {
		this.activateMsg = activateMsg;
	}

	public String getForgotPasswordMsg() {
		return forgotPasswordMsg;
	}

	public void setForgotPasswordMsg(String forgotPasswordMsg) {
		this.forgotPasswordMsg = forgotPasswordMsg;
	}

	public String getResetPasswordMsg() {
		return resetPasswordMsg;
	}

	public void setResetPasswordMsg(String resetPasswordMsg) {
		this.resetPasswordMsg = resetPasswordMsg;
	}

	public int getSuccessfulStatus() {
		return successfulStatus;
	}

	public void setSuccessfulStatus(int successfulStatus) {
		this.successfulStatus = successfulStatus;
	}

}

