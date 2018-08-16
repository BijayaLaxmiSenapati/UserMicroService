package com.bridgelabz.fundoo.user.services;

import javax.mail.MessagingException;

import com.bridgelabz.fundoo.user.models.Email;
public interface EmailService {

	public void sendEmail(Email email) throws MessagingException;

}
