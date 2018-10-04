package com.bridgelabz.fundoo.user.services;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundoo.user.models.Email;

@Service
public class EmailServiceImplementation implements EmailService {

	@Autowired
	public JavaMailSender emailSender;

	public void sendEmail(Email email) throws MessagingException {

		MimeMessage message = emailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);

		helper.setTo(email.getTo());
		helper.setSubject(email.getSubject());
		helper.setText(email.getText());

		emailSender.send(message);

	}

}
