package com.bridgelabz.fundoo.user.rabbitmq;

import javax.mail.MessagingException;

import com.bridgelabz.fundoo.user.models.Email;

public interface Consumer {
	/**
	 * @param email
	 * @throws MessagingException
	 */
	public void recievedMessage(Email email) throws MessagingException;
}
