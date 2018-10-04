package com.bridgelabz.fundoo.user.listeners;

import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundoo.user.models.Email;
import com.bridgelabz.fundoo.user.services.EmailService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

@Service
public class SQSListener implements MessageListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(SQSListener.class);

	@Autowired
	private EmailService emailService;

	@Override
	public void onMessage(Message message) {

		System.out.println("In Listener");
		TextMessage textMessage = (TextMessage) message;
		ObjectMapper mapper = new ObjectMapper();
		Email email;
		try {
			email = mapper.readValue(textMessage.getText(), Email.class);
			emailService.sendEmail(email);
		} catch (JsonParseException e1) {
			e1.printStackTrace();
		} catch (JsonMappingException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (JMSException e1) {
			e1.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}

	}

}
