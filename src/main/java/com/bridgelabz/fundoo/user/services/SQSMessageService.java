package com.bridgelabz.fundoo.user.services;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundoo.user.models.Email;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

@Service
public class SQSMessageService implements MessageService {

	@Autowired
	private JmsTemplate jmsTemplate;

	@Value("${queue.name}")
	private String queueName;

	private static final Logger LOGGER = LoggerFactory.getLogger(MessageService.class);

	public void sendMessage(final Email email) {
		
System.out.println("In message service");
		jmsTemplate.send(queueName, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				ObjectMapper mapper = new ObjectMapper();
				String mail=null;
				try {
					mail = mapper.writeValueAsString(email);
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
				return session.createTextMessage(mail);
			}

		});

	}
}
